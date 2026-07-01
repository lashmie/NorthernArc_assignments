package org.northernarc.ecom.service;

import org.northernarc.ecom.DTO.OrderRequestDTO;
import org.northernarc.ecom.DTO.OrderResponseDTO;
import org.northernarc.ecom.DTO.OrderUpdateDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO placeOrder(OrderRequestDTO request);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Integer id);

    void cancelOrder(Integer orderId);

    List<OrderResponseDTO> getOrdersByCustomer(Integer customerId);

    OrderResponseDTO updateOrder(Integer id,
                                 OrderUpdateDTO dto);
}