package com.hbk.food_order_system.factory.impl;

import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.factory.ProductFactory;
import com.hbk.food_order_system.builder.ProductBuilder;

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
            product.setIsAvailable(isAvailable);
            return product;
    }

    @Override
    public ProductBuilder newProductBuilder() {
        return new ProductBuilder();
    }
}
