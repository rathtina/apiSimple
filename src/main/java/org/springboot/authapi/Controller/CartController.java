package org.springboot.authapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.CartItemRepository;
import org.springboot.authapi.Request.CartItemRequest;
import org.springboot.authapi.ResponseEnities.CartItemResponse;
import org.springboot.authapi.Service.CartService;
import org.springboot.authapi.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private JwtService jwtService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/listCart")
    public ResponseEntity<List<CartItemResponse>> getCartItems(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null && !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authorization.substring( 7);
        String email =jwtService.extractUsername(token);
        return ResponseEntity.ok(cartService.getCartItems(email));
    }

    @PostMapping("/addCart")
    public ResponseEntity<?> addToCart(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authorization.substring(7);
        String email = jwtService.extractUsername(token);
        try {
            CartItemResponse response = cartService.addToCart(email, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
