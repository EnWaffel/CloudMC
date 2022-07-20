package de.enwaffel.cloudmc.main;

import de.enwaffel.cloudmc.CloudSystem;

public class Main {

    private static CloudSystem cloudSystem;

    public static void main(String[] args) {
        cloudSystem = new CloudSystem();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> cloudSystem.shutdown()));
    }

    public static boolean isRunning() {
        return cloudSystem != null;
    }

    public static String getVersion() {
        return "0.0.1";
    }

}