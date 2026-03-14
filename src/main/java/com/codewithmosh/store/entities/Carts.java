package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItems> items = new HashSet<>();

    // method to get the total price of all items in the cart
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // method to get an item from the cart
    public CartItems getItem(Long itemId) {
        return items.stream()
                .filter(items -> items.getId().equals(itemId))
                .findFirst()
                .orElse(null);

    }

    // method to add an item to the cart
    public CartItems addItem(Product product, Integer quantity) {
        var cartItem = getItem(product.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItems();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }

    // method to remove an item from the cart
    public void removeItem(Long itemId) {
        var cartItem = getItem(itemId);
        if (cartItem != null) {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            } else {
                items.remove(cartItem);
                cartItem.setCart(null);

            }

        }
    }

    public void clearCart(){
        items.clear();
    }
}