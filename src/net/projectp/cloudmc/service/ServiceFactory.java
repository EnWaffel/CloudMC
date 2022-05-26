package net.projectp.cloudmc.service;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.group.Group;
import net.projectp.cloudmc.jvm.JVM;
import net.projectp.cloudmc.util.MCVersion;
import net.projectp.cloudmc.util.result.Fail;
import net.projectp.cloudmc.util.result.Result;
import net.projectp.cloudmc.util.result.Success;
import net.projectp.http.HttpFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ServiceFactory {

    public static String defaultServerFileDownloadLink = "https://download.getbukkit.org/";

    private final CloudSystem cloud;
    private final HashMap<UUID, Service> activeServices = new HashMap<>();
    private final HashMap<UUID, PreparedService> preparedServices = new HashMap<>();

    private final List<ServicePrepareRequest> prepareServiceQueue = new ArrayList<>();
    private final List<ServiceStartRequest> startServiceQueue = new ArrayList<>();

    private boolean preparing = false;
    private boolean starting = false;

    public ServiceFactory(CloudSystem cloud) {
        this.cloud = cloud;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!preparing && prepareServiceQueue.size() > 0) {
                    ServicePrepareRequest request = prepareServiceQueue.get(0);
                    prepareServiceQueue.remove(request);
                    requestPreparedService(request);
                }
                if (!starting && startServiceQueue.size() > 0) {
                    ServiceStartRequest request = startServiceQueue.get(0);
                    startServiceQueue.remove(request);
                    startService(request.getGroup(), request.getUUID());
                }
            }
        }, 0, 1);
    }

    public void requestPreparedService(ServicePrepareRequest request) {
        if (!preparing) {
            preparing = true;
            prepareService(request.getGroup(), request.getType(), request.getServiceType());
        } else {
            prepareServiceQueue.add(request);
        }
    }

    public PreparedService prepareService(Group group, int type, int serviceType) {
        try {
            UUID serviceId = UUID.randomUUID();
            int serviceNumber = group.getTotalServiceCount() + 1;
            cloud.getLogger().i("Preparing Service... [" + MCVersion.fromServerType(type) + "/" + group.getName() + "-" + serviceNumber + " (" + serviceId + ")]");
            String servicePath = serviceType == 0 ? "services/temp/" + group.getName() + "-" + serviceId : "services/static/" + group.getName() + "-" + serviceNumber;
            Result result = createServiceFiles(group, serviceId, servicePath);
            if (result instanceof Fail) {
                cloud.getLogger().err(((Fail) result).getMessage());
                return null;
            }
            PreparedService preparedService = new PreparedService(cloud, group, serviceId, type, serviceType, serviceNumber);
            preparedServices.put(serviceId, preparedService);
            group.getPreparedServices().add(preparedService);
            preparing = false;
            return preparedService;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestService(ServiceStartRequest request) {
        if (!starting) {
            starting = true;
            startService(request.getGroup(), request.getUUID());
        } else {
            startServiceQueue.add(request);
        }

    }

    public void startService(Group group, UUID id) {
        PreparedService preparedService = preparedServices.get(id);
        JVM jvm = new JVM(cloud);
        int serviceType = preparedService.getServiceType();
        String servicePath = serviceType == 0 ? "services/temp/" + group.getName() + "-" + preparedService.getUUID() : "services/static/" + group.getName() + "-" + preparedService.getServiceNumber();
        jvm.start("", new FileOrPath(servicePath + "/server.jar"));
        starting = false;
    }

    public void downloadServerFile(int type, String version) {
        HttpFile file = new HttpFile(defaultServerFileDownloadLink + MCVersion.fromServerType(type) + "/" + MCVersion.fromServerType(type) + "-" + version + ".jar", new File("versions/" + MCVersion.fromServerType(type) + "-" + version + ".jar"));
        file.download();
    }

    public Result createServiceFiles(Group group, UUID serviceId, String servicePath) throws IOException {
        Files.copy(Paths.get(""), Paths.get(""), StandardCopyOption.REPLACE_EXISTING);
        cloud.createFolders(servicePath);
        createEula(servicePath);
        return new Success();
    }

    private void createEula(String path) {
        FileUtil.writeFile("eula=true", new FileOrPath(path + "/eula.txt"));
    }

}
