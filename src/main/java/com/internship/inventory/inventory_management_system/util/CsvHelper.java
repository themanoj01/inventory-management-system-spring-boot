package com.internship.inventory.inventory_management_system.util;

import com.internship.inventory.inventory_management_system.dto.*;
import com.internship.inventory.inventory_management_system.exception.*;
import com.internship.inventory.inventory_management_system.model.Orders;
import com.internship.inventory.inventory_management_system.model.Product;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.model.User;
import com.internship.inventory.inventory_management_system.repository.OrderRepository;
import com.internship.inventory.inventory_management_system.repository.ProductRepository;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import com.internship.inventory.inventory_management_system.repository.UserRepository;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CsvHelper {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    public List<ProductRequestDTO> importProductsFromCsv(MultipartFile file){
        List<ProductRequestDTO> products = new ArrayList<>();
        try{
            Reader reader = new InputStreamReader(file.getInputStream());
            String[] columns = {"productName","description","price","quantity","supplierId"};
            ColumnPositionMappingStrategy<ProductRequestDTO> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(ProductRequestDTO.class);
            strategy.setColumnMapping(columns);
            CsvToBean<ProductRequestDTO> csvToBean = new CsvToBeanBuilder<ProductRequestDTO>(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<ProductRequestDTO> parsedProducts = csvToBean.parse();
            System.out.println("Parsed Products:" + parsedProducts.size());
            parsedProducts.forEach(product -> {
                System.out.println("Parsed Product: " + product.getProductName() +
                        ", Supplier ID: " + product.getSupplierId() +
                        ", Description: " + product.getDescription() +
                        ", Price: " + product.getPrice() +
                        ", Quantity: " + product.getQuantity());
            });

            List<Long> supplierIds = parsedProducts.stream()
                    .map(ProductRequestDTO::getSupplierId)
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println("Supplier IDs:" + supplierIds);
            Map<Long, Supplier> suppliers = supplierRepository.findAllById(supplierIds).stream()
                    .collect(Collectors.toMap(Supplier::getSupplierId, supplier -> supplier));
            for(ProductRequestDTO product : parsedProducts){
                Long supplierId = product.getSupplierId();
                Supplier supplier = suppliers.get(supplierId);
                if(supplier == null){
                    throw new SupplierNotFoundException("Supplier not found with id" + supplierId);

                }
                if(file.isEmpty()){
                    throw new CsvImportException("File is empty");
                }
                Product productToSave = mapper.mapToProduct(product);
                productRepository.save(productToSave);
                products.add(product);
            }
            reader.close();
        } catch (Exception e) {
            throw new CsvImportException("Failed to import data from CSV");
        }
        return products;
    }

    public void exportProductsToCsv(PrintWriter writer, List<ProductResponseDTO> products){
        try{
            ColumnPositionMappingStrategy<ProductResponseDTO> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(ProductResponseDTO.class);

            String[] columns = {"productId","productName","description","price","quantity","supplierId"};
            strategy.setColumnMapping(columns);
            StatefulBeanToCsv<ProductResponseDTO> beanToCsv = new StatefulBeanToCsvBuilder<ProductResponseDTO>(writer)
                    .withMappingStrategy(strategy)
                    .withSeparator(',')
                    .withQuotechar('\"')
                    .withOrderedResults(true)
                    .build();
            beanToCsv.write(products);
            writer.close();
        }
        catch(CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e){
            throw new CsvExportException("Failed to export data to CSV");
        }
    }
    // Import orders from csv
    public List<OrderRequestDTO> importOrdersFromCsv(MultipartFile file){
        List<OrderRequestDTO> orders = new ArrayList<>();
        try{
            Reader reader = new InputStreamReader(file.getInputStream());
            String[] columnsName = {"orderDate","orderStatus","orderTotal","userId"};
            ColumnPositionMappingStrategy<OrderRequestDTO> mappingStrategy= new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(OrderRequestDTO.class);
            mappingStrategy.setColumnMapping(columnsName);
            CsvToBean<OrderRequestDTO> csvToBean = new CsvToBeanBuilder<OrderRequestDTO>(reader)
                    .withMappingStrategy(mappingStrategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<OrderRequestDTO> parsedOrders = csvToBean.parse();
            for(OrderRequestDTO order : parsedOrders){
                if(file.isEmpty()){
                    throw new CsvImportException("File is empty");
                }
                validateOrderRequest(parsedOrders);
                Orders orderToSave = mapper.mapToOrder(order);
                orderRepository.save(orderToSave);
                orders.add(order);
            }
            reader.close();
        } catch (Exception e) {
            throw new CsvImportException("Failed to import data from CSV: " + e.getMessage());
        }
        return orders;
    }

    private void validateOrderRequest(List<OrderRequestDTO> orderRequestDTO){
        List<Long> userIds = orderRequestDTO.stream()
                .map(OrderRequestDTO::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, User> users = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, user -> user));
        for(OrderRequestDTO order : orderRequestDTO){
            Long userId = order.getUserId();
            User user = users.get(userId);
            if(user == null){
                throw new UserNotFoundException("User not found with id " + userId);
            }
        }
    }
    //Export orders data as csv file
    public void exportOrdersToCsv(PrintWriter writer, List<OrderResponseDTO> orders){
        try{
            ColumnPositionMappingStrategy<OrderResponseDTO> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(OrderResponseDTO.class);
            String[] columnsName = {"orderId","orderDate","orderStatus","orderTotal","userId"};
            strategy.setColumnMapping(columnsName);
            StatefulBeanToCsv<OrderResponseDTO> beanToCsv = new StatefulBeanToCsvBuilder<OrderResponseDTO>(writer)
                    .withMappingStrategy(strategy)
                    .withSeparator(',')
                    .withQuotechar('\"')
                    .withOrderedResults(true)
                    .build();
            beanToCsv.write(orders);
            writer.close();
        }
        catch(CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e){
            throw new CsvExportException("Failed to export data to CSV");
        }
    }
}
