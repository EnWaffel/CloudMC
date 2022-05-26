package net.projectp.cloudmc.service;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.group.Group;
import net.projectp.cloudmc.util.Util;
import net.projectp.cloudmc.util.result.Fail;
import net.projectp.cloudmc.util.result.Result;
import net.projectp.cloudmc.util.result.Success;

import java.util.HashMap;
import java.util.UUID;

public class ServiceManager {

    private final CloudSystem cloud;
    private final HashMap<UUID, Service> activeServices = new HashMap<>();

    public ServiceManager(CloudSystem cloud) {
        this.cloud = cloud;
    }

    public Service newService(Group group, int type) {
        try {
            UUID serviceId = UUID.randomUUID();
            Result result = createServiceFiles(group, serviceId, type);
            if (result instanceof Fail) {
                cloud.getLogger().err(((Fail) result).getMessage());
                return null;
            }
            String cmd = cloud.getConfig("config").getJSONObject("server").getString("start_args");
            cmd.replaceAll("%maxRam%", "1");

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            Service service = new Service(group, serviceId, type, process, cloud);
            activeServices.put(serviceId, service);
            return service;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result createServiceFiles(Group group, UUID serviceId, int type) {
        if (activeServices.containsKey(serviceId))
            return new Fail("A service with same id is already running!");

        String path = "services/";
        switch (type) {
            case 0: {
                path += "temp/" + group.getName() + "-" + serviceId;
                cloud.createFolders(path);
                createEula(path);
                return new Success();
            }
            case 1: {
                path += "static/" + group.getName() + "-" + serviceId;
                createEula(path);
                cloud.createFolders(path);
                return new Success();
            }
            default: return new Fail("Service-Type not found: " + type);
        }
    }

    private void createEula(String path) {
        FileUtil.writeFile("eula=true", new FileOrPath(path));
    }

}
