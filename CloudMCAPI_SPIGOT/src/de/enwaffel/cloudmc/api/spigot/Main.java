package de.enwaffel.cloudmc.api.spigot;

import net.projectp.network.channel.CIL;
import net.projectp.network.channel.ChannelInfo;
import net.projectp.network.client.Client;
import net.projectp.network.packet.JSONPacket;
import net.projectp.network.packet.Packet;
import net.projectp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class Main extends JavaPlugin {

    private String serviceId;
    private Client client;

    @Override
    public void onEnable() {
        super.onEnable();
        serviceId = Util.readFileToJSON(".service").getString("serviceId");
        client = new Client("localhost", 49152);
        client.connect();
        client.createChannel("serviceCommunication");
        client.getChannel("serviceCommunication").addHandle(new CIL() {
            @Override
            public void handle(ChannelInfo channelInfo, Packet<?> packet, String s) {
                JSONPacket packet1 = (JSONPacket) packet;
                JSONObject data = packet1.getData();
                if (data.getString("serviceId").equals(serviceId)) {
                    switch (data.getString("action")) {
                        case "shutdown": {
                            Bukkit.shutdown();
                            break;
                        }
                        case "sendCommand": {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), data.getString("d"));
                            break;
                        }
                    }
                }
            }

            @Override
            public void handleRaw(byte[] bytes, String s) {

            }
        });
    }

    public static void main(String[] args) {
        System.exit(0);
    }
}
