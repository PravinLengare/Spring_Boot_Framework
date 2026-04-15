package com.ecommerce.project.Controller;

import com.ecommerce.project.Service.OrderService;
import com.ecommerce.project.Util.AuthUtil;
import com.ecommerce.project.payload.Orders.OrderDTO;
import com.ecommerce.project.payload.Orders.OrderRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> placeOrder(
            @Valid @PathVariable String paymentMethod,
            @Valid @RequestBody OrderRequestDTO orderRequestDTO){

        String emailId = authUtil.loggedInEmail();
        OrderDTO savedOrderDTO = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage(),
                orderRequestDTO.getPgPaymentId()
        );
        return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);

    }
}
