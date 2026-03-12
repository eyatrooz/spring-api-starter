package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.Cart.CartItemsDto;
import com.codewithmosh.store.dtos.Cart.CartsDto;
import com.codewithmosh.store.entities.CartItems;
import com.codewithmosh.store.entities.Carts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartsMapper {
    CartsDto toDto(Carts carts);


    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))")
    CartItemsDto toCartItemsDto(CartItems cartItem);
}


//بكرا حاول احذف addToCart ارجع ابنيها من جديد