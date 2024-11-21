package com.internship.inventory.inventory_management_system.repository;

import com.internship.inventory.inventory_management_system.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
