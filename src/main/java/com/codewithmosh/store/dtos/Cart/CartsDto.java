package com.codewithmosh.store.dtos.Cart;


import com.codewithmosh.store.entities.CartItems;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CartsDto {
    private UUID id;
    private Set<CartItemsDto> items;
}
