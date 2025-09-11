package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Customer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerRepositoryImpl implements CustomerRepository {
    private final Map<String, Customer> customers;

    public CustomerRepositoryImpl() {
        customers = new ConcurrentHashMap<>();
    }

    @Override
    public void saveCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public Optional<Customer> findCustomerById(String  id) {
        return Optional.ofNullable(customers.get(id));
    }
}
