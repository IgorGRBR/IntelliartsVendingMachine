package com.IVM;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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
        var new_categories = new HashMap<String, SnackCategory>();
        for (var category : categories.values()) {
            if (category.getAmount() > 0) {
                new_categories.put(category.getName(), category);
            }
        }
        categories = new_categories;
    }

    public void report(YearMonth ym) {
        Calendar calendar = new GregorianCalendar();
        //Filter records by year and a month, group them by category and sum them all by price
        HashMap<SnackCategory, List<Record>> ym_records = (HashMap<SnackCategory, List<Record>>) purchases.stream().filter(x -> {
            calendar.setTime(x.getDate());
            return calendar.get(Calendar.YEAR) == ym.getYear() && calendar.get(Calendar.MONTH) == ym.getMonthValue() - 1;
        }).collect(Collectors.groupingBy(x -> x.getCategory()));
        HashMap<String, Integer> sales = new HashMap<>();
        for (var key: ym_records.keySet()) {
            var list = ym_records.get(key);
            sales.put(key.getName(), list.size() * key.getPrice());
        }
        System.out.println("Sales report as of "+ym.toString()+":");
        for (var record : sales.keySet()) {
            System.out.println("\t"+record+": "+SnackCategory.getPriceStr(sales.get(record)));
        }
    }

    public void report(Date date) {
        Date now = Calendar.getInstance().getTime();
        //Filter records by year and a month, group them by category and sum them all by price
        HashMap<SnackCategory, List<Record>> records = (HashMap<SnackCategory, List<Record>>) purchases.stream().filter(x -> {
            var record_date = x.getDate();
            return now.compareTo(record_date) >= 0 && date.compareTo(record_date) <= 0;
        }).collect(Collectors.groupingBy(x -> x.getCategory()));
        HashMap<String, Integer> sales = new HashMap<>();
        for (var key: records.keySet()) {
            var list = records.get(key);
            sales.put(key.getName(), list.size() * key.getPrice());
        }
        System.out.println("Sales report from "+date.toString()+" to as of today:");
        for (var record : sales.keySet()) {
            System.out.println("\t"+record+": "+SnackCategory.getPriceStr(sales.get(record)));
        }
    }
}
