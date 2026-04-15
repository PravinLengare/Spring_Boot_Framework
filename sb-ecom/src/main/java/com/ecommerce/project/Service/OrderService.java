package com.ecommerce.project.Service;

import com.ecommerce.project.payload.Orders.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgStatus, String pgResponseMessage, String pgPaymentId);
}
