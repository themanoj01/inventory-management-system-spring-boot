package com.internship.inventory.inventory_management_system.controller;

import com.internship.inventory.inventory_management_system.dto.SupplierDTO;
import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.service.OrderService;
import com.internship.inventory.inventory_management_system.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@EnableMethodSecurity
@Tag(name = "Suppliers", description = "Crud operations related to suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @GetMapping
    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers from database.")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @GetMapping("/{supplierId}")
    @Operation(summary = "Get supplier by ID", description = "Retrieve supplier details by their unique ID.")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        return ResponseEntity.ok(supplier);
    }
    @PostMapping("/create")
    @Operation(summary = "Create supplier", description = "Create a new supplier.")
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierDTO supplier) {
        Supplier supplierCreated = supplierService.createSupplier(supplier);
        return new ResponseEntity<>(supplierCreated, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{supplierId}")
    @Operation(summary = "Update supplier by ID", description = "Update supplier details by their unique ID.")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierDTO supplier) {
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, supplier);
        return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{supplierId}")
    @Operation(summary = "Delete particular supplier.", description = "Delete supplier from database.")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return new ResponseEntity<>("Supplier deleted successfully.",HttpStatus.NO_CONTENT);
    }
}
