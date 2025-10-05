package com.hbk.food_order_system.factory;

import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.builder.ProductBuilder;

public interface ProductFactory {
   Product newProduct(
            String name, 
            String description, 
            String image, 
            int price, 
            boolean isAvailable);
   ProductBuilder newProductBuilder();
}
