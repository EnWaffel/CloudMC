package de.enwaffel.cloudmc.util;

import de.enwaffel.cloudmc.CloudSystem;
import de.enwaffel.cloudmc.group.Group;
import de.enwaffel.cloudmc.service.Service;
import de.enwaffel.randomutils.file.FileOrPath;
import de.enwaffel.randomutils.file.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Util {

    public static String getOS() {
        return System.getProperty("os.name").split(" ")[0];
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String stringListToString(List<String> l) {
        StringBuilder result = new StringBuilder();
        for (String str1 : l) {
            result.append(str1);
        }
        return result.toString();
    }

    public static JSONObject readJsonFromUrl(String url) {
        try {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONObject(jsonText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFromUrl(String url) {
        try {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                return readAll(rd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray readJsonArrayFromUrl(String url) {
        try {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONArray(jsonText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void hideFile(File file) {
        try {
            Runtime.getRuntime().exec("attrib +H " + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(File file) {
        try {
            if (!file.exists()) {
                if (file.isDirectory())  {
                    file.mkdirs();
                } else {
                    file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(File file,String text,boolean hidden) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.flush();
            writer.close();
            if (hidden) {
                hideFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyIfNotExist(String from, String to) throws IOException {
        if (!new File(to).exists()) {
            Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void writeIfNotExist(String data, String to) {
        if (!new File(to).exists()) {
            FileUtil.writeFile(data, FileOrPath.path(to));
        }
    }

    public static Integer isNumber(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            return null;
        }
    }

    public static void createIfNotExist(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static Group groupExists(CloudSystem sys, String name) {
        for (Group group : sys.getGroups()) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    public static Service serviceIsActive(Group group, int num) {
        for (Service service : group.getActiveServices()) {
            if (service.getServiceNumber() == num) {
                return service;
            }
        }
        return null;
    }

}