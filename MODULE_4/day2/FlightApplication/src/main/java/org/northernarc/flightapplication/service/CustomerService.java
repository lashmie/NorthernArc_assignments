package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.CustomerRequestDTO;
import org.northernarc.flightapplication.dto.CustomerResponseDTO;
import org.northernarc.flightapplication.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO findCustomerById(Long id);
    List<CustomerResponseDTO> findAllCustomers();
    CustomerResponseDTO updateCustomerById(Long id, CustomerRequestDTO customerRequestDTO);
    void deleteCustomer(Long id);
}
