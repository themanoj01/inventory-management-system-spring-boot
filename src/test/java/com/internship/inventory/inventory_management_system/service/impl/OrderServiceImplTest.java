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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;
    private OrderResponseDTO orderResponseDTO;
    private Orders order;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(1L);
        orderResponseDTO.setOrderStatus(OrderStatus.PENDING);

        products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("test");
        products.add(product1);
        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("test2");
        products.add(product2);

        order = new Orders();
        order.setOrderId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setProducts(products);
    }

    @Test
    void createOrder() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(2L)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);
        orderService.createOrder(order);
        verify(orderRepository, times(1)).save(order);

        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(order));
    }

    @Test
    void getAllOrders() {
        List<Orders> orders = new ArrayList<>();
        Orders order1 = new Orders();
        order1.setOrderId(1L);
        Orders order2 = new Orders();
        order2.setOrderId(2L);
        orders.add(order1);
        orders.add(order2);
        when(orderRepository.findAll()).thenReturn(orders);
        when(mapper.mapToOrderResponseDTOList(orders)).thenReturn(Arrays.asList(new OrderResponseDTO(), new OrderResponseDTO()));
        List<OrderResponseDTO> orderResponseDTOs = orderService.getAllOrders();
        assertNotNull(orderResponseDTOs);
        assertEquals(2, orderResponseDTOs.size());
    }

    @Test
    void getOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.mapToOrderResponseDTO(order)).thenReturn(new OrderResponseDTO());
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(1L);
        assertNotNull(orderResponseDTO);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void deleteOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.deleteOrderById(1L);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void updateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.updateOrderStatus(1L, OrderStatus.DELIVERED);
        verify(orderRepository, times(1)).save(order);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.DELIVERED));

    }

    @Test
    void findPendingOrders() {
        Orders pendingOrders = new Orders();
        List<Orders> pendingOrdersList = List.of(pendingOrders);
        when(orderRepository.findPendingOrders()).thenReturn(pendingOrdersList);

        List<OrderResponseDTO> orderResponseDTOs = orderService.findPendingOrders();
        assertNotNull(orderResponseDTOs);
        assertEquals(1, orderResponseDTOs.size());
    }
}