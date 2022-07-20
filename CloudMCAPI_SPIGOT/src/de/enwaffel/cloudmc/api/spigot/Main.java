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

import java.util.Timer;
import java.util.TimerTask;

public class Main extends JavaPlugin {

    private String serviceId;
    private int serverType;
    private Client client;
    private boolean connected = false;

    @Override
    public void onEnable() {
        super.onEnable();
        JSONObject object = Util.readFileToJSON(".service");
        serviceId = object.getString("serviceId");
        serverType = object.getInt("type");
        client = new Client("localhost", 49152);
        client.connect();
        client.getDefaultChannel().addHandle(new CIL() {
            @Override
            public void handle(ChannelInfo channelInfo, Packet<?> packet, String s) {
                JSONPacket packet1 = (JSONPacket) packet;
                JSONObject data = packet1.getData();
                if (data.getString("serviceId").equals(serviceId)) {
                    switch (data.getString("action")) {
                        case "connected": {
                            connected = true;
                        }
                        case "shutdown": {
                            Bukkit.shutdown();
                            break;
                        }
                        case "sendCommand": {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), data.getJSONObject("d").getString("text"));
                            break;
                        }
                    }
                }
            }

            @Override
            public void handleRaw(byte[] bytes, String s) {

            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!connected) {
                    client.getDefaultChannel().sendPacket(new JSONPacket(client.getDefaultChannel().info(), new JSONObject().put("type", "serviceCommunication").put("serviceId", serviceId).put("action", "started")));
                } else {
                    cancel();
                }
            }
        }, 0, 3000);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> client.getDefaultChannel().sendPacket(new JSONPacket(client.getDefaultChannel().info(), new JSONObject().put("type", "serviceCommunication").put("serviceId", serviceId).put("action", "stopped")))));
    }

    public static void main(String[] args) {
        System.exit(0);
    }
}
