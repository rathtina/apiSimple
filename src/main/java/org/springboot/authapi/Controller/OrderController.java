package org.springboot.authapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springboot.authapi.Enities.Order;
import org.springboot.authapi.Request.OrderRequestItem;
import org.springboot.authapi.Service.JwtService;
import org.springboot.authapi.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private JwtService jwtService;


    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody List<OrderRequestItem> items,
                                            HttpServletRequest request) {
        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Order order = orderService.placeOrder(email, items);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        List<Order> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }
}
