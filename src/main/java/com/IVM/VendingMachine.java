package com.IVM;

import java.util.ArrayList;
import java.util.HashMap;

public class VendingMachine
{
    private HashMap<String, SnackCategory> categories;

    public VendingMachine() {
        categories = new HashMap<>();
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

    public ArrayList<SnackCategory> list() {
        ArrayList<SnackCategory> snacks = new ArrayList<>(categories.values());
        return new ArrayList<>(); //TODO
    }

    public void clear() {
        //TODO
    }

    public void report(int year, int month){
        //TODO
    }

    public void report(int year, int month, int day) {
        //TODO
    }
}
