package net.projectp.cloudmc.service;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.group.Group;
import net.projectp.cloudmc.util.MCVersion;
import net.projectp.cloudmc.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
        cloud.getLogger().i("Requesting Prepare-Task for " + request + "...");
        if (!working) {
            working = true;
            work(request);
        } else {
            queue.add(request);
        }
    }

    private void work(ServicePrepareRequest request) {
        try {
            int type = request.getType();
            int serviceType = request.getServiceType();
            Group group = request.getGroup();
            UUID id = UUID.randomUUID();
            String mcVersion = MCVersion.fromServerType(serviceType);
            int serviceNumber = request.getGroup().getTotalServiceCount() + 1;
            String path = type == 0 ? "services/temp/" + id : "services/static/" + id;

            cloud.getLogger().i("Preparing Service [type: " + mcVersion + "/" + group.getName() + "-" + serviceNumber + " (" + id + ")]");

            if (!templateExists(path)) {
                copyTemplate(group, path, mcVersion,  true);
            } else {
                copyTemplate(group, path, mcVersion, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyTemplate(Group group, String path, String mcVersion, boolean copyDefaultTemplate) throws IOException {
        String templatePath = "templates/" + group.getName();

        if (!copyDefaultTemplate) {
            Files.copy(Paths.get(templatePath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        }

        Util.copyIfNotExist(path + "/spigot.jar", "versions/" + mcVersion + "-" + group.getGroupOptions().getVersionName() + ".jar");
    }

    private boolean templateExists(String theoreticalPath) {
        return new File(theoreticalPath).exists();
    }

}
