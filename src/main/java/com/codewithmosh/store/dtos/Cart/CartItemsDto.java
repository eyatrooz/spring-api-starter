package com.codewithmosh.store.dtos.Cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemsDto {
    private Long id;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;
    private Integer quantity;

}
