package org.northernarc.week5_assess.service;

import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl {

    public CustomerServiceImpl(CustomerRepository customerRepository) {
    }

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    }

    public Customer registerCustomer(Customer request) {
        return null;
    }

    public List<Customer> getAllCustomers() {
        return Collections.emptyList();
    }

    public Customer getCustomerById(Long id) {
        return null;
    }

    public Customer updateCustomer(Long id, Customer request) {
        return null;
    }

    public void deleteCustomer(Long id) {
    }

    public Customer createCustomer(Customer request) {
        return null;
    }
}
