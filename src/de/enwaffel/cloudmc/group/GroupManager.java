package de.enwaffel.cloudmc.group;

import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.service.Service;

public class GroupManager {

    private final CloudSystem cloud;

    public GroupManager(CloudSystem cloud) {
        this.cloud = cloud;
    }

    public Service startServer(Group group) {
        //return cloud.getServiceManager().newService(group);
        return null;
    }

    public CloudSystem getCloud() {
        return cloud;
    }

}
