package de.enwaffel.cloudmc.util;

public class MCVersion {

    public static String fromServerType(int type) {
        return type == 0 ? "bukkit" : "spigot";
    }

}
