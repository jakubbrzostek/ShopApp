package com.example.brzostek.common.model;

import java.io.Serializable;

public class Product implements Serializable, Identifiable {

    public static final String KEY = "PRODUCT_KEY";

    private String name;
    private int price, quantity;
    private boolean checked;

    public Product(String name, int price, int quantity, boolean checked) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.checked = checked;
    }

    public Product() {
    }

    @Override
    public String getId() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }
}
