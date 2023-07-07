package com.example.newpetstore.repository;

import com.example.newpetstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
