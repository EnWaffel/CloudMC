package net.projectp.cloudmc.command.commands;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;
import net.projectp.cloudmc.command.QuestionSequence;
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
                                switch (text) {
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
                            default: return new Fail("invalid sequence");
                        }
                    },
                            "How would you like to name the new group?",
                            "What Server-Type should the group be?\n- spigot\n- bukkit\n- bungeecord",
                            "What Type should the group be?\n- dynamic (Server will be deleted when stopped)\n- static (Server will not be deleted when stopped)"
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
