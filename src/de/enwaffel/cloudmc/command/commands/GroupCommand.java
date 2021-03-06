package de.enwaffel.cloudmc.command.commands;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.QuestionSequence;
import de.enwaffel.cloudmc.util.Util;
import de.enwaffel.cloudmc.util.result.Fail;
import de.enwaffel.cloudmc.util.result.Success;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;
import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GroupCommand extends B implements CommandExecutor {

    public GroupCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "create": {
                    AtomicReference<String> name = new AtomicReference<>("");
                    AtomicReference<String> versionType = new AtomicReference<>("");
                    AtomicReference<String> version = new AtomicReference<>("");
                    AtomicInteger serverType = new AtomicInteger(-1);
                    AtomicInteger maxMemory = new AtomicInteger(-1);
                    AtomicInteger minServices = new AtomicInteger(-1);

                    QuestionSequence sequence = new QuestionSequence((i, text) -> {
                        switch (i) {
                            case -1: {
                                output.info("Creating group...");
                                JSONObject o = new JSONObject();
                                o.put("name", name);
                                o.put("versionType", versionType);
                                o.put("version", version);
                                o.put("serverType", serverType);
                                o.put("minServices", minServices);
                                o.put("jvm", new JSONObject().put("maxMemory", maxMemory));
                                FileUtil.writeFile(o, FileOrPath.path("groups/" + name + ".json"));
                                return new Success();
                            }
                            case 0: {
                                if (!text.contains(" ")) {
                                    name.set(text);
                                    return new Success();
                                } else {
                                    return new Fail("Name cannot contain spaces!");
                                }
                            }
                            case 1: {
                                versionType.set(text);
                                return new Success();
                            }
                            case 2: {
                                switch (text.toLowerCase()) {
                                    case "dynamic": {
                                        serverType.set(0);
                                        return new Success();
                                    }
                                    case "static": {
                                        serverType.set(1);
                                        return new Success();
                                    }
                                    default: return new Fail("");
                                }
                            }
                            case 3: {
                                version.set(text);
                                return new Success();
                            }
                            case 4: {
                                Integer num = Util.isNumber(text);
                                if (num != null) {
                                    if (num > 0) {
                                        maxMemory.set(num);
                                    } else {
                                        return new Fail("Argument cannot be lower than 1!");
                                    }
                                } else {
                                    return new Fail("Argument must be a integer!");
                                }
                                return new Success();
                            }
                            case 5: {
                                Integer num = Util.isNumber(text);
                                if (num != null) {
                                    if (num > -1) {
                                        minServices.set(num);
                                    } else {
                                        return new Fail("Argument cannot be lower than 0!");
                                    }
                                } else {
                                    return new Fail("Argument must be a integer!");
                                }
                                return new Success();
                            }
                            default: return new Fail("invalid sequence");
                        }
                    },
                            "How would you like to name the new group?",
                            "What Server-Type should the group be?\n- spigot\n- bukkit\n- bungeecord",
                            "What Type should the group be?\n- dynamic (Server will be deleted when stopped)\n- static (Server will not be deleted when stopped)",
                            "What Version should the group be? (example: '1.18' or 'latest')",
                            "How much memory should a service have at max? (in MB)",
                            "How many services should be running always?"

                    );
                    cloud.getConsole().ask(sequence);
                    break;
                }
            }
        } else {
            output.err("group <create, modify, delete>");
        }
    }
}
