package net.projectp.cloudmc.service;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.group.Group;

import java.util.UUID;

public class PreparedService extends B {

    private final Group group;
    private final UUID uuid;
    private final int type;
    private final int serviceType;
    private final int serviceNumber;

    public PreparedService(CloudSystem cloud, Group group, UUID uuid, int type, int serviceType,  int serviceNumber) {
        super(cloud);
        this.group = group;
        this.uuid = uuid;
        this.type = type;
        this.serviceType = serviceType;
        this.serviceNumber = serviceNumber;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getType() {
        return type;
    }

    public int getServiceType() {
        return serviceType;
    }

    public int getServiceNumber() {
        return serviceNumber;
    }

}