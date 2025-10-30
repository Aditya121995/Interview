package com.problems.InventoryManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        InventoryManagementSystem inventoryManagementSystem = new InventoryManagementSystem();

        User u1 = new User("u1", "Aditya", "adi@gmail.com");
        User u2 = new User("u2", "Moksha", "moksha@gmail.com");

        Product p1 = new Product("p1", "Apple mac", "",
                100000.0, ProductCategory.ELECTRONICS);
        Product p2 = new Product("p2", "Apple watch", "",
                15000.0, ProductCategory.ELECTRONICS);
        Product p3 = new Product("p3", "Apple Ipad", "",
                25000.0, ProductCategory.ELECTRONICS);
        Product p4 = new Product("p4", "HarryPotter", "",
                1000.0, ProductCategory.BOOKS);
        Product p5 = new Product("p5", "Tshirt half allen solly", "",
                2000.0, ProductCategory.CLOTHES);
        Product p6 = new Product("p7", "Tshirt full allen solly", "",
                2500.0, ProductCategory.CLOTHES);


        inventoryManagementSystem.addProduct(p1, 4);
        inventoryManagementSystem.addProduct(p2, 5);
        inventoryManagementSystem.addProduct(p3, 3);
        inventoryManagementSystem.addProduct(p4, 4);
        inventoryManagementSystem.addProduct(p5, 10);
        inventoryManagementSystem.addProduct(p6, 20);

        System.out.println("........Browsing .......");
        inventoryManagementSystem.browseProducts();
        inventoryManagementSystem.browseProductsByCategory(ProductCategory.ELECTRONICS);

        System.out.println("........adding to cart .......");
        inventoryManagementSystem.addToCart(u1, p1, 1);
        inventoryManagementSystem.addToCart(u1, p2, 1);
        inventoryManagementSystem.addToCart(u1, p5, 10);
        inventoryManagementSystem.addToCart(u2, p6, 20);

        System.out.println("........place order and payment .......");
        Order order = inventoryManagementSystem.placeOrder(u1, PaymentMethod.UPI);
        inventoryManagementSystem.processPayment(order.getPayment().getPaymentId());

        Order orderExp = inventoryManagementSystem.placeOrder(u2, PaymentMethod.DEBIT_CARD);

        System.out.println("........order details .......");
        inventoryManagementSystem.getOrder(order.getOrderId());


        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User("ConcUser-" + i, "User" + i, "User"+i+"@gmail.com");
            userList.add(user);
            inventoryManagementSystem.addToCart(user, p1, 1);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (User user : userList) {
            executor.submit(()->{
                try {
                    Order ord = inventoryManagementSystem.placeOrder(user, PaymentMethod.UPI);
                    if (ord.getPayment() != null) {
                        inventoryManagementSystem.processPayment(ord.getPayment().getPaymentId());
                        inventoryManagementSystem.getOrder(ord.getOrderId());
                    }
                } finally {
                    countDownLatch.countDown();
                }

            });
        }

        countDownLatch.await();
        executor.shutdown();


        Thread.sleep(25000);

        inventoryManagementSystem.shutDown();
    }
}
