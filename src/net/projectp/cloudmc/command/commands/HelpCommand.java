package net.projectp.cloudmc.command.commands;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;

public class HelpCommand extends B implements CommandExecutor {

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