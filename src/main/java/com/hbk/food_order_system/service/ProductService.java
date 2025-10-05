package com.hbk.food_order_system.service;

import com.hbk.food_order_system.OrderApplication;
import com.hbk.food_order_system.entity.Product;
import com.hbk.food_order_system.factory.EntityFactory;
import com.hbk.food_order_system.builder.ProductBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final List<Product> products; 

    public ProductService() {
        this.products=new ArrayList();
        initializeProducts();
    }

    void initializeProducts() {
        final List<Object[]> mockProducts = List.of(
            new Object[]{"Double Cheese Burger", "Burger đôi với phô mai đặc biệt", 89000},
            new Object[]{"Classic Burger", "Burger truyền thống với rau tươi", 65000},
            new Object[]{"Pizza Hải Sản", "Pizza với topping hải sản cao cấp", 159000}
        );
        
        final OrderApplication application=OrderApplication.getInstance();
        final EntityFactory entityFactory=application.getEntityFactory();

        for (Object[] data : mockProducts) {
            final String name = (String) data[0];
            final String description = (String) data[1];
            final int price = (int) data[2];

            final ProductBuilder builder = entityFactory.newEntityBuilder(ProductBuilder.class);
            products.add(
                builder
                    .name(name)
                    .description(description)
                    .image(null)
                    .price(price)
                    .isAvailable(true)
                    .build()
            );
        }
    }

    public List<Product> getAllProducts() {
        return new ArrayList<> (products);
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts= new ArrayList<>();
        for(Product product : products) {
            if(product.getIsAvailable()) {
                availableProducts.add(product); 
            }
        }
        return availableProducts;
    }
}
