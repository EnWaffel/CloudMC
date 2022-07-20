package de.enwaffel.cloudmc.command.commands;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;

public class StopCommand extends B implements CommandExecutor {


    public StopCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (!cloud.isShuttingDown()) {
            cloud.shutdown();
        }
    }

}
