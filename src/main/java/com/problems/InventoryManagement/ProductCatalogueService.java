package com.problems.InventoryManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductCatalogueService {
    private final Map<String, Product> products;
    private final Map<ProductCategory, List<Product>> productsByCategory;

    public ProductCatalogueService() {
        products=new ConcurrentHashMap<>();
        productsByCategory = new ConcurrentHashMap<>();
    }

    public void addProduct(Product product) {
        if(products.containsKey(product.getProductId())) {
            return;
        }
        products.put(product.getProductId(), product);
        productsByCategory.computeIfAbsent(product.getProductCategory(),
                k->new CopyOnWriteArrayList<>()).add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product.getProductId());
    }

    public Product getProductById(String productId) {
        return products.get(productId);
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productsByCategory.get(category);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
}
