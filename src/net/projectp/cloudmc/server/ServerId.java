package net.projectp.cloudmc.server;

import java.util.UUID;

public class ServerId {

    private final ServerInstance server;
    private final UUID id;

    public ServerId(ServerInstance server, UUID id) {
        this.server = server;
        this.id = id;
    }

    public ServerInstance getServer() {
        return server;
    }

    public UUID getId() {
        return id;
    }

}
