package net.projectp.cloudmc.group;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.service.Service;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class Group {

    private final String name;
    private final JSONObject json;

    private int maxRam;
    private int minServers;

    private final HashMap<UUID, Service> servers = new HashMap<>();

    public Group(String name, FileOrPath file) {
        this.name = name;
        this.json = FileUtil.readJSON(file);
    }

    public Group setOpts(int maxRam, int minServers) {
        this.maxRam = maxRam;
        this.minServers = minServers;
        return this;
    }

    public void addServer(UUID id, Service server) {
        servers.put(id, server);
    }

    public String getName() {
        return name;
    }

}
