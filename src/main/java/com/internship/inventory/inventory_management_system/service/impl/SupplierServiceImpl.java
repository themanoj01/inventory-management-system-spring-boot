package com.internship.inventory.inventory_management_system.service.impl;

import com.internship.inventory.inventory_management_system.dto.Mapper;
import com.internship.inventory.inventory_management_system.dto.SupplierDTO;
import com.internship.inventory.inventory_management_system.exception.SupplierNotFoundException;
import com.internship.inventory.inventory_management_system.model.Supplier;
import com.internship.inventory.inventory_management_system.repository.SupplierRepository;
import com.internship.inventory.inventory_management_system.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final Mapper mapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, Mapper mapper) {
        this.supplierRepository = supplierRepository;
        this.mapper = mapper;
    }
    @Override
    public List<Supplier> getAllSuppliers() {
         return supplierRepository.findAll();
    }

    @Override
    public Supplier getSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id " + supplierId));
    }

    @Override
    public Supplier createSupplier(SupplierDTO supplier) {
        Supplier savedSupplier = mapper.mapToSupplier(supplier);
        return supplierRepository.save(savedSupplier);
    }

    @Override
    public Supplier updateSupplier(Long supplierId, SupplierDTO supplier) {
        Supplier existingSupplier = getSupplierById(supplierId);
        existingSupplier.setSupplierName(supplier.getSupplierName());
        existingSupplier.setSupplierAddress(supplier.getSupplierAddress());
        existingSupplier.setSupplierEmail(supplier.getSupplierEmail());
        existingSupplier.setSupplierPhone(supplier.getSupplierPhone());
        supplierRepository.save(existingSupplier);
        return existingSupplier;
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        supplierRepository.deleteById(supplierId);
    }
}
