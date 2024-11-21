package com.internship.inventory.inventory_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;

    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    @JsonManagedReference
    private Supplier supplier;

    @ManyToMany(mappedBy = "products")
    @JsonBackReference
    private List<Orders> orders = new ArrayList<>();
}
