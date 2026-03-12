package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
