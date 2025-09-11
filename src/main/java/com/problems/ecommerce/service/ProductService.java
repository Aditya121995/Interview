package com.problems.ecommerce.service;

import com.problems.ecommerce.entity.Product;
import com.problems.ecommerce.repository.ProductRepository;
import com.problems.ecommerce.repository.ProductRepositoryImpl;

import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;
    public ProductService() {
        this.productRepository = new ProductRepositoryImpl();
    }

    public String addProduct(String name, double price, int quantity) {
        Product product = new Product(name, price, quantity);
        productRepository.saveProduct(product);
        System.out.println("Added product with name " + name + " and price " + price);
        return product.getId();
    }

    public boolean updateInventory(String productId, int quantity) {
        Optional<Product> productOpt = productRepository.findProductById(productId);
        if (productOpt.isEmpty()) {
            System.out.println("Product does not exist with id :: " + productId);
            return false;
        }

        Product product = productOpt.get();
        product.reserveInventory(quantity);
        return true;
    }

    public int checkProductInventoryCount(String productId) {
        Optional<Product> productOpt = productRepository.findProductById(productId);
        if (productOpt.isEmpty()) {
            System.out.println("Product does not exist with id :: " + productId);
            return 0;
        }

        Product product = productOpt.get();
        return product.getInventory();
    }

    public Product getProduct(String productId) {
        Optional<Product> productOpt = productRepository.findProductById(productId);
        return productOpt.orElse(null);
    }
}
