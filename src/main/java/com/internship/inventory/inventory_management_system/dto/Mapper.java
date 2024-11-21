package com.internship.inventory.inventory_management_system.dto;

import com.internship.inventory.inventory_management_system.exception.ProductNotFoundException;
import com.internship.inventory.inventory_management_system.exception.SupplierNotFoundException;
import com.internship.inventory.inventory_management_system.exception.UserNotFoundException;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.model.User;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import com.internship.inventory.inventory_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public Product mapToProduct(ProductRequestDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        Long supplierId = productDTO.getSupplierId();
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id " + supplierId));
        product.setSupplier(supplier);
        return product;
    }
    public ProductResponseDTO mapToProductResponseDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setProductName(product.getProductName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setQuantity(product.getQuantity());
        productResponseDTO.setSupplierId(product.getSupplier().getSupplierId());
        return productResponseDTO;
    }

    public Orders mapToOrder(OrderRequestDTO ordersDTO) {
        Orders order = new Orders();
        order.setOrderDate(ordersDTO.getOrderDate());
        order.setOrderStatus(ordersDTO.getOrderStatus());
        order.setOrderTotal(ordersDTO.getOrderTotal());
        Long userId = ordersDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        order.setUser(user);
        return order;
    }

    public OrderResponseDTO mapToOrderResponseDTO(Orders order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getOrderId());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setOrderStatus(order.getOrderStatus());
        orderResponseDTO.setOrderTotal(order.getOrderTotal());
        Long userId = order.getUser().getUserId();
        orderResponseDTO.setUserId(userId);
        return orderResponseDTO;
    }

    public List<OrderResponseDTO> mapToOrderResponseDTOList(List<Orders> orders) {
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for(Orders order : orders ){
            orderResponseDTOList.add(mapToOrderResponseDTO(order));
        }
        return orderResponseDTOList;
    }

    public Supplier mapToSupplier(SupplierDTO supplierDTO){
        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierDTO.getSupplierName());
        supplier.setSupplierAddress(supplierDTO.getSupplierAddress());
        supplier.setSupplierEmail(supplierDTO.getSupplierEmail());
        supplier.setSupplierPhone(supplierDTO.getSupplierPhone());
        return supplier;
    }
}
