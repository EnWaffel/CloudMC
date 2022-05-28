package de.enwaffel.cloudmc.util;

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
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray readJsonArrayFromUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONArray json = new JSONArray(jsonText);
                return json;
            } finally {
                is.close();
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
            FileUtil.writeFile(data, new FileOrPath(to));
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

}