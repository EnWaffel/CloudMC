package de.enwaffel.cloudmc.group;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.service.PreparedService;
import de.enwaffel.cloudmc.service.Service;
import de.enwaffel.cloudmc.jvm.JVMOptions;
import de.enwaffel.cloudmc.version.Version;
import de.enwaffel.cloudmc.version.VersionProvider;
import net.projectp.network.channel.ServerChannel;
import net.projectp.network.packet.JSONPacket;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void sendPacketToService(UUID uuid, JSONObject data) {
        ServerChannel channel = cloud.getNetworkServer().getChannel("serviceCommunication");
        JSONPacket packet = new JSONPacket(channel.info(), new JSONObject().put("serviceId", uuid).put("sentData", data));
        channel.sendPacketGlobally(packet);
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

}
