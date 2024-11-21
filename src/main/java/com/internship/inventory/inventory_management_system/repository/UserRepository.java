package com.internship.inventory.inventory_management_system.repository;

import com.internship.inventory.inventory_management_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String username);
}