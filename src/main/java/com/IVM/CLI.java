package com.IVM;

import java.util.*;
import java.util.function.Consumer;

public class CLI
{
    private String app_name;
    private Map<String, Command> commands;
    private boolean running;
    private Consumer<String[]> on_start;
    private Consumer<String[]> on_stop;

    public CLI(String name) {
        app_name = name;
        commands = new HashMap<>();
        running = false;
    }

    public void registerCommand(String name, IVMConsumer<String[]> lambda) {
        var command = new Command(lambda, 0);
        commands.putIfAbsent(name.toLowerCase(Locale.ROOT), command);
    }

    public void registerCommand(String name, IVMConsumer<String[]> lambda, int args) {
        var command = new Command(lambda, args);
        commands.putIfAbsent(name.toLowerCase(Locale.ROOT), command);
    }

    public void registerCommand(String name, IVMConsumer<String[]> lambda, int minargs, int maxargs) {
        var command = new Command(lambda, minargs, maxargs);
        commands.putIfAbsent(name.toLowerCase(Locale.ROOT), command);
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        var inp_scanner = new Scanner(System.in);
        while(running) {
            try {
                //Print prompt and get user input
                System.out.print(app_name+">");
                String input_string;
                try {
                    input_string = inp_scanner.nextLine();
                } catch (NoSuchElementException e) {
                    running = false;
                    break;
                }

                //Parse user input and find the matching command
                var split_input_string = parseArgs(input_string);
                var command = commands.get(split_input_string[0].toLowerCase(Locale.ROOT));
                var arguments = Arrays.copyOfRange(split_input_string, 1, split_input_string.length);
                if (command == null) {
                    throw new IVMException("Command not found");
                }

                //Execute the command
                command.invoke(arguments);
            } catch (IVMException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    private String[] parseArgs(String arg_string) throws IVMException {
        String[] split_string = arg_string.split("\\s");
        if (split_string.length == 0 || arg_string.length() == 0) {
            throw new IVMException("Command string is empty");
        }
        return split_string;
    }
}
