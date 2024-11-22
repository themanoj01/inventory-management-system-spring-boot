package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.ProductRequestDTO;
import com.internship.inventory.inventory_management_system.exception.ProductNotFoundException;
import com.internship.inventory.inventory_management_system.exception.SupplierNotFoundException;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import com.internship.inventory.inventory_management_system.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(ProductRepository productRepository, Mapper mapper, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.supplierRepository = supplierRepository;
    }
    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Supplier supplier = supplierRepository.findById(productRequestDTO.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with ID " + productRequestDTO.getSupplierId() + " not found."));
        Product product = mapper.mapToProduct(productRequestDTO);
        product.setSupplier(supplier);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequestDTO product) {
        Product existingProduct = getProductById(productId);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        Supplier supplier = supplierRepository.findById(product.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with id " + product.getSupplierId() + "not found."));
        existingProduct.setSupplier(supplier);
        productRepository.save(existingProduct);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> findProductsByName(String productName) {
            List<Product> products =  productRepository.findProductsByName(productName);
            if (products.isEmpty()) {
                throw new ProductNotFoundException("Product with name " + productName + " not found.");
            }
            return products;
    }

}
