package net.projectp.cloudmc.group;

public class GroupOptions {

    private final int serverType;
    private final String versionName;

    public GroupOptions(int serverType, String versionName) {
        this.serverType = serverType;
        this.versionName = versionName;
    }

    public int getServerType() {
        return serverType;
    }

    public String getVersionName() {
        return versionName;
    }

}
