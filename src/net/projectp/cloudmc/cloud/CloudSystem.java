package net.projectp.cloudmc.cloud;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.api.event.Event;
import net.projectp.cloudmc.api.event.EventHandler;
import net.projectp.cloudmc.api.event.EventListener;
import net.projectp.cloudmc.command.CommandConsole;
import net.projectp.cloudmc.command.QuestionSequence;
import net.projectp.cloudmc.command.dcmds.GroupCommand;
import net.projectp.cloudmc.command.dcmds.HelpCommand;
import net.projectp.cloudmc.main.Main;
import net.projectp.cloudmc.service.ServiceFactory;
import net.projectp.cloudmc.util.result.Success;
import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloudSystem {

    private final HashMap<String, JSONObject> configs = new HashMap<>();

    private final Logger logger;
    private final CommandConsole console;
    public final ServiceFactory serviceFactory;

    private final List<EventListener> eventListeners = new ArrayList<>();

    private final String version = "0.0.01";

    public CloudSystem() {
        this.logger = new Logger(this);
        this.console = new CommandConsole(this);
        this.serviceFactory = new ServiceFactory(this);
        load();
    }

    private void load() {
        logger.i("loading...");
        loadFiles();
        // welcome text
        logger.i(
                "Enabling:\n   _____   _                       _   __  __    _____      ___        _____       ______ \n" +
                        "  / ____| | |                     | | |  \\/  |  / ____|    / _ \\      | ____|     |____  |\n" +
                        " | |      | |   ___    _   _    __| | | \\  / | | |        | | | |     | |__           / / \n" +
                        " | |      | |  / _ \\  | | | |  / _` | | |\\/| | | |        | | | |     |___ \\         / /  \n" +
                        " | |____  | | | (_) | | |_| | | (_| | | |  | | | |____    | |_| |  _   ___) |  _    / /   \n" +
                        "  \\_____| |_|  \\___/   \\__,_|  \\__,_| |_|  |_|  \\_____|    \\___/  (_) |____/  (_)  /_/    \n" +
                        "                                                                                          \n" +
                        "                                                                                          ");

        // internal commands

        console.registerCommand("help", new HelpCommand(this));
        console.registerCommand("info", new HelpCommand(this));
        console.registerCommand("h", new HelpCommand(this));
        console.registerCommand("i", new HelpCommand(this));
        console.registerCommand("group", new GroupCommand(this));

        //serviceFactory.requestPreparedService(new ServicePrepareRequest(new Group("test", new FileOrPath("")), 1, 1));
        QuestionSequence sequence = new QuestionSequence((i, text) -> new Success(), "test", "asdsad");
        //console.ask(sequence);
    }

    // file stuff

    private void loadFiles() {
        String defaultURL = "https://assets.bierfrust.de/webservice/cloudmc/";
        logger.i("loading files...");
        createFolders("groups", "modules", "services", "services/temp", "services/static");
        //JSONObject v = Util.readJsonFromUrl(defaultURL+"version.json");

        FileUtil.writeFileIf(new JSONObject().put("server", new JSONObject()
                .put("ip", "localhost")
                .put("startArgs", "java -Xmx%maxRam%MB -jar spigot.jar")
        ), new FileOrPath("config.json"), !new FileOrPath("config.json").getFile().exists());
        loadConfigs(new FileOrPath("config.json"));

        if (!"".equals(Main.getVersion())) {
            logger.warn("Hey there, you are running an outdated version of CloudMC! Download a new build here: https://github.com/EnWaffel/CloudMC/releases/");
        } else {
            logger.i("Everything is up to date!");
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

    //public String getVersion() { return getConfig("version").getString("version"); }

}