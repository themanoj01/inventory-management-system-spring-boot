package com.internship.inventory.inventory_management_system.controller;

import com.internship.inventory.inventory_management_system.dto.OrderRequestDTO;
import com.internship.inventory.inventory_management_system.dto.OrderResponseDTO;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.internship.inventory.inventory_management_system.exception.ProductNotFoundException;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.service.CsvService;
import com.internship.inventory.inventory_management_system.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Orders related operations inventory.")
public class OrderController {
    private final OrderService orderService;
    private final CsvService csvService;
    public OrderController(OrderService orderService, CsvService csvService) {
        this.orderService = orderService;
        this.csvService = csvService;
    }
    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders from the inventory.")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by Id", description = "Get a particular order details by its id.")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    @PostMapping("/create")
    @Operation(summary = "Create order", description = "create and save a new order in the inventory.")
    public ResponseEntity<String> createOrder(@RequestBody Orders order) {
            orderService.createOrder(order);
            return new ResponseEntity<>( "Order created successfully", HttpStatus.CREATED);
    }

    //to update the status of order either pending or delivered or canceled
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{orderId}")
    @Operation(summary = "Update order status", description = "Update order status of any order.")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return new ResponseEntity<>("Order status updated successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order", description = "Delete a particular order by its id.")
    public ResponseEntity<Orders> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/pending")
    @Operation(summary = "find pending orders", description = "Get orders with status pending from inventory.")
    public ResponseEntity<List<OrderResponseDTO>> findPendingOrders() {
        List<OrderResponseDTO> pending = orderService.findPendingOrders();
        return new ResponseEntity<>(pending, HttpStatus.OK);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import orders from csv", description = "Import orders into inventory from a csv file")
    public ResponseEntity<List<OrderRequestDTO>> importOrdersFromCsv(@RequestParam("file") MultipartFile file) {
        List<OrderRequestDTO> orders = csvService.importOrdersFromCsv(file);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/export")
    @Operation(summary = "Export orders to csv", description = "Export all orders in a csv file.")
    public ResponseEntity<Void> exportOrdersToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=orders.csv");
        PrintWriter writer = response.getWriter();
        csvService.exportOrdersToCsv(writer);
        writer.flush();
        return ResponseEntity.ok().build();
    }
}
