package de.enwaffel.cloudmc.command.commands;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;

public class HelpCommand extends B implements CommandExecutor {

    public HelpCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        output.info("Commands:");
        output.info("- help (help, info, h, i)");
        output.info("- group <create, modify, delete>");
        output.info("- service <start, stop>");
    }

}