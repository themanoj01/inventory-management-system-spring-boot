package com.internship.inventory.inventory_management_system.service;

import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long productId);
    List<Product> getAllProducts();
    Product createProduct(ProductRequestDTO productRequestDTO);
    Product updateProduct(Long productId,ProductRequestDTO productRequestDTO);
    void deleteProduct(Long productId);
    List<Product> findProductsByName(String productName);
}
