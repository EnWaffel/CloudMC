package net.projectp.cloudmc.command.dcmds;

import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.command.CommandExecutor;
import net.projectp.cloudmc.command.ConsoleOutput;

public class DEFAULT_HELP implements CommandExecutor {

    private final CloudSystem cloudSystem;

    public DEFAULT_HELP(CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
    }

    @Override
    public void onCommand(String[] args,ConsoleOutput output) {
        output.info("Commands:");
        output.info("-help <help,info,h,i>");
        output.info("-group <create>");
    }

    public CloudSystem getCloudSystem() {
        return cloudSystem;
    }

}