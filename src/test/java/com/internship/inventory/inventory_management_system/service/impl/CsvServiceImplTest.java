package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.OrderRequestDTO;
import com.internship.inventory.inventory_management_system.dto.OrderResponseDTO;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.dto.ProductResponseDTO;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.repository.OrderRepository;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.util.CsvHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvServiceImplTest {
    @Mock
    private CsvHelper csvHelper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PrintWriter writer;
    @InjectMocks
    private CsvServiceImpl csvService;
    private MultipartFile file;
    @BeforeEach
    void setUp(){
//        productResponseDTOs = Arrays.asList(
//                new ProductResponseDTO(), new ProductResponseDTO()
//        );
//        orderResponseDTOs = Arrays.asList(
//                new OrderResponseDTO(), new OrderResponseDTO()
//        );

    }
    @Test
    void importProductsFromCsv() {
        when(csvHelper.importProductsFromCsv(file)).thenReturn((Arrays.asList(
                new ProductRequestDTO(), new ProductRequestDTO()
        )));
        List<ProductRequestDTO> productRequestDTOs = csvService.importProductsFromCsv(file);
        assertNotNull(productRequestDTOs);
        assertEquals(2, productRequestDTOs.size());
    }

    @Test
    void exportProductsToCsv() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                new Product(),new Product(),new Product()
        ));
        csvService.exportProductsToCsv(writer);
        verify(csvHelper,times(1)).exportProductsToCsv(eq(writer), anyList());
    }

    @Test
    void importOrdersFromCsv() {
        when(csvHelper.importOrdersFromCsv(file)).thenReturn((Arrays.asList(
                new OrderRequestDTO(), new OrderRequestDTO()
        )));
        List<OrderRequestDTO> orderRequestDTOs = csvService.importOrdersFromCsv(file);
        assertNotNull(orderRequestDTOs);
        assertEquals(2, orderRequestDTOs.size());
        verify(csvHelper, times(1)).importOrdersFromCsv(file);
    }

    @Test
    void exportOrdersToCsv() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(
                new Orders(),new Orders(),new Orders()
        ));
        csvService.exportOrdersToCsv(writer);
        verify(csvHelper,times(1)).exportOrdersToCsv(eq(writer), anyList());
    }
}