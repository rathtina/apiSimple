package org.springboot.authapi.Controller;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.ResponseEnities.CartItemResponseDTO;
import org.springboot.authapi.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping("/getCart")
    public ResponseEntity<List<CartItemResponseDTO>> getCart(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cartService.getCartItems(authHeader));
    }

    @PostMapping("/addCart")
    public ResponseEntity<?> addToCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Integer productId,
            @RequestParam Integer quantity
    ) {
        String message = cartService.addToCart(authHeader, productId, quantity);
        return ResponseEntity.ok(message);
    }
}
