package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.util.RequestCallback;

public interface ServicePreparedCallback extends RequestCallback {
     void finish(PreparedService preparedService);
}
