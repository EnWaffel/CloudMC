package net.projectp.cloudmc.service;

import net.projectp.cloudmc.group.Group;
import net.projectp.cloudmc.util.Request;

public class ServicePrepareRequest extends Request<ServicePreparedCallback> {

    private final Group group;
    private final int type;
    private final int serviceType;

    public ServicePrepareRequest(ServicePreparedCallback callback, Group group, int type, int serviceType) {
        super(callback);
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

    @Override
    public String toString() {
        return "ServicePrepareRequest{" +
                "group=" + group.getName() +
                ", type=" + type +
                ", serviceType=" + serviceType +
                '}';
    }

}
