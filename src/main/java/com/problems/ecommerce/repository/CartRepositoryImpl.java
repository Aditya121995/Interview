package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Cart;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CartRepositoryImpl implements CartRepository {
    private final Map<String, Cart> customerCart;

    public CartRepositoryImpl() {
        this.customerCart = new ConcurrentHashMap<>();
    }

    @Override
    public void saveCart(Cart cart) {
        customerCart.put(cart.getCustomerId(), cart);
    }

    @Override
    public Optional<Cart> findCartByCustomerId(String id) {
        return Optional.ofNullable(customerCart.get(id));
    }

    @Override
    public void deleteCartByCustomerId(String id) {
        customerCart.remove(id);
    }
}
