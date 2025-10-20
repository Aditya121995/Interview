package com.problems.vendingMachine;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    private final Map<String, Product> products;
    private final Map<String, Integer> quantities;

    public Inventory() {
        this.products = new ConcurrentHashMap<>();
        this.quantities = new ConcurrentHashMap<>();
    }

    public synchronized void addProduct(Product product) {
        if (!products.containsKey(product.getCode())) {
            products.put(product.getCode(), product);
        }

        quantities.put(product.getCode(), quantities.getOrDefault(product.getCode(), 0) + 1);
    }

    public synchronized boolean hasProduct(String code) {
        return products.containsKey(code) && quantities.get(code) > 0;
    }

    public synchronized void reduceProduct(Product product) {
        if (hasProduct(product.getCode())) {
            quantities.put(product.getCode(), quantities.get(product.getCode()) - 1);
        }
    }

    public synchronized Product getProduct(String code) {
        return products.get(code);
    }

    public synchronized Map<String, Integer> getInventoryStatus() {
        return new HashMap<>(quantities);
    }
}
