package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Product;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepositoryImpl implements ProductRepository {
    private final Map<String, Product> products;

    public ProductRepositoryImpl() {
        products = new ConcurrentHashMap<>();
    }

    @Override
    public void saveProduct(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findProductById(String id) {
        return Optional.ofNullable(products.get(id));
    }
}
