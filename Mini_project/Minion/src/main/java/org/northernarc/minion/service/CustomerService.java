package org.northernarc.minion.service;

import org.northernarc.minion.model.Customer;
import org.northernarc.minion.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomersWithOverdueEmis() {
        return customerRepository.findCustomersWithOverdueEmis();
    }

    public List<Customer> getTopDefaulters() {
        return customerRepository.findTopDefaulters();
    }
}

