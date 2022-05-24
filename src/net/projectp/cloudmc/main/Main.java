package net.projectp.cloudmc.main;

import net.projectp.cloudmc.cloud.CloudSystem;
import net.projectp.cloudmc.cloud.Properties;

public class Main {

    private static CloudSystem cloudSystem;

    public static void main(String[] args) {
        cloudSystem = new CloudSystem(new Properties("localhost"));
    }

    public static boolean isRunning() {
        return cloudSystem != null;
    }

    public static String getVersion() {
        return "0.0.1";
    }

}