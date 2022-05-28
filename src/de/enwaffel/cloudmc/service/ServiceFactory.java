package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;

public class ServiceFactory extends B {

    private final PreparingFactory preparingFactory;
    private final StartingFactory startingFactory;

    public ServiceFactory(CloudSystem cloud) {
        super(cloud);
        this.preparingFactory = new PreparingFactory(cloud);
        startingFactory = new StartingFactory(cloud);
    }

    public void prepareService(ServicePrepareRequest request) {
        preparingFactory.request(request);
    }

    public void startService(ServiceStartRequest request) {
        startingFactory.request(request);
    }

}
