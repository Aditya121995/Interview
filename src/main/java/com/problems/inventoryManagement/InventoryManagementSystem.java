package com.problems.inventoryManagement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManagementSystem {
    private final ProductCatalogueService productCatalogueService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final OrderExpirationService orderExpirationService;

    public InventoryManagementSystem() {
        productCatalogueService=new ProductCatalogueService();
        inventoryService=new InventoryService();
        paymentService=new PaymentService();
        orderService = new OrderService(paymentService, inventoryService);
        paymentService.addPaymentObserver(orderService);
        this.orderExpirationService = new OrderExpirationService(orderService, inventoryService);
        orderExpirationService.startExpirationMonitoring();
    }

    public void addProduct(Product product, int quantity) {
        productCatalogueService.addProduct(product);
        inventoryService.addInventory(product.getProductId(), quantity);
    }

    public List<Product> browseProducts() {
        System.out.println("======= products =====");
        List<Product> products = productCatalogueService.getAllProducts();
        for (Product p : products) {
            System.out.println(p);
        }
        return products;
    }

    public List<Product> browseProductsByCategory(ProductCategory category) {
        System.out.println("=======" + category + " products =====");
        List<Product> products = productCatalogueService.getProductsByCategory(category);
        for (Product p : products) {
            System.out.println(p);
        }
        return products;
    }

    public void addToCart(User user, Product product, int quantity) {
        user.getCart().addItem(product, quantity);
    }

    public Order placeOrder(User user, PaymentMethod paymentMethod) {
        return orderService.createOrder(user, paymentMethod);
    }

    public void processPayment(String paymentId) {
        paymentService.processPayment(paymentId);
    }

    public void getOrder(String orderId) {
        System.out.println("Order: " + orderService.getOrder(orderId));
    }

    public void shutDown() {
        orderExpirationService.shutdown();
    }
}
