package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.util.Request;

public class ServicePrepareRequest extends Request<ServicePreparedCallback> {

    private final Group group;

    public ServicePrepareRequest(ServicePreparedCallback callback, Group group) {
        super(callback);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

}
