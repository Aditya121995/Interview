package com.problems.ecommerce.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
    private final String id;
    private final String name;

    public Customer(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
