package org.springboot.authapi.Service;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.Product;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.CartItemRepository;
import org.springboot.authapi.Repository.ProductRepository;
import org.springboot.authapi.ResponseEnities.CartItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;

    public List<CartItemResponse> getCartItems(User user) {
        List<CartItem> items=cartItemRepository.findByUser(user);
        return items.stream().map(CartItemResponse::fromEntity).toList();
    }

    public CartItemResponse addToCart(User user,Integer productId,Integer quantity) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem=cartItemRepository.findByUserAndProduct(user,product)
                .orElse(new CartItem(user,product,0));

        cartItem.setQuantity(cartItem.getQuantity()+quantity);
        CartItem saveItem=cartItemRepository.save(cartItem);
        return CartItemResponse.fromEntity(saveItem);
    }

}
