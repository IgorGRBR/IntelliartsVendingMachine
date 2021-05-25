package com.IVM;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class App
{
    private Map<String, Consumer<String[]>> commands;

    public App()
    {
        commands = new HashMap<>();
    }

    public void registerCommand(String name, Consumer<String[]> lambda)
    {
        commands.putIfAbsent(name, lambda);
    }

    public void run() {
        var running = true;
        var inp_scanner = new Scanner(System.in);
        while(running)
        {
            try {
                //Print prompt and get user input
                System.out.print("vending machine>");
                var input_string = inp_scanner.nextLine();
                //Parse user input and find the matching command
                var split_input_string = parseArgs(input_string);
                var command = commands.get(split_input_string[0]);
                var arguments = Arrays.copyOfRange(split_input_string, 1, split_input_string.length);
                if (command == null)
                {
                    throw new Exception("Command not found");
                }
                //Execute the command
            } catch (NoSuchElementException e) {
                running = false;
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            };
        }
    }

    private String[] parseArgs(String arg_string) throws Exception
    {
        String[] split_string = arg_string.split("\\s");
        if (split_string.length == 0)
        {
            throw new Exception("Command string is empty");
        }
        return split_string;
    }
}
