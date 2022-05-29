package de.enwaffel.cloudmc.service;

import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.jvm.JVM;
import net.projectp.network.server.ClientConnection;

import java.util.UUID;

public class Service extends PreparedService {

    private final JVM jvm;
    private final int serverPort;
    private boolean connected;
    private ClientConnection networkClient;

    public Service(CloudSystem cloud, Group group, UUID uuid, int serviceNumber, String workingFolder, JVM jvm, int serverPort) {
        super(cloud, group, uuid, serviceNumber, workingFolder);
        this.jvm = jvm;
        this.serverPort = serverPort;
    }

    public JVM getJVM() {
        return jvm;
    }

    public int getServerPort() {
        return serverPort;
    }

    public boolean isConnected() {
        return connected;
    }

    public ClientConnection getNetworkClient() {
        return networkClient;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setNetworkClient(ClientConnection networkClient) {
        this.networkClient = networkClient;
    }

}
