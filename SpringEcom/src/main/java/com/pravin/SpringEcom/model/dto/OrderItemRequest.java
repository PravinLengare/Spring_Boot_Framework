package com.pravin.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {}
