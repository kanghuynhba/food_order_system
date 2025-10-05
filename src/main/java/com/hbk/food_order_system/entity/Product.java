package com.hbk.food_order_system.entity;

import lombok.Data;

@Data
public class Product {
    int id;
    String name, description, image;
    int price;
    boolean isAvailable;

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable=isAvailable;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }
}
