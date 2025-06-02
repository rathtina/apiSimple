package org.springboot.authapi.Service;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.Product;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.CartItemRepository;
import org.springboot.authapi.Repository.ProductRepository;
import org.springboot.authapi.Repository.UserRepository;
import org.springboot.authapi.ResponseEnities.CartItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    public List<CartItemResponse> getCartItems(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems=cartItemRepository.findByUser(user);
        return cartItems.stream().map(CartItemResponse::fromEntity).toList();

    }

    public CartItemResponse addToCart(String email,Integer productId,Integer quantity) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem=cartItemRepository.findByUserAndProduct(user,product)
                .orElse(new CartItem(user, product, 0));

        cartItem.setQuantity(cartItem.getQuantity()+quantity);

        CartItem savedCartItem=cartItemRepository.save(cartItem);
        return CartItemResponse.fromEntity(savedCartItem);
    }

}


//Product product=productRepository.findById(productId)
//        .orElseThrow(() -> new RuntimeException("Product not found"));
//
//CartItem cartItem=cartItemRepository.findByUserAndProduct(user,product)
//        .orElse(new CartItem(user,product,0));
//
//        cartItem.setQuantity(cartItem.getQuantity()+quantity);
//CartItem saveItem=cartItemRepository.save(cartItem);
//        return
//        CartItemResponse.fromEntity(saveItem);

//List<CartItem> items=cartItemRepository.findByUser(user);
//        return items.stream().map(CartItemResponse::fromEntity).toList();