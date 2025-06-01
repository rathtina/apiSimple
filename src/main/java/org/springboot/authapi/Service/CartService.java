package org.springboot.authapi.Service;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.CartItemRepository;
import org.springboot.authapi.ResponseEnities.CartItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired CartItemRepository CartItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItemResponse> getCartItems(User user) {
        List<CartItem> items=cartItemRepository.findByUser(user);
        return items.stream().map(CartItemResponse::fromEntity).toList();
    }
}
