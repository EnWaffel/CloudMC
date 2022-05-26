package net.projectp.cloudmc.service;

import de.enwaffel.randomutils.buff.InByteBuffer;
import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.group.Group;

import java.util.UUID;

public class Service {

    private final CloudSystem cloudSystem;

    private final Group group;
    private final UUID id;
    private final int type;
    private final Process attachedProcess;
    private final Thread thread;

    public Service(Group group, UUID id, int type, Process attachedProcess, CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
        this.group = group;
        this.id = id;
        this.type = type;
        this.attachedProcess = attachedProcess;
        thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        try {
            while (attachedProcess.isAlive()) {
                InByteBuffer buff = new InByteBuffer(attachedProcess.getInputStream());
                buff.readFully();
                System.out.println(new String(buff.getBuffer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CloudSystem getCloudSystem() {
        return cloudSystem;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Process getAttachedProcess() {
        return attachedProcess;
    }

    public Thread getThread() {
        return thread;
    }

}
