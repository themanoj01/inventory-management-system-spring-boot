package com.internship.inventory.inventory_management_system.service;

import com.internship.inventory.inventory_management_system.dto.OrderRequestDTO;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.util.List;

public interface CsvService {
    List<ProductRequestDTO> importProductsFromCsv(MultipartFile file);
    void exportProductsToCsv(PrintWriter writer);
    List<OrderRequestDTO> importOrdersFromCsv(MultipartFile file);
    void exportOrdersToCsv(PrintWriter writer);
}
