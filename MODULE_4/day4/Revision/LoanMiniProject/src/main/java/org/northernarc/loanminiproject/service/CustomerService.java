package org.northernarc.loanminiproject.service;

import org.northernarc.loanminiproject.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> findCustomersWithOverdueEmis();
    List<Customer> findTopDefaulters();
}

