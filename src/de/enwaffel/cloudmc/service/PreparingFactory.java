package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.util.Util;
import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;

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
            Group group = request.getGroup();
            cloud.getVersionManager().downloadVersion(group.getGroupOptions().getVersion());
            int type = group.getGroupOptions().getServerType();
            UUID id = UUID.randomUUID();
            int serviceNumber = request.getGroup().getTotalServiceCount() + 1;
            String path = type == 0 ? "services/temp/" + group.getName() + "-" + serviceNumber + "-" + id : "services/static/" + group.getName() + "-" + serviceNumber;

            cloud.getLogger().i("Preparing Service [type: " + group.getGroupOptions().getVersion().getProvider().getName() + "/" + group.getName() + "-" + serviceNumber + " (" + id + ")]");

            if (!new File(path).exists()) {
                copyTemplate(group, path, true);
            } else {
                copyTemplate(group, path, false);
            }
            PreparedService preparedService = new PreparedService(cloud, group, id, serviceNumber, path);
            group.getPreparedServices().add(preparedService);
            request.getCallback().finish(preparedService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyTemplate(Group group, String path, boolean copyDefaultTemplate) throws IOException {
        String templatePath = "templates/" + group.getName();
        new File(path).mkdirs();
        new File(path + "/plugins").mkdirs();
        String jarPath = path + "/" + group.getGroupOptions().getVersion().getName() + ".jar";

        if (!copyDefaultTemplate) {
            Files.copy(Paths.get(templatePath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        }

        //Util.createIfNotExist(new File(jarPath));
        FileUtil.writeFile("eula=true", new FileOrPath(path + "/eula.txt"));
        Util.copyIfNotExist("versions/" + group.getGroupOptions().getVersion().getName() + ".jar", jarPath);
        Util.copyIfNotExist("C:/Users/leoar/OneDrive/Desktop/JavaStuff/CloudMC/out/artifacts/SpigotAPI/CloudMCAPI.jar", path + "/plugins/CloudMCAPI.jar");
    }

}
