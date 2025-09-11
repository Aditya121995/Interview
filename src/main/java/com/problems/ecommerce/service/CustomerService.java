package com.problems.ecommerce.service;

import com.problems.ecommerce.entity.Customer;
import com.problems.ecommerce.repository.CustomerRepository;
import com.problems.ecommerce.repository.CustomerRepositoryImpl;

import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepositoryImpl();
    }

    public String addCustomer(String name) {
        Customer customer = new Customer(name);
        customerRepository.saveCustomer(customer);
        System.out.println("Added Customer with name " + name);
        return customer.getId();
    }

    public Customer getCustomer(String customerId) {
        Optional<Customer> customer = customerRepository.findCustomerById(customerId);
        return customer.orElse(null);
    }
}
