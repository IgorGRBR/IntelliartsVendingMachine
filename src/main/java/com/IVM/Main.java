package com.IVM;

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
            if (cargs.length > 2) {
                amount = Integer.parseInt(cargs[2]);
            }
            vmachine.addCategory(name, price, amount);
        }, 2, 3);
        cli.registerCommand("addItem", cargs -> {
            String name = cargs[0];
            int amount = Integer.parseInt(cargs[1]);
            vmachine.addItem(name, amount);
        }, 2);
        cli.registerCommand("list", cargs -> {
            var snacks = vmachine.list();
            System.out.println("[ Category\t | Amount\t | Price ]");
            for (var snack : snacks) {
                System.out.println("  " + snack.getName() + "\t\t | " + snack.getAmount() + "\t\t | " + snack.getPriceStr());
            }
        });
        cli.registerCommand("clear", cargs -> vmachine.clear());
        cli.run();
    }
}
