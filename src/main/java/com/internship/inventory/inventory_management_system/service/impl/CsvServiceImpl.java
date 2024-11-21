package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.OrderRequestDTO;
import com.internship.inventory.inventory_management_system.dto.OrderResponseDTO;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.dto.ProductResponseDTO;
import com.internship.inventory.inventory_management_system.exception.CsvExportException;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.repository.OrderRepository;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.service.CsvService;
import com.internship.inventory.inventory_management_system.util.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvServiceImpl implements CsvService {
    @Autowired
    private CsvHelper csvHelper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<ProductRequestDTO> importProductsFromCsv(MultipartFile file) {
        return csvHelper.importProductsFromCsv(file);
    }

    @Override
    public void exportProductsToCsv(PrintWriter writer) {
        List<ProductResponseDTO> products = getProductsForExport();
        csvHelper.exportProductsToCsv(writer, products);
    }

    @Override
    public List<OrderRequestDTO> importOrdersFromCsv(MultipartFile file) {
        return csvHelper.importOrdersFromCsv(file);
    }

    @Override
    public void exportOrdersToCsv(PrintWriter writer) {
        List<OrderResponseDTO> orders = getOrdersForExport();
        csvHelper.exportOrdersToCsv(writer, orders);
    }

    private List<ProductResponseDTO> getProductsForExport() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponseDTO(
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getSupplier() != null ? product.getSupplier().getSupplierId() : null
                ))
                .collect(Collectors.toList());
    }
    private List<OrderResponseDTO> getOrdersForExport() {
        return orderRepository.findAll().stream()
                .map(order ->new OrderResponseDTO(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getOrderStatus(),
                        order.getOrderTotal(),
                        order.getUser() != null ? order.getUser().getUserId() : null
                ))
                .collect(Collectors.toList());
    }
}
