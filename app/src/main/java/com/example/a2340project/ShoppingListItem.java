package com.example.a2340project;

public class ShoppingListItem {
    private String name;
    private int quantity;

    public ShoppingListItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getName() {
        return this.name;
    }
    public int getQuantity() {
        return this.quantity;
    }
}
