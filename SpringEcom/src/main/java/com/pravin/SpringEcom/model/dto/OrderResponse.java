package com.pravin.SpringEcom.model.dto;

// ✅ Create this file with this content.
public record OrderResponse(
        Long id,
        String status
) {
    public OrderResponse(Long id, String status) {
        this.id = id;
        this.status = status;
    }
}