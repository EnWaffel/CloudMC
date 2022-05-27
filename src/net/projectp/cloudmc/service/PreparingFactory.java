package net.projectp.cloudmc.service;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.cloud.CloudSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PreparingFactory extends B {

    private final List<ServicePrepareRequest> queue = new ArrayList<>();
    private boolean working;

    public PreparingFactory(CloudSystem cloud) {
        super(cloud);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!working && queue.size() > 0) {
                    request(queue.get(0));
                }
            }
        }, 0, 1);
    }

    public void request(ServicePrepareRequest request) {
        if (!working) {
            working = true;
            work(request);
        } else {
            queue.add(request);
        }
    }

    public void work(ServicePrepareRequest request) {

    }

}
