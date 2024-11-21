package com.internship.inventory.inventory_management_system.repository;

import com.internship.inventory.inventory_management_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM Product WHERE product_name=:productName", nativeQuery = true)
    List<Product> findProductsByName(String productName);
}

