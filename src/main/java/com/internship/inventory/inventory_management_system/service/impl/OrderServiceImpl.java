package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.OrderResponseDTO;
import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.internship.inventory.inventory_management_system.exception.OrderNotFoundException;
import com.internship.inventory.inventory_management_system.exception.ProductNotFoundException;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.repository.OrderRepository;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, Mapper mapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }
    @Override
    public void createOrder(Orders order) {
        for(Product product : order.getProducts()) {
            if(!productRepository.existsById(product.getProductId())){
                throw new ProductNotFoundException("Product not found");
            }
        }
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        return mapper.mapToOrderResponseDTOList(orders);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Optional<Orders> order = orderRepository.findById(orderId);
        return order.map(mapper::mapToOrderResponseDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public void deleteOrderById(Long orderId) {
        Orders existingOrder = orderRepository.findById(orderId)
                        .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
        orderRepository.delete(existingOrder);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
        existingOrder.setOrderStatus(newStatus);
        orderRepository.save(existingOrder);
    }

    @Override
    public List<OrderResponseDTO> findPendingOrders() {
        List<Orders> pendingOrders = orderRepository.findPendingOrders();
        return pendingOrders.stream()
                .map(mapper::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }
}
