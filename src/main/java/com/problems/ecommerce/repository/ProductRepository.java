package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    void saveProduct(Product product);
    Optional<Product> findProductById(String id);
}
