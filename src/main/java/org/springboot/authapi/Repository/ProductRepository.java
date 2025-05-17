package org.springboot.authapi.Repository;

import org.springboot.authapi.Enities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);
}
