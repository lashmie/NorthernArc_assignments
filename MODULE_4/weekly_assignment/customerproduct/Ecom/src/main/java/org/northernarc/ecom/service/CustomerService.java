package org.northernarc.ecom.service;



import org.northernarc.ecom.DTO.CustomerRequestDTO;
import org.northernarc.ecom.DTO.CustomerResponseDTO;
import org.northernarc.ecom.DTO.CustomerUpdateDTO;
import org.northernarc.ecom.model.Order;

import java.util.List;

public interface CustomerService {

CustomerResponseDTO saveCustomer(CustomerRequestDTO customerDTO);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(Integer id);

    CustomerResponseDTO updateCustomer(Integer id,
                                       CustomerUpdateDTO customerDTO);

    void deleteCustomer(Integer id);

    List<Order> getCustomerOrders(Integer customerId);
}
