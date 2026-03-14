package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.Cart.AddItemToCartRquest;
import com.codewithmosh.store.dtos.Cart.CartItemsDto;
import com.codewithmosh.store.dtos.Cart.CartsDto;
import com.codewithmosh.store.dtos.Cart.UpdateCartItemRequest;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartsController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartsDto> createCart(UriComponentsBuilder uriBuilder){

        var cartDto = cartService.createCart();

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CartsDto> getCart(@PathVariable UUID id){

        var cart = cartService.getCart(id);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }

       return ResponseEntity.ok(cart);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemsDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRquest request) {

        var cartItemDto = cartService.addToCart(cartId, request.getProductId(), request.getQuantity());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }


    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable UUID cartId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ){

        var cartItemDto = cartService.updateCart(cartId, itemId, request.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> deleteItem(
            @PathVariable UUID cartId,
            @PathVariable Long itemId
    ){
        cartService.removeItem(cartId, itemId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Product not found")
        );
    }

}



//aa7f66a3-3220-44e3-832c-f0c6c72875a3