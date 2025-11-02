package com.pravin.SpringEcom.service;

import com.pravin.SpringEcom.model.Order;
import com.pravin.SpringEcom.model.OrderItem;
import com.pravin.SpringEcom.model.Product;
import com.pravin.SpringEcom.model.dto.OrderRequest;
import com.pravin.SpringEcom.model.dto.OrderResponse;
import com.pravin.SpringEcom.repository.OrderRepo;
import com.pravin.SpringEcom.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepository; // Use the correct repository name

    @Autowired
    private ProductRepo productRepository; // Use the correct repository name

    /**
     * Places a new order, updates product stock, and saves it to the database.
     * The @Transactional annotation ensures that all these operations either
     * succeed together or fail together, preventing data inconsistencies.
     */
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        // 1. Set customer details from the incoming request
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setOrderDate(LocalDate.now());
        order.setStatus("PLACED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 2. Loop through each item in the order
        for (var itemRequest : orderRequest.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.productId()));

            // 3. Check for sufficient stock
            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // 4. Reduce the stock quantity and save the product
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

            // 5. Create and populate the OrderItem entity
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            orderItem.setOrder(order); // Link the item back to the main order

            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        // 6. Set the final total price and save the complete order
        order.setTotalPrice(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // 7. Return a simple response DTO to the frontend
        return new OrderResponse(savedOrder.getId(), savedOrder.getStatus());
    }

    /**
     * Fetches all orders from the database and maps them to OrderResponse DTOs.
     * This is used by the "Orders" page on your frontend.
     */
    public List<OrderResponse> getAllOrderResponses() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderResponse(order.getId(), order.getStatus()))
                .collect(Collectors.toList());
    }
}