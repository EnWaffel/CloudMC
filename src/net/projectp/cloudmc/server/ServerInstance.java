package net.projectp.cloudmc.server;

import net.projectp.cloudmc.group.Group;

public class ServerInstance {

    private final Group group;
    private final ServerId id;
    private final Process attachedProcess;

    public ServerInstance(Group group, ServerId id, Process attachedProcess) {
        this.group = group;
        this.id = id;
        this.attachedProcess = attachedProcess;
    }

    public Group getGroup() {
        return group;
    }

    public ServerId getId() {
        return id;
    }

    public Process getAttachedProcess() {
        return attachedProcess;
    }

}
