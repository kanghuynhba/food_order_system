package com.hbk.food_order_system.factory.impl;

import com.hbk.food_order_system.entity.Product;

import com.hbk.food_order_system.factory.ProductFactory;
import com.hbk.food_order_system.factory.EntityFactory;

import com.hbk.food_order_system.factory.impl.ProductFactoryImpl;

import com.hbk.food_order_system.builder.Builder;
import com.hbk.food_order_system.builder.ProductBuilder;

public class EntityFactoryImpl implements EntityFactory {
    private final ProductFactory productFactory=new ProductFactoryImpl();

    @SuppressWarnings("unchecked")
    @Override
    public <E> E newEntity(Class<E> entityType, String name) {
        if(entityType==Product.class) {
            return (E) productFactory.newProduct(name, "", "", 0, true);
        } 
        throw new IllegalArgumentException(
            "there is no factory type for: " + entityType
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, B extends Builder<E>> B newEntityBuilder(Class<B> builderType) {
        if(builderType==ProductBuilder.class) {
            return (B) productFactory.newProductBuilder();
        }        
        throw new IllegalArgumentException(
            "there is no builder for: " + builderType 
        );
    }
}
