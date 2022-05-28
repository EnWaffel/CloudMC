package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.group.Group;

import java.util.UUID;

public class PreparedService extends B {

    private final Group group;
    private final UUID uuid;
    private final int serviceNumber;
    private final String workingFolder;

    public PreparedService(CloudSystem cloud, Group group, UUID uuid, int serviceNumber, String workingFolder) {
        super(cloud);
        this.group = group;
        this.uuid = uuid;
        this.serviceNumber = serviceNumber;
        this.workingFolder = workingFolder;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getServiceNumber() {
        return serviceNumber;
    }

    public String getWorkingFolder() {
        return workingFolder;
    }

}