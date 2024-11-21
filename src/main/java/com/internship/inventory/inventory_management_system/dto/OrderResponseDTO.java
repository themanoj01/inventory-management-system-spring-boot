package com.internship.inventory.inventory_management_system.dto;

import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.internship.inventory.inventory_management_system.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private double orderTotal;
    private Long userId;
}
