package org.springboot.authapi.Repository;

import org.springboot.authapi.Enities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

} 

//---------------------------------------------2----------------------------------------------