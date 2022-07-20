package de.enwaffel.cloudmc.command.commands;

import com.sun.nio.file.ExtendedCopyOption;
import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.service.Service;
import de.enwaffel.cloudmc.util.Util;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class CopyCommand extends B implements CommandExecutor {

    public CopyCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (args.length > 0) {
            String[] arr = args[0].split("-");
            if (arr.length == 2) {
                Group group = Util.groupExists(cloud, arr[0]);
                if (group != null) {
                    Service service = Util.serviceIsActive(group, Integer.parseInt(arr[1]));
                    if (service != null) {
                        try {
                            for (File file : new File(group.getTemplateFolder()).listFiles()) {
                                FileDeleteStrategy.FORCE.delete(file);
                            }
                            Files.copy(Paths.get(service.getWorkingFolder()), Paths.get(service.getGroup().getTemplateFolder()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        output.err("Service with number: [" + arr[1] + "] is not active.");
                    }
                } else {
                    output.err("Group does not exist.");
                }
            } else {
                output.err("copy <service (Example: Test-1)>");
            }
        } else {
            output.err("copy <service (Example: Test-1)>");
        }
    }

}
