package com.pravin.SpringEcom.repository;

import com.pravin.SpringEcom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderId(String orderId);
}
