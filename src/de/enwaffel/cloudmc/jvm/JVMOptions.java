package de.enwaffel.cloudmc.jvm;

public class JVMOptions {

    private final int maxMemory;

    public JVMOptions(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

}
