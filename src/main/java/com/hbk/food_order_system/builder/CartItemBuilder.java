package com.hbk.food_order_system.builder;

import com.hbk.food_order_system.entity.CartItem;
import com.hbk.food_order_system.entity.Product;

public class CartItemBuilder implements Builder<CartItem> {
    private int id;
    private Product product;
    private int quantity;
    private int unitPrice;

    public CartItemBuilder id(int id) {
        this.id = id;
        return this;
    }

    public CartItemBuilder product(Product product) {
        this.product = product;
        return this;
    }

    public CartItemBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public CartItemBuilder unitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    @Override
    public CartItem build() {
        CartItem item = new CartItem();
        item.setId(id);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        return item;
    }
}
