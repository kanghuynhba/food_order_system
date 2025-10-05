package com.hbk.food_order_system;

import com.hbk.food_order_system.factory.EntityFactory;
import com.hbk.food_order_system.factory.impl.EntityFactoryImpl;

import lombok.Getter;

public final class OrderApplication {
    
    @Getter
    private final EntityFactory entityFactory;
    
    private static volatile OrderApplication instance;
    
    private OrderApplication() {
        this.entityFactory = new EntityFactoryImpl();
    }
    
    public static OrderApplication getInstance() {
        if (instance == null) {
            synchronized (OrderApplication.class) {
                if (instance == null) {
                    instance = new OrderApplication();
                }
            }
        }
        return instance;
    }
}  
