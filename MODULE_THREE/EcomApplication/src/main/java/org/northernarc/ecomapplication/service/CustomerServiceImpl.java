package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Customer;
import org.northernarc.ecomapplication.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerDao customerDao;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public List<Customer> getCustomersByName(String name) {
        return customerDao.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerDao.deleteById(id);
    }


}
