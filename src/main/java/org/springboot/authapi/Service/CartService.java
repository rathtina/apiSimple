package org.springboot.authapi.Service;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.Product;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.CartItemRepository;
import org.springboot.authapi.Repository.ProductRepository;
import org.springboot.authapi.Repository.UserRepository;
import org.springboot.authapi.ResponseEnities.CartItemResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired private JwtService jwtService;

    @Autowired private ProductRepository productRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private CartItemRepository cartItemRepository;

    private User extractUserFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));
    }

    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(String authHeader) {
        User user = extractUserFromToken(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        List<CartItemResponseDTO> response = cartItems.stream()
                .map(CartItemResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    public String addToCart(String authHeader, Integer productId,Integer quantity){
        User user=extractUserFromToken(authHeader);
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem=cartItemRepository.findByUserAndProduct(user,product)
                        .orElse(new CartItem(null,user,product,0));

        cartItem.setQuantity(cartItem.getQuantity()+quantity);
        cartItemRepository.save(cartItem);
        return "Item Added to cart Successfully";
    }

}
