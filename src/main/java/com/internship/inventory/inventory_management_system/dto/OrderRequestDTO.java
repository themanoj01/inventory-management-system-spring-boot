package com.internship.inventory.inventory_management_system.dto;

import com.internship.inventory.inventory_management_system.enums.OrderStatus;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @CsvDate("yyyy-MM-dd")
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private double orderTotal;
    private Long userId;
}
