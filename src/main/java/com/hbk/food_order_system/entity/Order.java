package com.hbk.food_order_system.entity;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class Order {
    private int id;
    private String orderCode;
    private String customerName;
    private String orderType;
    private int discount;
    private int total;
    private List<CartItem> items;
    private int orderStatus;
    private String orderTime;
    private String note;
    
    public Order() {
        this.items = new ArrayList<>();
    }
}
