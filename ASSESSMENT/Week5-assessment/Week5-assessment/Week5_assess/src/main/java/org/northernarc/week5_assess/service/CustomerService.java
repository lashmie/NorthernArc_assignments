package org.northernarc.week5_assess.service;

import java.util.List;

public interface CustomerService {
    Object createCustomer(Object request);

    List<Object> getAllCustomers();

    Object getCustomerById(Long id);

    Object updateCustomer(Long id, Object request);

    void deleteCustomer(Long id);
}

