package com.backend.movie_matrix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.movie_matrix.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber); 

    boolean existsByEmail(String email);
    
}
