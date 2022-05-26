package net.projectp.cloudmc.command.dcmds;

import net.projectp.cloudmc._;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;

public class HelpCommand extends _ implements CommandExecutor {

    public HelpCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        output.info("Commands:");
        output.info("-help <help,info,h,i>");
        output.info("-group <create>");
    }

}