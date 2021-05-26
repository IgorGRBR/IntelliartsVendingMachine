package com.IVM;

public class SnackCategory {
    private String name;
    private int price;
    private int amount;

    public SnackCategory(String _name, int _price) {
        name = _name;
        price = _price;
        amount = 0;
    }
    public SnackCategory(String _name, int _price, int _amount) {
        name = _name;
        price = _price;
        amount = _amount;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getPriceStr() { return String.valueOf((float)price/100.0f); }

    public void addItem(Integer amt) { amount += amt; }
    public int getAmount() { return amount; }

    public boolean purchase() {
        if (amount > 0) {
            amount--;
            return true;
        } else {
            return false;
        }
    }
}
