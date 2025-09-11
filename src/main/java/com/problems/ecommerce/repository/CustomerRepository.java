package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Customer;

import java.util.Optional;

public interface CustomerRepository {
    void saveCustomer(Customer customer);
    Optional<Customer> findCustomerById(String id);
}
