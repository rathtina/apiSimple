package org.springboot.authapi.Controller;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cartService.getCartItems(authHeader));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam int productId,
            @RequestParam Integer quantity
    ) {
        String message = cartService.addToCart(authHeader, productId, quantity);
        return ResponseEntity.ok(message);
    }
}
