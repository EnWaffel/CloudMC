package net.projectp.cloudmc.group;

import de.enwaffel.randomutils.file.FileOrPath;
import net.projectp.cloudmc.jvm.JVMOptions;
import net.projectp.cloudmc.service.PreparedService;
import net.projectp.cloudmc.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Group {
    //https://download.getbukkit.org/spigot/spigot-1.18.2.jar
    private final String name;
    private final List<Service> activeServices = new ArrayList<>();
    private final List<PreparedService> preparedServices = new ArrayList<>();

    private GroupOptions groupOptions;
    private JVMOptions jvmOptions;

    private final HashMap<UUID, Service> servers = new HashMap<>();

    public Group(String name, FileOrPath file) {
        this.name = name;
        //this.json = FileUtil.readJSON(file);
    }

    public Group setOptions(GroupOptions groupOptions, JVMOptions jvmOptions) {
        this.groupOptions = groupOptions;
        this.jvmOptions = jvmOptions;
        return this;
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

}
