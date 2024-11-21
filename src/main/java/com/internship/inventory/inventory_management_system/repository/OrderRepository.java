package com.internship.inventory.inventory_management_system.repository;

import com.internship.inventory.inventory_management_system.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT * FROM Orders o WHERE o.order_status='PENDING'", nativeQuery = true)
    List<Orders> findPendingOrders();
}
