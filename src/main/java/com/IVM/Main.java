package com.IVM;

import java.text.ParseException;
import java.util.Date;

public class Main
{
    public static void main(String[] args)
    {
        var vmachine = new VendingMachine();
        var cli = new CLI("vending machine");
        cli.registerCommand("help", cargs -> {
            System.out.println("Commands:");
            System.out.println("help - prints this message.");
            System.out.println("exit - stops the program.");
            System.out.println("addCategory <name> <price> [amount of items] - register a snack category in the system.");
            System.out.println("addItem <name> <amount> - register provided amount of snack items to sell.");
            System.out.println("purchase <name> <date> - purchase a single snack item.");
            System.out.println("list - show list of served categories with amount of items available for sale sorted by amount.");
            System.out.println("clear - stop serving all snack categories that don’t have items for sale.");
            System.out.println("report <YYYY-MM> - show earnings by category in specified month.");
            System.out.println("report <YYYY-MM-DD> - show earnings by category gained since provided date till now sorted by category name.");
        });
        cli.registerCommand("exit", cargs -> cli.stop());
        cli.registerCommand("addCategory", cargs -> {
            String name = cargs[0];
            int price, amount = 0;
            price = (int)(Float.parseFloat(cargs[1]) * 100);
            if (cargs[2] != null) {
                amount = Integer.parseInt(cargs[2]);
            }
            vmachine.addCategory(name, price, amount);
        }, new String[] {CLI.STRING, CLI.DECIMAL, CLI.DECIMAL_OPT});
        cli.registerCommand("addItem", cargs -> {
            String name = cargs[0];
            int amount = Integer.parseInt(cargs[1]);
            vmachine.addItem(name, amount);
        }, new String[] {CLI.STRING, CLI.DECIMAL});
        cli.registerCommand("list", cargs -> {
            var snacks = vmachine.list();
            System.out.println("[ Category\t | Amount\t | Price ]");
            for (var snack : snacks) {
                System.out.println("  " + snack.getName() + "\t\t | " + snack.getAmount() + "\t\t | " + snack.getPriceStr());
            }
        });
        cli.registerCommand("purchase", cargs -> {
            try {
                var date = Record.date_format.parse(cargs[1]);
                vmachine.purchase(cargs[0], date);
            } catch (ParseException e) {
                throw new IVMException("Date parsing exception. Perhaps incorrect month/day numbers?");
            }
        }, new String[] {CLI.STRING, CLI.DATE});
        cli.registerCommand("report", cargs -> {
            Date date = null;
            try {
                date = Record.date_format.parse(cargs[0]);
            } catch (ParseException e) {
                try {
                    date = Record.date_format_ym.parse(cargs[0]);
                } catch (ParseException parseException) {
                    throw new IVMException("Date parsing exception. Perhaps incorrect month/day numbers?");
                }
            }

        }, new String[] {CLI.DATE_YM});
        cli.registerCommand("clear", cargs -> vmachine.clear());
        cli.run();
    }
}
