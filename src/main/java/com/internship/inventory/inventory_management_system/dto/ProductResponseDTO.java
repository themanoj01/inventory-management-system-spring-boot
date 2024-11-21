package com.internship.inventory.inventory_management_system.dto;

import com.internship.inventory.inventory_management_system.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;
    private Long supplierId;
}
