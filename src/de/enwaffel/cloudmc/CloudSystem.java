package de.enwaffel.cloudmc;

import de.enwaffel.cloudmc.api.event.Event;
import de.enwaffel.cloudmc.api.event.EventHandler;
import de.enwaffel.cloudmc.api.event.EventListener;
import de.enwaffel.cloudmc.command.CommandConsole;
import de.enwaffel.cloudmc.command.commands.*;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.main.Main;
import de.enwaffel.cloudmc.service.Service;
import de.enwaffel.cloudmc.service.ServiceFactory;
import de.enwaffel.cloudmc.task.Scheduler;
import de.enwaffel.cloudmc.util.Util;
import de.enwaffel.cloudmc.version.VersionManager;
import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.callback.Callback;
import net.projectp.network.channel.ChannelInfo;
import net.projectp.network.channel.SCIL;
import net.projectp.network.packet.JSONPacket;
import net.projectp.network.packet.Packet;
import net.projectp.network.server.ClientConnection;
import net.projectp.network.server.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class CloudSystem {

    private final HashMap<String, JSONObject> configs = new HashMap<>();
    private final List<Group> groups = new ArrayList<>();

    private final Logger logger;
    private final CommandConsole console;
    private final ServiceFactory serviceFactory;
    private final Scheduler scheduler;
    private final VersionManager versionManager;
    private Server networkServer;

    private final List<EventListener> eventListeners = new ArrayList<>();

    private final String version = "0.0.01";
    private boolean shuttingDown = false;
    private volatile int servicesToWaitFor = 0;
    private String welcomeText = "";

    public CloudSystem() {
        this.logger = new Logger(this);
        this.console = new CommandConsole(this);
        this.serviceFactory = new ServiceFactory(this);
        this.scheduler = new Scheduler(this);
        this.versionManager = new VersionManager(this);
        load();
    }

    private void load() {
        logger.i("loading...");
        loadFiles();
        // welcome text
        logger.i(welcomeText);

        // internal commands

        console.registerCommand("help", new HelpCommand(this));
        console.registerCommand("info", new HelpCommand(this));
        console.registerCommand("h", new HelpCommand(this));
        console.registerCommand("i", new HelpCommand(this));
        console.registerCommand("group", new GroupCommand(this));
        console.registerCommand("service", new ServiceCommand(this));
        console.registerCommand("stop", new StopCommand(this));
        console.registerCommand("copy", new CopyCommand(this));
    }

    // file stuff

    private void loadFiles() {
        String defaultURL = "https://assets.bierfrust.de/webservice/cloudmc/";
        welcomeText = "\n" + Util.readFromUrl(defaultURL + "welcometext.txt");
        logger.i("loading files...");
        createFolders("groups", "modules", "services", "services/temp", "services/static", "versions");
        JSONObject versionCfg = Util.readJsonFromUrl(defaultURL + "version.json");
        if (versionCfg == null) {
            logger.e("Failed to load files; A core file is null!");
            System.exit(-1);
        }
        logger.i("Loading versions...");
        JSONArray minecraftBuilds = Util.readJsonArrayFromUrl(defaultURL + "mc-builds.json");
        if (minecraftBuilds == null) {
            logger.e("Failed to load files; A core file is null!");
            System.exit(-1);
        }
        for (Object _o : minecraftBuilds) {
            JSONObject o = new JSONObject(_o.toString());
            versionManager.loadVersion(o);
        }

        FileUtil.writeFileIf(new JSONObject().put("server", new JSONObject()
                .put("ip", "localhost")
                .put("startArgs", "java -Xmx%maxRam%MB -jar spigot.jar")
        ), FileOrPath.path("config.json"), !FileOrPath.path("config.json").getFile().exists());
        logger.i("Loading configs...");
        loadConfigs(FileOrPath.path("config.json"));
        logger.i("Loading groups...");

        for (File _group : Objects.requireNonNull(new File("groups").listFiles())) {
            if (_group.isFile()) {
                if (net.projectp.util.Util.validExtension(_group.getPath(), "json")) {
                    groups.add(Group.fromJson(this, FileUtil.readJSON(FileOrPath.file(_group))));
                } else {
                    logger.e("File: '" + _group.getPath() + "' is not a json file!");
                }
            }
        }
        logger.i("Starting network server...");
        this.networkServer = new Server(49152);
        networkServer.open();
        networkServer.getDefaultChannel().addHandle(new SCIL() {
            @Override
            public void handle(ClientConnection clientConnection, ChannelInfo channelInfo, Packet<?> packet, String s) {
                if (packet instanceof JSONPacket) {
                    JSONPacket packet1 = (JSONPacket) packet;
                    JSONObject data = packet1.getData();
                    if (data.getString("type").equals("serviceCommunication")) {
                        switch (data.getString("action")) {
                            case "started": {
                                for (Group group : groups) {
                                    for (Service service : group.getActiveServices()) {
                                        if  (service.getUUID().toString().equals(data.getString("serviceId"))) {
                                            service.setNetworkClient(clientConnection);
                                            service.setConnected(true);
                                            networkServer.getDefaultChannel().sendPacket(clientConnection, new JSONPacket(networkServer.getDefaultChannel().info(), new JSONObject().put("type", "serviceCommunication").put("action", "connected")));
                                            logger.i("Service Connected [" + group.getGroupOptions().getVersion().getProvider().getName() + "/" + service.name() + " (" + service.getUUID() + ")]");
                                        }
                                    }
                                }
                                break;
                            }
                            case "stopped": {
                                for (Group group : groups) {
                                    for (int i = 0; i < group.getActiveServices().size();i++) {
                                        Service service = group.getActiveServices().get(i);
                                        if (service != null) {
                                            if  (service.getUUID().toString().equals(data.getString("serviceId"))) {
                                                service.setConnected(false);
                                                logger.i("Service Disconnected [" + group.getGroupOptions().getVersion().getProvider().getName() + "/" + service.name() + " (" + service.getUUID() + ")]");
                                                group.stopService(service);
                                                servicesToWaitFor--;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void handleRaw(byte[] bytes, String s) {

            }
        });

        if (!versionCfg.getString("version").equals(Main.getVersion())) {
            logger.w("Hey there, you are running an outdated version of CloudMC! Download a new build here: https://github.com/EnWaffel/CloudMC/releases/latest");
        } else {
            logger.i("Everything is up to date!");
            if (!versionCfg.getBoolean("stable")) {
                logger.w("Hey there, you are running an unstable version! Please download a stable build here: https://github.com/EnWaffel/CloudMC/releases/latest");
            }
        }
    }

    // module stuff

    private void loadModules() {

    }

    // config stuff

    public void createFolders(String... paths) {
        for (String path : paths) {
            new File(path).mkdirs();
        }
    }

    private void loadConfigs(FileOrPath... configs) {
        try {
            for (FileOrPath file : configs) {
                this.configs.put(file.getFile().getName().split("\\.")[0], FileUtil.readJSON(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // event stuff

    public void registerEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    private boolean hasEventAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(EventHandler.class)) {
                return true;
            }
        }
        return false;
    }

    public void callEvent(Event event) {
        try {
            for (EventListener eventListener : eventListeners) {
                for (Method method : eventListener.getClass().getDeclaredMethods()) {
                    if (hasEventAnnotation(method.getAnnotations())) {
                        Object o = Class.forName(eventListener.getClass().getName()).newInstance();
                        method.setAccessible(true);
                        method.invoke(o,event);
                        method.setAccessible(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (!shuttingDown) {
            shuttingDown = true;
            logger.i("Shutting down...");
            logger.i("Stopping services...");
            for (Group group : groups) {
                for (int i = 0;i < group.getActiveServices().size();i++) {
                    Service service = group.getActiveServices().get(i);
                    if (service != null) {
                        if (service.isConnected()) {
                            servicesToWaitFor++;
                            service.getGroup().shutdownService(service);
                        } else {
                            service.getJVM().stop(false);
                            group.stopService(service);
                        }
                    }
                }
            }
            while (servicesToWaitFor > 0) Thread.onSpinWait();
            System.exit(0);
        }
    }

    // getters / setters

    public Logger getLogger() {
        return logger;
    }

    public JSONObject getConfig(String name) {
        return configs.get(name);
    }

    public CommandConsole getConsole() {
        return console;
    }

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }

    public Server getNetworkServer() {
        return networkServer;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public int getServicesToWaitFor() {
        return servicesToWaitFor;
    }

    public boolean groupExists(String name) {
        return getGroup(name) != null;
    }

    public Group getGroup(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    //public String getVersion() { return getConfig("version").getString("version"); }


    public void setServicesToWaitFor(int servicesToWaitFor) {
        this.servicesToWaitFor = servicesToWaitFor;
    }

}