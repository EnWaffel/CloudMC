package de.enwaffel.cloudmc.command.commands;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.command.CommandExecutor;
import de.enwaffel.cloudmc.command.ConsoleOutput;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.service.PreparedService;
import de.enwaffel.cloudmc.service.ServicePrepareRequest;
import de.enwaffel.cloudmc.service.ServiceStartRequest;

public class ServiceCommand extends B implements CommandExecutor {

    public ServiceCommand(CloudSystem cloud) {
        super(cloud);
    }

    @Override
    public void onCommand(String[] args, ConsoleOutput output) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "prepare": {
                    if (args.length > 1) {
                        if (cloud.groupExists(args[1])) {
                            cloud.getServiceFactory().prepareService(new ServicePrepareRequest((preparedService) -> {}, cloud.getGroup(args[1])));
                        } else {
                            output.err("Group: '" + args[1] + "' doesn't exist!");
                        }
                    } else {
                        output.err("service prepare [group name]");
                    }
                    break;
                }
                case "start": {
                    if (args.length > 1) {
                        if (cloud.groupExists(args[1])) {
                            Group group = cloud.getGroup(args[1]);
                            PreparedService preparedService = group.getPreparedServiceByName(args[2]);
                            if (preparedService != null) {
                                cloud.getServiceFactory().startService(new ServiceStartRequest((service) -> {}, preparedService));
                            } else {
                                cloud.getServiceFactory().prepareService(new ServicePrepareRequest((_preparedService) -> {
                                    cloud.getServiceFactory().startService(new ServiceStartRequest((service) -> {}, _preparedService));
                                }, cloud.getGroup(args[1])));
                                //output.err("There is no prepared-service with name: '" + args[2] + "'!");
                            }
                        } else {
                            output.err("Group: '" + args[1] + "' doesn't exist!");
                        }
                    } else {
                        output.err("service start [prepared service name (example: Test-1)]");
                    }
                    break;
                }
            }
        } else {
            output.err("service <prepare, start, stop>");
        }
    }

}
