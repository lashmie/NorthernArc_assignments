package org.northernarc.ecom.service;
//
//import org.springdatajpademo.Ecommerce.DTO.OrderItemRequestDTO;
//import org.example.springdatajpademo.Ecommerce.DTO.OrderItemResponseDTO;
//import org.example.springdatajpademo.Ecommerce.DTO.OrderItemUpdateDTO;

import org.northernarc.ecom.DTO.OrderItemRequestDTO;
import org.northernarc.ecom.DTO.OrderItemResponseDTO;
import org.northernarc.ecom.DTO.OrderItemUpdateDTO;

import java.util.List;

public interface OrderItemService {

    OrderItemResponseDTO saveOrderItem(OrderItemRequestDTO dto);

    List<OrderItemResponseDTO> getAllOrderItems();

    OrderItemResponseDTO getOrderItemById(Integer id);

    OrderItemResponseDTO updateOrderItem(Integer id,
                                         OrderItemUpdateDTO dto);

    void deleteOrderItem(Integer id);

    List<OrderItemResponseDTO> getItemsByOrder(Integer orderId);
}