package net.projectp.cloudmc.command.dcmds;

import net.projectp.cloudmc._;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;
import net.projectp.cloudmc.command.QuestionSequence;
import net.projectp.cloudmc.util.result.Fail;
import net.projectp.cloudmc.util.result.Success;

public class GroupCommand extends _ implements CommandExecutor {

    public GroupCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "create": {
                    QuestionSequence sequence = new QuestionSequence((i, text) -> {
                        switch (i) {
                            case 0: {
                                if (!text.contains(" ")) {

                                    return new Success();
                                } else {
                                    return new Fail("Name cannot contain spaces!");
                                }
                            }
                            default: return new Fail("invalid sequence");
                        }
                    }, "How would you like to name the new group?");
                    cloud.getConsole().ask(sequence);
                    break;
                }
            }
        } else {
            output.err("group <create, modify, delete>");
        }
    }
}
