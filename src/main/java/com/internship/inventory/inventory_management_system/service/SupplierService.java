package com.internship.inventory.inventory_management_system.service;

import com.internship.inventory.inventory_management_system.dto.SupplierDTO;
import com.internship.inventory.inventory_management_system.model.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(Long supplierId);
    Supplier createSupplier(SupplierDTO supplier);
    Supplier updateSupplier(Long supplierId, SupplierDTO supplier);
    void deleteSupplier(Long supplierId);
}
