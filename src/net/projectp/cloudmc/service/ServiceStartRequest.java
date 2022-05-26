package net.projectp.cloudmc.service;

import net.projectp.cloudmc.group.Group;

import java.util.UUID;

public class ServiceStartRequest {

    private final Group group;
    private final UUID uuid;

    public ServiceStartRequest(Group group, UUID uuid) {
        this.group = group;
        this.uuid = uuid;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getUUID() {
        return uuid;
    }

}
