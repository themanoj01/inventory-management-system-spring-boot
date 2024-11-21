package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.exception.ProductNotFoundException;
import com.internship.inventory.inventory_management_system.exception.SupplierNotFoundException;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private SupplierRepository supplierRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    private Product product;
    private Supplier supplier;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product","Recently launched", 1000, 10, supplier, null);
        supplier = new Supplier(1L, "Test Supplier","test address","9800","test@gmail.com",null);
        productRequestDTO = new ProductRequestDTO("Test product","Recently launched",1000,10,1L);
    }


    @Test
    void getProductById() {
        // if found
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product returnedProduct = productService.getProductById(1L);
        assertNotNull(returnedProduct);
        assertEquals(product.getProductName(), returnedProduct.getProductName());
        verify(productRepository, times(1)).findById(1L);
        // if not found
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        assertEquals("Product with id 1 not found.", thrown.getMessage());
        verify(productRepository, times(2)).findById(1L);
    }

    @Test
    void getAllProducts() {
        List<Product> products = Arrays.asList(product,new Product(2L, "Test","test", 1000, 10, supplier, null));
        when(productRepository.findAll()).thenReturn(products);
        List<Product> returnedProducts = productService.getAllProducts();
        assertNotNull(returnedProducts);
        assertEquals(products.size(), returnedProducts.size());
        assertEquals(products.get(0).getProductName(), returnedProducts.get(0).getProductName());
        assertEquals(products.get(1).getProductName(), returnedProducts.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void createProduct() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(mapper.mapToProduct(productRequestDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product returnedProduct = productService.createProduct(productRequestDTO);
        assertNotNull(returnedProduct);
        assertEquals(product.getProductName(), returnedProduct.getProductName());
        assertEquals(1L,returnedProduct.getSupplier().getSupplierId());
        verify(productRepository, times(1)).save(product);

        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());
        SupplierNotFoundException thrown = assertThrows(SupplierNotFoundException.class, () -> productService.createProduct(productRequestDTO));
        assertEquals("Supplier with ID 1 not found.", thrown.getMessage());
    }

    @Test
    void updateProduct() {
        ProductRequestDTO updated = new ProductRequestDTO("updated","updated",1000,10,1L);
        Product updatedProduct = new Product(1L, "updated","updated", 1000, 10, supplier, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.save(product)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updated);
        assertNotNull(result);
        assertEquals(updatedProduct.getProductName(), result.getProductName());
        assertEquals(updatedProduct.getSupplier().getSupplierId(), result.getSupplier().getSupplierId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void findProductsByName() {
        when(productRepository.findProductsByName("Test Product")).thenReturn(Collections.singletonList(product));
        List<Product> returnedProducts = productService.findProductsByName("Test Product");
        assertNotNull(returnedProducts);
        assertEquals(product.getProductName(), returnedProducts.get(0).getProductName());
        verify(productRepository, times(1)).findProductsByName("Test Product");
    }
}