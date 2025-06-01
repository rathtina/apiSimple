package org.springboot.authapi.Repository;

import org.springboot.authapi.Enities.Order;
import org.springboot.authapi.Enities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUser(User user);
}
