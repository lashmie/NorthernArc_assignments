package org.northernarc.flightapplication.service;

import org.northernarc.flightapplication.dto.CustomerRequestDTO;
import org.northernarc.flightapplication.dto.CustomerResponseDTO;
import org.northernarc.flightapplication.exception.CustomerNotFoundException;
import org.northernarc.flightapplication.model.Customer;
import org.northernarc.flightapplication.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = mapToEntity(customerRequestDTO);
        customerRepo.save(customer);
        return mapToDTO(customer);
    }


    @Override
    public CustomerResponseDTO findCustomerById(Long id) {
       Customer customer= customerRepo.findById(id)
               .orElseThrow(()-> new CustomerNotFoundException("customer not fount...."));
       return mapToDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> findAllCustomers() {
        return customerRepo.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public CustomerResponseDTO updateCustomerById(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        // Update the customer fields as needed
        // For example, you can update the name, email, etc.
        // customer.setName(updatedName);
        // customer.setEmail(updatedEmail);
        // ...
customer.setName(customerRequestDTO.getName());
customer.setEmail(customerRequestDTO.getEmail());
customer.setAddress(customerRequestDTO.getAddress());
        customerRepo.save(customer);
        return mapToDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
customerRepo.findById(id).orElseThrow(()-> new CustomerNotFoundException("customer not found with id: "+id));
        customerRepo.deleteById(id);
    }

    public Customer mapToEntity(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setAddress(customerRequestDTO.getAddress());
        customer.setPassportNo(customerRequestDTO.getPassportNo());
        customer.setPhone(customerRequestDTO.getPhone());

        return customer;
    }
    public CustomerResponseDTO mapToDTO(Customer customer) {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setCustomerId(customer.getCustomerId());
        customerResponseDTO.setName(customer.getName());
        return customerResponseDTO;
    }
}
