package net.projectp.cloudmc.cloud;

public class MCVersion {

    private final String prefix;
    private final String name;

    public MCVersion(String prefix, String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }




}
