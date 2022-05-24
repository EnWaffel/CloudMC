package net.projectp.cloudmc.cloud;

import net.projectp.cloudmc.util.Colors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private final CloudSystem cs;
    private final List<String> logs = new ArrayList<>();

    public Logger(CloudSystem cs) {
        this.cs = cs;
    }

    public void info(String text,boolean prefix) {
        if (prefix) {
            System.out.println("[" + getCurrentTime() + "][CLOUD/INFO] " + text);
            logs.add("[" + getCurrentTime() + "][CLOUD/INFO] " + text);
        } else {
            System.out.println(text);
        }
    }

    public void warn(String text) {
        System.out.println("["+getCurrentTime()+"][CLOUD/WARNING] "+Colors.YELLOW+text+Colors.RESET);
        logs.add("["+getCurrentTime()+"][CLOUD/WARNING] "+text);
    }

    public void err(String text) {
        System.out.println("["+getCurrentTime()+"][CLOUD/ERROR] "+Colors.RED+text+Colors.RESET);
        logs.add("["+getCurrentTime()+"][CLOUD/ERROR] "+text);
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void exception(Thread thread,Exception e) {
        System.out.println("["+getCurrentTime()+"][CLOUD/EXCEPTION]"+Colors.RED+" An unexpected exception occurred in thread: "+thread.getName()+" ("+e+": "+e.getMessage()+")"+Colors.RESET);
    }

    public void saveLog() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CloudSystem getCloudSystem() {
        return cs;
    }

}