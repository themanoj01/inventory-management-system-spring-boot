package com.internship.inventory.inventory_management_system.controller;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.dto.ProductResponseDTO;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.service.CsvService;
import com.internship.inventory.inventory_management_system.service.ProductService;
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
@RequestMapping("/api/products")
@EnableMethodSecurity
@Tag(name="Products", description = "Operations related to products.")
public class ProductController {
    private final ProductService productService;
    private final CsvService csvService;
    private final Mapper mapper;

    public ProductController(ProductService productService, CsvService csvService, Mapper mapper) {
        this.productService = productService;
        this.csvService = csvService;
        this.mapper = mapper;
    }

    // Create a new product
    @PostMapping("/create")
    @Operation(summary = "Create a new Product", description = "Create new product in the inventory.")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.createProduct(productRequestDTO);
        mapper.mapToProductResponseDTO(savedProduct);
        return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
    }

    // Get a list of all products
    @GetMapping
    @Operation(summary = "Get all products from database", description = "Retrieve a list of all products.")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get a product by ID
    @GetMapping("/{productId}")
    @Operation(summary = "Get product by Id", description = "Retrieve details of specific product by its unique id.")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // Update a product
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}")
    @Operation(summary = "update existing product", description = "update the details of existing product.")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDTO productRequestDTO) {
        Product updatedProduct = productService.updateProduct(productId, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete Product", description = "Delete a particular product from inventory.")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully.", HttpStatus.OK);
    }

    //Get products by name
    @GetMapping("/productName")
    @Operation(summary = "Get products by name", description = "Retrieve products from inventory matching their names.")
    public ResponseEntity<List<Product>> getProductsByName(@RequestParam String productName) {
        List<Product> products = productService.findProductsByName(productName);
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import products from csv", description = "Import products into inventory from a csv file")
    public ResponseEntity<List<ProductRequestDTO>> importProductsFromCsv(@RequestParam("file") MultipartFile file) {
        List<ProductRequestDTO> products = csvService.importProductsFromCsv(file);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/export")
    @Operation(summary = "Export products to csv", description = "Export all products in a csv file.")
    public ResponseEntity<Void> exportProductsToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=products.csv");
        PrintWriter writer = response.getWriter();
        csvService.exportProductsToCsv(writer);
        writer.flush();
        return ResponseEntity.ok().build();
    }

}
