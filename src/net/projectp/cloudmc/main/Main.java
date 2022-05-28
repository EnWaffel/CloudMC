package net.projectp.cloudmc.main;

import net.projectp.cloudmc.CloudSystem;

import java.io.IOException;

public class Main {
    private static CloudSystem cloudSystem;
    public static void main(String[] args) throws IOException {
        cloudSystem = new CloudSystem();
    }

    public static boolean isRunning() {
        return cloudSystem != null;
    }

    public static String getVersion() {
        return "0.0.1";
    }

}