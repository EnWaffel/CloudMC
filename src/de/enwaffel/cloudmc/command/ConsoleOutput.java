package de.enwaffel.cloudmc.command;

import de.enwaffel.cloudmc.util.Colors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface ConsoleOutput {
    default void info(String text) { System.out.println("["+getCurrentTime() + "][CLOUD/COMMAND:INFO] "+text); }
    default void warn(String text) { System.out.println("["+getCurrentTime()+"][CLOUD/COMMAND:WARNING] "+ Colors.YELLOW+text+Colors.RESET); }
    default void err(String text) { System.out.println("["+getCurrentTime()+"][CLOUD/COMMAND:ERROR] "+Colors.RED+text+Colors.RESET); }
    default String getCurrentTime() { DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); LocalDateTime now = LocalDateTime.now(); return dtf.format(now); }
}