package de.enwaffel.cloudmc.group;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.jvm.JVMOptions;
import de.enwaffel.cloudmc.service.PreparedService;
import de.enwaffel.cloudmc.service.Service;
import de.enwaffel.cloudmc.version.Version;
import de.enwaffel.cloudmc.version.VersionProvider;
import net.projectp.network.channel.ServerChannel;
import net.projectp.network.packet.JSONPacket;
import org.apache.commons.io.FileDeleteStrategy;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Group extends B {

    private final String name;
    private final List<Service> activeServices = new ArrayList<>();
    private final List<PreparedService> preparedServices = new ArrayList<>();

    private GroupOptions groupOptions;
    private JVMOptions jvmOptions;

    public Group(CloudSystem cloud, String name) {
        super(cloud);
        this.name = name;
    }

    public Group setOptions(GroupOptions groupOptions, JVMOptions jvmOptions) {
        this.groupOptions = groupOptions;
        this.jvmOptions = jvmOptions;
        return this;
    }

    public void sendPacketToService(Service service, String action, JSONObject data) {
        ServerChannel channel = cloud.getNetworkServer().getDefaultChannel();
        JSONPacket packet = new JSONPacket(channel.info(), new JSONObject().put("serviceId", service.getUUID()).put("action", action).put("d", data));
        channel.sendPacketGlobally(packet);
    }

    public void sendTextToService(Service service, String text) {
        sendPacketToService(service, "sendCommand", new JSONObject().put("text", text));
    }

    public void shutdownService(Service service) {
        sendPacketToService(service, "shutdown", new JSONObject());
    }

    public PreparedService getPreparedServiceByName(String name) {
        for (PreparedService preparedService : preparedServices) {
            if (preparedService.name().equals(name)) {
                return preparedService;
            }
        }
        return null;
    }

    public void stopService(Service service) {
        activeServices.remove(service);
        cloud.getNetworkServer().getClients().remove(service.getNetworkClient());
        service.getJVM().stop(false);
        if (groupOptions.getServerType() == 0) {
            try {
                FileDeleteStrategy.FORCE.delete(new File(service.getWorkingFolder()));
                cloud.getLogger().i("Service Deleted [" + groupOptions.getVersion().getProvider().getName() + "/" + service.name() + " (" + service.getUUID() + ")]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<Service> getActiveServices() {
        return activeServices;
    }

    public List<PreparedService> getPreparedServices() {
        return preparedServices;
    }

    public int getTotalServiceCount() {
        return activeServices.size() + preparedServices.size();
    }

    public GroupOptions getGroupOptions() {
        return groupOptions;
    }

    public JVMOptions getJVMOptions() {
        return jvmOptions;
    }

    public String getTemplateFolder() {
        return "templates/" + name;
    }

    public static Group fromJson(CloudSystem cloud, JSONObject json) {
        Group group = new Group(cloud, json.getString("name"));
        JSONObject _jvmOptions = json.getJSONObject("jvm");

        VersionProvider versionProvider = cloud.getVersionManager().getByName(json.getString("versionType"));
        Version version = versionProvider.getByPrefix(json.getString("version"));
        cloud.getVersionManager().downloadVersion(version);

        GroupOptions groupOptions = new GroupOptions(
                json.getInt("serverType"),
                version
        );

        JVMOptions jvmOptions = new JVMOptions(
                _jvmOptions.getInt("maxMemory")
        );

        group.setOptions(groupOptions, jvmOptions);
        return group;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", activeServices=" + activeServices +
                ", preparedServices=" + preparedServices +
                ", groupOptions=" + groupOptions +
                ", jvmOptions=" + jvmOptions +
                ", cloud=" + cloud +
                '}';
    }

}
