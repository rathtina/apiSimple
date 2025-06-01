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
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private JwtService jwtService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCartItems(user));
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal User user, @RequestBody CartItemRequest cartItemRequest) {
        CartItemResponse cartItemResponse=cartService.addToCart(user,cartItemRequest.getProductId(),cartItemRequest.getQuantity());
        return ResponseEntity.ok(Map.of("Message","Product Added Successfully","cartItem",cartItemResponse));
    }
}
