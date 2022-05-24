package net.projectp.cloudmc.cloud;

import java.util.HashMap;

public class VersionManager {

    private static final HashMap<String,ServerVersion> serverVersions = new HashMap<>();

    public static void addServerVersion(ServerVersion serverVersion) {
        serverVersions.put(serverVersion.getName(),serverVersion);
    }

    public static HashMap<String, ServerVersion> getServerVersions() {
        return serverVersions;
    }

}
