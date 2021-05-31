package com.IVM;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLI
{
    public static final String DECIMAL = "^\\s*(\\d+\\.?\\d*)\\s*";
    public static final String STRING = "^\\s*(\"(.*?)\"|\\w+)\\s*";
    public static final String DATE = "^\\s*(\\d{4}-\\d{2}-\\d{2})\\s*";
    public static final String DATE_YM = "^\\s*((\\d{4}-\\d{2}-\\d{2})|(\\d{4}-\\d{2}))\\s*";

    public static final String DECIMAL_OPT = "^\\s*(\\d+\\.?\\d*)?\\s*$";
    public static final String STRING_OPT = "^\\s*\"(.*?)?\"\\s*|^\\s*(\\w+)?\\s*$";
    public static final String DATE_OPT = "^\\s*(\\d{4}-\\d{2}-\\d{2})?\\s*$";
    public static final String DATE_YM_OPT = "^\\s*((\\d{4}-\\d{2}-\\d{2})|(\\d{4}-\\d{2}))?\\s*$";

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
        var command = new Command(lambda);
        commands.putIfAbsent(name.toLowerCase(Locale.ROOT), command);
    }

    public void registerCommand(String name, IVMConsumer<String[]> lambda, String[] args) {
        var command = new Command(lambda, args);
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
                var parse_result = parseCommand(input_string);
                String command_name = parse_result[0], arg_string = parse_result[1];
                var command = commands.get(command_name.toLowerCase(Locale.ROOT));
                var arguments = parseArguments(arg_string, command.getArgTypes());
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

    private String[] parseCommand(String arg_string) throws IVMException {
        var cmd_pattern = Pattern.compile("^(\\w+)\\s*");
        var cmd_matcher = cmd_pattern.matcher(arg_string);
        String new_arg_string, command;
        if (cmd_matcher.find()) {
            command = cmd_matcher.group(1);
            new_arg_string = arg_string.substring(cmd_matcher.end());
        } else {
            throw new IVMException("Invalid command name/syntax");
        }
        return new String[] {command, new_arg_string};
    }

    private String[] parseArguments(String arg_string, String[] regexes) throws IVMException {
        var args = new String[regexes.length];
        for (int i = 0; i < regexes.length; i++) {
            var arg_pattern = Pattern.compile(regexes[i]);
            var arg_matcher = arg_pattern.matcher(arg_string);
            if (arg_matcher.find()){
                args[i] = getLastValidGroup(arg_matcher);
                arg_string = arg_string.substring(arg_matcher.end());
            } else {
                throw new IVMException("Invalid type of argument " + (i + 1));
            }
        }
        return args;
    }

    private String getLastValidGroup(Matcher matcher){
        String str = null;
        for (int i = matcher.groupCount(); i >= 0 ; i--) {
            str = matcher.group(i);
            if (str != null && !str.equals("")) {
                break;
            }
        }
        str = str.equals("") ? null : str;
        return str;
    }
}
