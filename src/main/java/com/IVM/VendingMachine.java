package com.IVM;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VendingMachine
{
    private HashMap<String, SnackCategory> categories;
    private ArrayList<Record> purchases;

    public VendingMachine() {
        categories = new HashMap<>();
        purchases = new ArrayList<>();
    }

    public void addCategory(String name, int price, int amount) {
        categories.putIfAbsent(name, new SnackCategory(name, price, amount));
    }

    public void addItem(String name, int amount) throws IVMException {
        var snack = categories.get(name);
        if (snack == null) {
            throw new IVMException("Snack category '" + name + "' was not found");
        }
        snack.addItem(amount);
    }

    public void purchase(String name, Date date) throws IVMException {
        var snack = categories.get(name);
        if (snack == null) {
            throw new IVMException("Snack category '" + name + "' was not found");
        }
        if (snack.purchase()) {
            purchases.add(new Record(date, snack));
            return;
        } else {
            throw new IVMException("Snack category '" + name + "' is empty");
        }
    }

    public ArrayList<SnackCategory> list() {
        ArrayList<SnackCategory> snacks = new ArrayList<>(categories.values());
        snacks.sort((a, b) -> a.getAmount() - b.getAmount());
        return snacks;
    }

    public void clear() {
        //TODO
    }

    public void report(int year, int month) {
        //TODO
    }

    public void report(int year, int month, int day) {
        //TODO
    }
}
