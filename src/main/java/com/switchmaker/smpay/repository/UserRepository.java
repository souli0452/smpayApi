package com.switchmaker.smpay.repository;

import com.switchmaker.smpay.entities.Customer;
import com.switchmaker.smpay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

    List<User> findByCustomer(Customer customer);

  User findByEmail(String email);

  //  List<User> findByIdAccountType();
}


