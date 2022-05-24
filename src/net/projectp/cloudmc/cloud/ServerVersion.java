package net.projectp.cloudmc.cloud;

import java.util.HashMap;

public class ServerVersion {

    private final String name;
    private final String downloadIP;
    private final HashMap<String,MCVersion> versions = new HashMap<>();

    public ServerVersion(String name, String downloadIP) {
        this.name = name;
        this.downloadIP = downloadIP;
    }

    public void addMCVersion(MCVersion version) {
        versions.put(version.getPrefix(),version);
    }

    public String getName() {
        return name;
    }

    public String getDownloadIP() {
        return downloadIP;
    }

    public HashMap<String, MCVersion> getVersions() {
        return versions;
    }

}
