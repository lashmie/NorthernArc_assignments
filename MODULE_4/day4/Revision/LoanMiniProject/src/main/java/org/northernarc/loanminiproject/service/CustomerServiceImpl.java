package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.model.Customer;
import org.northernarc.loanminiproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomersWithOverdueEmis() {
        return customerRepository.findCustomersWithOverdueEmis();
    }

    @Override
    public List<Customer> findTopDefaulters() {
        return customerRepository.findTopDefaulters();
    }
}

