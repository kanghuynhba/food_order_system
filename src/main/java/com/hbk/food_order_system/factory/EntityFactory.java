package com.hbk.food_order_system.factory;

import com.hbk.food_order_system.builder.Builder;

public interface EntityFactory {
   <E> E newEntity(Class<E> entityType, String name);
   <E, B extends Builder<E>> B newEntityBuilder(Class<B> builderType);
}
