package net.projectp.cloudmc.service;

import net.projectp.cloudmc.group.Group;

public class ServicePrepareRequest {

    private final Group group;
    private final int type;
    private final int serviceType;

    public ServicePrepareRequest(Group group, int type, int serviceType) {
        this.group = group;
        this.type = type;
        this.serviceType = serviceType;
    }

    public Group getGroup() {
        return group;
    }

    public int getType() {
        return type;
    }

    public int getServiceType() {
        return serviceType;
    }

}
