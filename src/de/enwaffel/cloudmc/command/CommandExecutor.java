package de.enwaffel.cloudmc.command;

public interface CommandExecutor {
    void onCommand(String[] args,ConsoleOutput output);
}