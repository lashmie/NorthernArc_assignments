package org.northernarc.week5_assess.service;

import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.dto.CustomerRequest;
import org.northernarc.week5_assess.dto.CustomerResponse;
import org.northernarc.week5_assess.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerFacadeService implements CustomerService {

    private final CustomerServiceImpl customerServiceImpl;

    public CustomerFacadeService(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setActive(true);
        return toResponse(customerServiceImpl.createCustomer(customer));
    }

    @Override
    public Object createCustomer(Object request) {
        if (request instanceof CustomerRequest customerRequest) {
            return createCustomer(customerRequest);
        }
        return null;
    }

    @Override
    public List<Object> getAllCustomers() {
        return Collections.emptyList();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return toResponse(customerServiceImpl.getCustomerById(id));
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        return toResponse(customerServiceImpl.updateCustomer(id, customer));
    }

    @Override
    public Object updateCustomer(Long id, Object request) {
        if (request instanceof CustomerRequest customerRequest) {
            return updateCustomer(id, customerRequest);
        }
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerServiceImpl.deleteCustomer(id);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        return response;
    }
}

