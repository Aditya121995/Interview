package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Cart;

import java.util.Optional;

public interface CartRepository {
    void saveCart(Cart cart);
    Optional<Cart> findCartByCustomerId(String id);
    void deleteCartByCustomerId(String id);
}
