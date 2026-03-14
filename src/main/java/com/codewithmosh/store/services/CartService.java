package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.Cart.CartItemsDto;
import com.codewithmosh.store.dtos.Cart.CartsDto;
import com.codewithmosh.store.entities.Carts;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartsMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private CartsRepository cartsRepository;
    private CartsMapper cartsMapper;
    private ProductRepository productRepository;


    public CartsDto createCart(){
        var cart = new Carts();
        cartsRepository.save(cart);

        return cartsMapper.toDto(cart);
    }

    public CartsDto getCart(UUID cartId){
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null){
            return null;
        }
        return cartsMapper.toDto(cart);
    }

    public CartItemsDto addToCart(UUID cartId, Long productId, Integer quantity){
        var cart = cartsRepository.findById(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.addItem(product, quantity);
        cartsRepository.save(cart);

        return cartsMapper.toCartItemsDto(cartItem);

    }

    public CartItemsDto updateCart(UUID cartId, Long productId, Integer quantity){
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null) throw  new CartNotFoundException();

        var cartItem = cart.getItem(productId);
        if(cartItem == null) throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartsRepository.save(cart);

        return cartsMapper.toCartItemsDto(cartItem);
    }


    public void removeItem(UUID cartId, Long itemId){
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        var item = cart.getItem(itemId);
        if(item == null){
            throw new ProductNotFoundException();
        }
        cart.removeItem(itemId);
        cartsRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart = cartsRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        cart.clearCart();
        cartsRepository.save(cart);


    }



}
