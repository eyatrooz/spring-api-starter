package com.codewithmosh.store.dtos.Cart;

import lombok.Data;

@Data
public class AddItemToCartRquest {
    private Long productId;
    private Integer quantity;
}
