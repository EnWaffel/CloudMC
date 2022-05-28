package de.enwaffel.cloudmc.version;

public class Version {

    private final VersionProvider provider;
    private final String name;
    private final String prefix;

    public Version(VersionProvider provider, String name, String prefix) {
        this.provider = provider;
        this.name = name;
        this.prefix = prefix;
    }

    public VersionProvider getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

}
