package com.problems.ecommerce.controller;

import com.problems.ecommerce.entity.Cart;
import com.problems.ecommerce.entity.Order;
import com.problems.ecommerce.enums.PaymentMode;
import com.problems.ecommerce.service.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EcommerceSystemDemo {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final ProductService productService;
    private final PincodeServiceabilityService  pincodeService;

    public EcommerceSystemDemo() {
        this.productService = new ProductService();
        this.customerService = new CustomerService();
        this.cartService = new CartService(productService, customerService);
        this.pincodeService =  new PincodeServiceabilityService();
        this.orderService = new OrderService(cartService, customerService, pincodeService);
    }

    public void runDemo() throws InterruptedException {
        // Add Products
        String p1 = productService.addProduct("iPhone 15", 80000.0, 50);
        String p2 = productService.addProduct("Samsung Galaxy S24", 70000.0, 30);
        String p3 = productService.addProduct("MacBook Pro", 150000.0, 20);
        String p4 = productService.addProduct("iPad Air", 60000.0, 40);

        // Add Customers
        String c1 = customerService.addCustomer("John Doe");
        String c2 = customerService.addCustomer("Jane Smith");
        String c3 = customerService.addCustomer("Bob Wilson");

        // Setup Pincode Serviceability
        pincodeService.addServiceability("110001", Set.of(PaymentMode.COD, PaymentMode.CARD, PaymentMode.UPI));
        pincodeService.addServiceability("400001", Set.of(PaymentMode.CARD, PaymentMode.UPI));
        pincodeService.addServiceability("560001", Set.of(PaymentMode.COD, PaymentMode.UPI));
        pincodeService.addServiceability("700001", Set.of(PaymentMode.COD, PaymentMode.UPI));
        pincodeService.removeServiceability("700001"); // Not serviceable

        System.out.println("=== E-commerce Application Demo ===\n");

        // 1. Check initial inventory
        System.out.println("1. Initial Product Inventory:");
        System.out.println("iPhone 15: " + productService.checkProductInventoryCount(p1));
        System.out.println("Samsung Galaxy S24: " + productService.checkProductInventoryCount(p2));
        System.out.println();

        // 2. Add products to cart
        System.out.println("2. Adding products to cart:");
        boolean result1 = cartService.addItemToCart(c1, p1, 2);
        boolean result2 = cartService.addItemToCart(c2, p2, 1);
        System.out.println("Added iPhone 15 (qty: 2) to C1's cart: " + result1);
        System.out.println("Added Samsung Galaxy S24 (qty: 1) to C1's cart: " + result2);
        System.out.println();

        // 3. Check updated inventory
        System.out.println("3. Inventory after adding to cart:");
        System.out.println("iPhone 15: " + productService.checkProductInventoryCount(p1));
        System.out.println("Samsung Galaxy S24: " + productService.checkProductInventoryCount(p2));
        System.out.println();

        // 4. Check cart total
        double cartTotal = cartService.getCartTotal(c1);
        System.out.println("4. Cart total for C1: ₹" + cartTotal);
        System.out.println();

        // 5. Check pincode serviceability
        System.out.println("5. Checking serviceability for C1's pincode (110001):");
        boolean serviceable = pincodeService.isServiceable("110001");
        Set<PaymentMode> paymentModes = pincodeService.getSupportedPaymentMethods("110001");
        System.out.println("Serviceable: " + serviceable);
        System.out.println("Supported payment modes: " + paymentModes);
        System.out.println();

        // 6. Place order
        System.out.println("6. Placing order:");
        String orderId = orderService.placeOrder(c1, PaymentMode.COD, "110001");
        Order order = orderService.getOrder(orderId);
        if (order != null) {
            System.out.println("Order placed successfully!");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Order Status: " + order.getOrderStatus());
            System.out.println("Total Amount: ₹" + order.getTotalAmount());
        }
        System.out.println();

        // 7. Confirm order
        System.out.println("7. Confirming order:");
        if (order != null) {
            boolean confirmed = orderService.confirmOrder(order.getOrderId());
            System.out.println("Order confirmed: " + confirmed);
            if (confirmed) {
                Order confirmedOrder = orderService.getOrder(order.getOrderId());
                System.out.println("Final order status: " + confirmedOrder.getOrderStatus());
            }
        }
        System.out.println();

        // 8. Check final inventory
        System.out.println("8. Final inventory after order confirmation:");
        System.out.println("iPhone 15: " + productService.checkProductInventoryCount(p1));
        System.out.println("Samsung Galaxy S24: " + productService.checkProductInventoryCount(p2));

        System.out.println("\n=== Concurrent Ordering Demo ===\n");

        // Reset inventory
//        productService.updateInventory(p3, 5); // Only 5 MacBooks available

        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        // Three customers trying to order MacBook concurrently
        List<String> customerList = List.of(c1, c2, c3);
        for (int i = 1; i <= 3; i++) {
            String customerId = customerList.get(i - 1);
            executor.submit(() -> {
                try {
                    System.out.println("Customer " + customerId + " trying to add MacBook to cart...");
                    boolean added = cartService.addItemToCart(customerId, p3, 8);
                    System.out.println("Customer " + customerId + " cart addition: " + added);

                    if (added) {
                        String oId = orderService.placeOrder(customerId, PaymentMode.CARD, "110001");
                        Order o = orderService.getOrder(oId);
                        if (o != null) {
                            System.out.println("Customer " + customerId + " placed order: " + o.getOrderId());
                            // Simulate some processing time
                            Thread.sleep(100);
                            orderService.confirmOrder(o.getOrderId());
                            System.out.println("Customer " + customerId + " confirmed order");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("Final MacBook inventory: " + productService.checkProductInventoryCount(p3));



        System.out.println("\n=== Cart Expiration Demo ===\n");

        // Add item to cart
        cartService.addItemToCart(c2, p4, 1);
        System.out.println("Added iPad Air to C2's cart");
        System.out.println("iPad Air inventory: " + productService.checkProductInventoryCount(p4));

        // Wait for 6 seconds (simulating 5+ minute expiration)
        System.out.println("Simulating cart expiration... (waiting 6 seconds)");
        Thread.sleep(6000);

        // Modify CartItem to have shorter expiration for demo
        Cart cart = cartService.getCart(c2);
        if (cart != null) {
            // Force expiration check
            System.out.println("After expiration check:");
            System.out.println("Cart items count: " + cart.getItems().size());
            System.out.println("iPad Air inventory (should be restored): " + productService.checkProductInventoryCount(p4));
        }

    }

    public static void main(String[] args) throws InterruptedException {
        EcommerceSystemDemo demo = new EcommerceSystemDemo();
        demo.runDemo();
    }
}
