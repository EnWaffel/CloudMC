package net.projectp.cloudmc.command;

import net.projectp.cloudmc.cloud.CloudSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CommandConsole extends Thread {

    private final CloudSystem cloudSystem;
    private final HashMap<String,CommandExecutor> registeredCommands = new HashMap<>();
    private boolean enabled;
    private final ConsoleOutput output;

    public CommandConsole(CloudSystem cloudSystem) {
        this.cloudSystem = cloudSystem;
        setName("CONSOLE-INPUT-THREAD");
        enabled = true;
        output = new ConsoleOutput() {
            @Override
            public void info(String text) {
                ConsoleOutput.super.info(text);
            }

            @Override
            public void warn(String text) {
                ConsoleOutput.super.warn(text);
            }

            @Override
            public void err(String text) {
                ConsoleOutput.super.err(text);
            }
        };
        start();
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                    Scanner scanner = new Scanner(System.in);
                    String in = scanner.nextLine();
                    if (enabled) {
                        if (!in.isEmpty()) {
                            List<Object> formattedCommand = formatCommand(in);
                            if (registeredCommands.containsKey((String)formattedCommand.get(0))) {
                             registeredCommands.get((String)formattedCommand.get(0)).onCommand((String[])formattedCommand.get(1),output);
                            } else {
                               cloudSystem.getLogger().err("Command \""+formattedCommand.get(0)+"\" not found! Try \"help\" or \"info\" for all commands!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //cloudSystem.getLogger().exception(this,e);
        }
    }

    private List<Object> formatCommand(String rawCommand) {
        List<Object> list = new ArrayList<>();
        String[] splitCmd = rawCommand.split(" ");
        String cmd = splitCmd[0];
        list.add(cmd);
        int cmdLength = cmd.length()+1;
        if (splitCmd.length > 1) { list.add(rawCommand.substring(cmdLength).split(" ")); } else { list.add(new String[]{}); }
        return list;
    }

    public void registerCommand(String name,CommandExecutor commandExecutor) {
        registeredCommands.put(name,commandExecutor);
    }

    public HashMap<String, CommandExecutor> getRegisteredCommands() {
        return registeredCommands;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CloudSystem getCloudSystem() {
        return cloudSystem;
    }

}