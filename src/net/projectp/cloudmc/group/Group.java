package net.projectp.cloudmc.group;

import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import net.projectp.cloudmc.server.ServerId;
import net.projectp.cloudmc.server.ServerInstance;
import org.json.JSONObject;

import java.util.HashMap;

public class Group {

    private final String name;
    private final JSONObject json;

    private final HashMap<ServerId, ServerInstance> servers = new HashMap<>();

    public Group(String name, FileOrPath file) {
        this.name = name;
        this.json = FileUtil.readJSON(file);
    }

}
