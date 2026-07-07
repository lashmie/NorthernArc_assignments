package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Customer;

import java.util.List;

public interface CustomerService {
Customer createCustomer(Customer customer);
Customer getCustomerById(Long id);
List<Customer> getAllCustomers();
List<Customer> getCustomersByName(String name);
void deleteCustomer(Long id);
}
