package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartsRepository extends JpaRepository<Carts, UUID> {
}
