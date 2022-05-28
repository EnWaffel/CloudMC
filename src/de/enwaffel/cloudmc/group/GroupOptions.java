package de.enwaffel.cloudmc.group;

import de.enwaffel.cloudmc.version.Version;

public class GroupOptions {

    private final int serverType;
    private final Version version;

    public GroupOptions(int serverType, Version version) {
        this.serverType = serverType;
        this.version = version;
    }

    public int getServerType() {
        return serverType;
    }

    public Version getVersion() {
        return version;
    }

}
