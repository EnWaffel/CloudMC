package de.enwaffel.cloudmc.version;

import java.util.List;

public class VersionProvider {

    private final String name;
    private final List<Version> versions;
    private final String download;

    public VersionProvider(String name, List<Version> versions, String download) {
        this.name = name;
        this.versions = versions;
        this.download = download;
    }

    public Version getByName(String name) {
        for (Version version : versions) {
            if (version.getName().equals(name)) {
                return version;
            }
        }
        return null;
    }

    public Version getByPrefix(String prefix) {
        for (Version version : versions) {
            if (version.getPrefix().equals(prefix)) {
                return version;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public String getDownload() {
        return download;
    }

}
