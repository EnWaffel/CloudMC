package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.util.RequestCallback;

public interface ServiceStartedCallback extends RequestCallback {
    void finish(Service service);
}
