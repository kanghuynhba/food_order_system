package com.hbk.food_order_system.entity;

import lombok.Data;

@Data
public class CartItem {
    private int id;
    private Product product;
    private int quantity;
    private int unitPrice;

    public void incrementQuantity() {
        this.quantity++;
    }
    
    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }   
    public int getTotalPrice() {
        return unitPrice * quantity;
    }
}
