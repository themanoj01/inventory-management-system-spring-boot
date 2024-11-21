package com.internship.inventory.inventory_management_system.service;

import com.internship.inventory.inventory_management_system.dto.OrderResponseDTO;
import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.internship.inventory.inventory_management_system.model.Orders;

import java.util.List;

public interface OrderService {
    void createOrder(Orders order);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById(Long orderId);
    void deleteOrderById(Long orderId);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    List<OrderResponseDTO> findPendingOrders();
}
