package com.problems.ecommerce.service;

import com.problems.ecommerce.entity.Cart;
import com.problems.ecommerce.entity.Customer;
import com.problems.ecommerce.entity.Product;
import com.problems.ecommerce.repository.CartRepository;
import com.problems.ecommerce.repository.CartRepositoryImpl;

import java.util.Optional;

public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public CartService(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
        this.cartRepository = new CartRepositoryImpl();
    }

    public boolean addItemToCart(String customerId, String productId, int quantity) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            System.out.println("product not found");
            return false;
        }

        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("customer not found");
            return false;
        }

        Optional<Cart> cartOpt = cartRepository.findCartByCustomerId(customerId);
        Cart cart = cartOpt.orElse(new Cart(customerId));

        boolean res = cart.addItem(product, quantity);
        if (!res) {
            System.out.println("Item quantity is out of stock");
            return false;
        }

        cartRepository.saveCart(cart);
        return true;
    }

    public Cart getCart(String customerId) {
        Optional<Cart> cartOpt = cartRepository.findCartByCustomerId(customerId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.removeExpiredItems();
            return cart;
        }
        return null;
    }

    public void clearCart(String customerId) {
        Optional<Cart> cartOpt = cartRepository.findCartByCustomerId(customerId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.clear();
            cartRepository.deleteCartByCustomerId(customerId);
        }
    }

    public double getCartTotal(String customerId) {
        Optional<Cart> cartOpt = cartRepository.findCartByCustomerId(customerId);
        return cartOpt.map(Cart::calculateTotal).orElse(0.0);
    }


}
