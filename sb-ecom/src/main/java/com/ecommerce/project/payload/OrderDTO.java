package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private LocalDate orderDate;
    private String orderStatus;
    private Double totalAmount;
    private PaymentDTO payment;
    private List<OrderItemDTO> orderItems;
    private Long addressId;
}
