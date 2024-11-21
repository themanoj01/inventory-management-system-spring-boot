package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.SupplierDTO;
import com.internship.inventory.inventory_management_system.exception.SupplierNotFoundException;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private Mapper mapper;
    @InjectMocks
    private SupplierServiceImpl supplierService;
    private Supplier supplier;
    private SupplierDTO supplierDTO;
    @BeforeEach
    void setUp() {
        supplier = new Supplier(1L, "Supplier Name", "Supplier Address", "supplier@example.com", "1234567890",null);
        supplierDTO = new SupplierDTO("Supplier Name", "Supplier Address", "supplier@example.com", "1234567890");

    }
    @Test
    void getAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(List.of(supplier));
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty());
        assertTrue(suppliers.contains(supplier));
        assertEquals(supplier.getSupplierName(), suppliers.get(0).getSupplierName());
    }

    @Test
    void getSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        Supplier returnedSupplier = supplierService.getSupplierById(1L);
        assertNotNull(returnedSupplier);
        assertEquals(supplier.getSupplierName(),returnedSupplier.getSupplierName());

        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());
        SupplierNotFoundException supplierNotFoundException = assertThrows(SupplierNotFoundException.class, () -> supplierService.getSupplierById(1L));
        assertEquals("Supplier not found with id 1", supplierNotFoundException.getMessage());
    }

    @Test
    void createSupplier() {
        when(mapper.mapToSupplier(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        Supplier createdSupplier = supplierService.createSupplier(supplierDTO);
        assertNotNull(createdSupplier);
        assertEquals(supplier.getSupplierName(), createdSupplier.getSupplierName());
    }

    @Test
    void updateSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        Supplier updatedSupplier = supplierService.updateSupplier(1L, supplierDTO);
        assertNotNull(updatedSupplier);
        assertEquals(supplier.getSupplierName(),updatedSupplier.getSupplierName());
    }

    @Test
    void deleteSupplier() {
        doNothing().when(supplierRepository).deleteById(1L);
        supplierService.deleteSupplier(1L);
        verify(supplierRepository, times(1)).deleteById(1L);

    }
}