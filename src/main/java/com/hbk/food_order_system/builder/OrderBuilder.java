package com.hbk.food_order_system.builder;

import com.hbk.food_order_system.entity.Order;
import com.hbk.food_order_system.entity.CartItem;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder implements Builder<Order> {
    private int id;
    private String orderCode;
    private String customerName;
    private String orderType;
    private int discount;
    private int total;
    private List<CartItem> items = new ArrayList<>();
    private int orderStatus;
    private String orderTime;
    private String note;

    public OrderBuilder id(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderCode(String orderCode) {
        this.orderCode = orderCode;
        return this;
    }

    public OrderBuilder customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public OrderBuilder orderType(String orderType) {
        this.orderType = orderType;
        return this;
    }

    public OrderBuilder discount(int discount) {
        this.discount = discount;
        return this;
    }

    public OrderBuilder total(int total) {
        this.total = total;
        return this;
    }

    public OrderBuilder items(List<CartItem> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public OrderBuilder addItem(CartItem item) {
        this.items.add(item);
        return this;
    }

    public OrderBuilder orderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder orderTime(String orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public OrderBuilder note(String note) {
        this.note = note;
        return this;
    }

    @Override
    public Order build() {
        Order order = new Order();
        order.setId(id);
        order.setOrderCode(orderCode);
        order.setCustomerName(customerName);
        order.setOrderType(orderType);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setItems(items);
        order.setOrderStatus(orderStatus);
        order.setOrderTime(orderTime);
        order.setNote(note);
        return order;
    }
}
