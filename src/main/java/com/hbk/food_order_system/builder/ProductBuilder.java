package com.hbk.food_order_system.builder;

import com.hbk.food_order_system.entity.Product;

public class ProductBuilder implements Builder<Product> {
    private int id;
    private String name;
    private String description;
    private String image;
    private int price;
    private boolean isAvailable;
    
    public ProductBuilder id(int id) {
        this.id = id;
        return this;
    }
    
    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }
    
    public ProductBuilder image(String image) {
        this.image = image;
        return this;
    }
    
    public ProductBuilder price(int price) {
        this.price = price;
        return this;
    }
    
    public ProductBuilder isAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }
    
    @Override
    public Product build() {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setImage(image);
        product.setPrice(price);
        product.setIsAvailable(isAvailable);
        return product;
    }
} 
