package net.projectp.cloudmc.cloud;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.command.CommandConsole;
import net.projectp.cloudmc.command.dcmds.DEFAULT_HELP;
import net.projectp.cloudmc.event.Event;
import net.projectp.cloudmc.event.EventHandler;
import net.projectp.cloudmc.event.EventListener;
import net.projectp.cloudmc.main.Main;
import net.projectp.cloudmc.util.Util;
import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloudSystem {

    private final Properties properties;
    private final HashMap<String,JSONObject> configs = new HashMap<>();

    private final Logger logger;
    private final CommandConsole console;

    private final List<EventListener> eventListeners = new ArrayList<>();

    private final String version = "0.0.01";

    public CloudSystem(Properties properties) {
        this.properties = properties;
        this.logger = new Logger(this);
        this.console = new CommandConsole(this);
        load();
    }

    private void load() {
        logger.info("loading...",true);
        loadFiles();
            // welcome text
        logger.info(
                "   _____   _                       _   __  __    _____      ___        _____       ______ \n" +
                        "  / ____| | |                     | | |  \\/  |  / ____|    / _ \\      | ____|     |____  |\n" +
                        " | |      | |   ___    _   _    __| | | \\  / | | |        | | | |     | |__           / / \n" +
                        " | |      | |  / _ \\  | | | |  / _` | | |\\/| | | |        | | | |     |___ \\         / /  \n" +
                        " | |____  | | | (_) | | |_| | | (_| | | |  | | | |____    | |_| |  _   ___) |  _    / /   \n" +
                        "  \\_____| |_|  \\___/   \\__,_|  \\__,_| |_|  |_|  \\_____|    \\___/  (_) |____/  (_)  /_/    \n" +
                        "                                                                                          \n" +
                        "                                                                                          ",false);

        // internal commands

        console.registerCommand("help",new DEFAULT_HELP(this));
        console.registerCommand("info",new DEFAULT_HELP(this));
        console.registerCommand("h",new DEFAULT_HELP(this));
        console.registerCommand("i",new DEFAULT_HELP(this));


    }

    // file stuff

    private void loadFiles() {
        String defaultURL = "https://assets.bierfrust.de/webservice/cloudmc/";
        logger.info("loading files...",true);
        //JSONObject v = Util.readJsonFromUrl(defaultURL+"version.json");

        File cfg = new File("config.json");
        Util.saveFile(cfg);

        Util.writeFile(cfg,"{%" +
                "$test$:" +
                "$teet$" +
                "" +
                "}",false);
        loadConfigs(new File[]{cfg});

        if (!"".equals(Main.getVersion())) {
            logger.warn("You are running an outdated version of CloudMC! Download a new build here: https://github.com/EnWaffel/CloudMC/releases/");
        } else {
            logger.info("Everything is up to date!",true);
        }

    }

    // module stuff

    private void loadModules() {

    }

    // config stuff

    private void loadConfigs(File[] configs) {
        try {
            for (File file : configs) {
                this.configs.put(file.getName().split("\\.")[0], FileUtil.readJSON(new FileOrPath(file)));
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

    public Properties getProperties() {
        return properties;
    }

    public CommandConsole getConsole() {
        return console;
    }

    //public String getVersion() { return getConfig("version").getString("version"); }

}