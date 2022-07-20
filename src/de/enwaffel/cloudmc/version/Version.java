package de.enwaffel.cloudmc.version;

public class Version {

    private final VersionProvider provider;
    private final String name;
    private final String prefix;
    private final double minJavaVersion;
    private final double maxJavaVersion;

    public Version(VersionProvider provider, String name, String prefix, double minJavaVersion, double maxJavaVersion) {
        this.provider = provider;
        this.name = name;
        this.prefix = prefix;
        this.minJavaVersion = minJavaVersion;
        this.maxJavaVersion = maxJavaVersion;
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

    public double getMinJavaVersion() {
        return minJavaVersion;
    }

    public double getMaxJavaVersion() {
        return maxJavaVersion;
    }

}
