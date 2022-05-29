package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.jvm.JVM;
import de.enwaffel.cloudmc.util.Util;
import de.enwaffel.randomutils.file.FileOrPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartingFactory extends B {

    private final List<ServiceStartRequest> queue = new ArrayList<>();
    private boolean working;

    public StartingFactory(CloudSystem cloud) {
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

    public void request(ServiceStartRequest request) {
        cloud.getLogger().i("Requesting Start-Task for " + request + "...");
        if (!working) {
            working = true;
            work(request);
        } else {
            queue.add(request);
        }
    }

    private void work(ServiceStartRequest request) {
        PreparedService preparedService = request.getPreparedService();
        Group group = request.getPreparedService().getGroup();
        for (Service service : group.getActiveServices()) {
            if (service.name().equals(preparedService.name())) {
                cloud.getLogger().e("Service is already running: '" + preparedService.name() + "'!");
                return;
            }
        }
        cloud.getLogger().i("Starting Service [" + group.getGroupOptions().getVersion().getProvider().getName() + "/" + preparedService.name() + " (" + preparedService.getUUID() + ")]");
        group.getPreparedServices().remove(preparedService);
        String path = preparedService.getWorkingFolder();
        String jarPath = path + "/" + preparedService.getGroup().getGroupOptions().getVersion().getName() + ".jar";
        JVM jvm = new JVM(cloud);
        Service service = new Service(cloud, preparedService.getGroup(), preparedService.getUUID(), preparedService.getServiceNumber(), preparedService.getWorkingFolder(), jvm, Util.randomNumber(49153, 65535));
        request.getPreparedService().getGroup().getActiveServices().add(service);
        jvm.start(new FileOrPath(jarPath), new String[]{"-Xmx500MB", "-Dcom.mojang.eula.agree=true"}, "nogui", "o false" ,"p " + service.getServerPort());
        request.getCallback().finish(service);
    }

}
