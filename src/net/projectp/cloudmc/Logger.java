package net.projectp.cloudmc;

import net.projectp.cloudmc.B;
import net.projectp.cloudmc.CloudSystem;
import net.projectp.cloudmc.util.Colors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger extends B {

    private final List<String> logs = new ArrayList<>();

    public Logger(CloudSystem cloud) {
        super(cloud);
    }

    public void i(String text) {
        String message = "[" + getCurrentTime() + "][CLOUD/INFO] " + text;
        System.out.println(message);
        logs.add(message);
    }

    public void w(String text) {
        System.out.println("["+getCurrentTime()+"][CLOUD/WARNING] "+Colors.YELLOW+text+Colors.RESET);
        logs.add("["+getCurrentTime()+"][CLOUD/WARNING] "+text);
    }

    public void e(String text) {
        System.out.println("["+getCurrentTime()+"][CLOUD/ERROR] "+Colors.RED+text+Colors.RESET);
        logs.add("["+getCurrentTime()+"][CLOUD/ERROR] "+text);
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void ex(Thread thread, Exception e) {
        System.out.println("["+getCurrentTime()+"][CLOUD/EXCEPTION]"+Colors.RED+" An unexpected exception occurred in thread: "+thread.getName()+" ("+e+": "+e.getMessage()+")"+Colors.RESET);
    }

    public void saveLog() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}