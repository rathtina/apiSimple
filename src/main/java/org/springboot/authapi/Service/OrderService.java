package org.springboot.authapi.Service;

import org.springboot.authapi.Enities.Order;
import org.springboot.authapi.Enities.OrderItem;
import org.springboot.authapi.Enities.Product;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.OrderRepository;
import org.springboot.authapi.Repository.ProductRepository;
import org.springboot.authapi.Repository.UserRepository;
import org.springboot.authapi.Request.OrderRequestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    public Order placeOrder(String email, List<OrderRequestItem> requestItems) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order=new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems=new ArrayList<>();

        for (OrderRequestItem resquestItem:requestItems) {
            Product product=productRepository.findById(resquestItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock()<resquestItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            product.setStock(product.getStock()-resquestItem.getQuantity());

            OrderItem orderItem=new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(resquestItem.getQuantity());
            orderItem.setPrice(product.getPrice()*resquestItem.getQuantity());

            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByEmail(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }
}
