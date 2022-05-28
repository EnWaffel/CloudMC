package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.util.Request;

public class ServiceStartRequest extends Request<ServiceStartedCallback> {

    private final PreparedService preparedService;

    public ServiceStartRequest(ServiceStartedCallback callback, PreparedService preparedService) {
        super(callback);
        this.preparedService = preparedService;
    }

    public PreparedService getPreparedService() {
        return preparedService;
    }

}
