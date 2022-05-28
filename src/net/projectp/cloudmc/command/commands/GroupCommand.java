package net.projectp.cloudmc.command.commands;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;
import net.projectp.cloudmc.command.QuestionSequence;
import net.projectp.cloudmc.util.Util;
import net.projectp.cloudmc.util.result.Fail;
import net.projectp.cloudmc.util.result.Success;

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
                    AtomicReference<String> storedName = new AtomicReference<>("");
                    AtomicInteger storedServerType = new AtomicInteger(-1);
                    AtomicInteger storedType = new AtomicInteger(-1);
                    AtomicReference<String> storedVersion = new AtomicReference<>("");
                    AtomicInteger storedMaxMemory = new AtomicInteger(-1);
                    AtomicInteger storedMinServices = new AtomicInteger(-1);

                    QuestionSequence sequence = new QuestionSequence((i, text) -> {
                        switch (i) {
                            case -1: {
                                output.info("Creating group...");
                                return new Success();
                            }
                            case 0: {
                                if (!text.contains(" ")) {
                                    storedName.set(text);
                                    return new Success();
                                } else {
                                    return new Fail("Name cannot contain spaces!");
                                }
                            }
                            case 1: {
                                switch (text.toLowerCase()) {
                                    case "spigot": {
                                        storedServerType.set(1);
                                        return new Success();
                                    }
                                    case "bukkit": {
                                        storedServerType.set(0);
                                        return new Success();
                                    }
                                    case "bungeecord": {
                                        storedServerType.set(2);
                                        return new Success();
                                    }
                                    default: return new Fail("Invalid Sever-Type!");
                                }
                            }
                            case 2: {
                                switch (text.toLowerCase()) {
                                    case "dynamic": {
                                        storedType.set(0);
                                        return new Success();
                                    }
                                    case "static": {
                                        storedType.set(1);
                                        return new Success();
                                    }
                                    default: return new Fail("");
                                }
                            }
                            case 3: {
                                return new Success();
                            }
                            case 4: {
                                Integer num = Util.isNumber(text);
                                if (num != null) {
                                    if (num > 0) {
                                        storedMaxMemory.set(num);
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
                                        storedMinServices.set(num);
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
