package com.hbk.food_order_system.entity;

import lombok.Data;

@Data
public class Product {
    int id;
    String name, description, image;
    int price;
    boolean isAvailable;
}
