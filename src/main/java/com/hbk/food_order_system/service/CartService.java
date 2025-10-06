package com.hbk.food_order_system.service;

import com.hbk.food_order_system.entity.CartItem;
import com.hbk.food_order_system.entity.Product;
import java.util.*;

public class CartService {
    private final Map<String, CartItem> cart;
    private final List<CartUpdateListener> listeners;
    
    public CartService() {
        this.cart = new HashMap<>();
        this.listeners = new ArrayList<>();
    }
    
    public void addItem(Product product) {
        if (cart.containsKey(product.getName())) {
            CartItem item = cart.get(product.getName());
            item.setQuantity(item.getQuantity() + 1);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(1);
            item.setUnitPrice(product.getPrice());
            cart.put(product.getName(), item);
        }
        notifyListeners();
        notifyListeners();
    }
    
    public void removeItem(String productName) {
        cart.remove(productName);
        notifyListeners();
    }
    
    public void updateQuantity(String productName, int quantity) {
        if (cart.containsKey(productName)) {
            cart.get(productName).setQuantity(quantity);
            notifyListeners();
        }
    }
    
    public Collection<CartItem> getItems() {
        return cart.values();
    }
    
    public int getSubtotal() {
        return cart.values().stream()
            .mapToInt(CartItem::getTotalPrice)
            .sum();
    }
    
    public int getTotal() {
        return getSubtotal();
    }
    
    public void clear() {
        cart.clear();
        notifyListeners();
    }
    
    public void addListener(CartUpdateListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners() {
        for (CartUpdateListener listener : listeners) {
            listener.onCartUpdated();
        }
    }
    
    public interface CartUpdateListener {
        void onCartUpdated();
    }
}
