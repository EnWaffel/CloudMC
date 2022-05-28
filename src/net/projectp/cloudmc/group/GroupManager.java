package net.projectp.cloudmc.group;

import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.service.Service;

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
