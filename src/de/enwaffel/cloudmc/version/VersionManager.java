package de.enwaffel.cloudmc.version;

import de.enwaffel.cloudmc.B;
import de.enwaffel.cloudmc.CloudSystem;
import net.projectp.http.HttpFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class VersionManager extends B {

    private final HashMap<String, VersionProvider> versions = new HashMap<>();

    public VersionManager(CloudSystem cloud) {
        super(cloud);
    }

    public void loadVersion(JSONObject json) {
        String name = json.getString("name");
        VersionProvider provider = new VersionProvider(name, new ArrayList<>(), json.getString("download"));
        JSONArray versions = json.getJSONArray("versions");
        for (Object o : versions) {
            JSONObject obj = new JSONObject(o.toString());
            Version v = new Version(provider, obj.getString("name"), obj.getString("prefix"), obj.getDouble("min-java-version"), obj.getDouble("max-java-version"));
            provider.getVersions().add(v);
        }
        this.versions.put(name, provider);
    }

    public void downloadVersion(Version version) {
        try {
            if (version != null) {
                String filePath = version.getName().contains(".jar") ? "versions/" + version.getName() : "versions/" + version.getName() + ".jar";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                    cloud.getLogger().i("Downloading version file: '" + file.getName() + "'...");
                    HttpFile httpFile = new HttpFile(version.getProvider().getDownload().contains(".jar") ? version.getProvider().getDownload() : version.getProvider().getDownload() + file.getName(), file);
                    httpFile.download();
                    cloud.getLogger().i("Done!");
                }
            } else {
                cloud.getLogger().e("Version is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VersionProvider getByName(String name) {
        return versions.get(name);
    }

    public HashMap<String, VersionProvider> getVersions() {
        return versions;
    }

}
