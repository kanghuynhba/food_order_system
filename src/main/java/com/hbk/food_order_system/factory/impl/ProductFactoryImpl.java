package com.hbk.food_order_system.factory.impl;

import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.factory.ProductFactory;

public class ProductFactoryImpl implements ProductFactory {
    @Override
    public Product newProduct(
            String name, 
            String description, 
            String image, 
            int price, 
            boolean isAvailable) {
                    Product product=new Product();
                    product.setName(name);
                    product.setDescription(description);
                    product.setImage(image);
                    product.setPrice(price);
                    product.setIsAvailabe(isAvailable);
                    return product;
        }
}
