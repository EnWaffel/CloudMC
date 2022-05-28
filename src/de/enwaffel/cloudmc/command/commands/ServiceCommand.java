package de.enwaffel.cloudmc.command.commands;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;
import de.enwaffel.cloudmc.service.ServicePrepareRequest;
import de.enwaffel.cloudmc.service.ServiceStartRequest;
import org.json.JSONObject;

public class ServiceCommand extends B implements CommandExecutor {

    public ServiceCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "start": {
                    if (args.length > 1) {
                        if (cloud.groupExists(args[1])) {
                            cloud.getServiceFactory().prepareService(new ServicePrepareRequest((preparedService) -> {
                                cloud.getServiceFactory().startService(new ServiceStartRequest((service) -> {
                                    output.info("Done!");
                                }, preparedService));
                            }, cloud.getGroup(args[1])));
                        } else {
                            output.err("Group: '" + args[1] + "' doesn't exist!");
                        }
                    } else {
                        output.err("service start [group-name]");
                    }
                    break;
                }
                case "a": {
                    cloud.getGroup("test").sendPacketToService(cloud.getGroup("test").getActiveServices().get(0).getUUID(), new JSONObject().put("a", args[1]));
                    break;
                }
            }
        } else {
            output.err("service <start, stop>");
        }
    }

}
