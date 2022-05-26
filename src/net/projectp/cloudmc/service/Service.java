package net.projectp.cloudmc.service;

import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.group.Group;
import net.projectp.cloudmc.jvm.JVM;

import java.util.UUID;

public class Service extends PreparedService {

    private final JVM jvm;

    public Service(CloudSystem cloud, Group group, UUID uuid, int type, int serviceType, int serviceNumber, JVM jvm) {
        super(cloud, group, uuid, type, serviceType, serviceNumber);
        this.jvm = jvm;
    }

    public JVM getJVM() {
        return jvm;
    }

}
