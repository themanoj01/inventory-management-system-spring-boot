package com.internship.inventory.inventory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;
}
