package org.springboot.authapi.Repository;

import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUser(User user);
}
