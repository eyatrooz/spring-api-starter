package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.Cart.AddItemToCartRquest;
import com.codewithmosh.store.dtos.Cart.CartItemsDto;
import com.codewithmosh.store.dtos.Cart.CartsDto;
import com.codewithmosh.store.entities.CartItems;
import com.codewithmosh.store.entities.Carts;
import com.codewithmosh.store.mappers.CartsMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartsController {

    private final CartsRepository cartsRepository;
    private final CartsMapper cartsMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartsDto> createCart(UriComponentsBuilder uriBuilder){
        var cart = new Carts();
        cartsRepository.save(cart);

        var cartDto = cartsMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CartsDto> getCart(@PathVariable UUID id){
        var cart = cartsRepository.findById(id).orElse(null);

        if(cart == null){
            return ResponseEntity.notFound().build();
        }

       return ResponseEntity.ok(cartsMapper.toDto(cart));

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemsDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRquest request) {

        var cart = cartsRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //check if the product already exist in the cart
        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItems();
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
        cartsRepository.save(cart);
        var cartItemDto = cartsMapper.toCartItemsDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

}
