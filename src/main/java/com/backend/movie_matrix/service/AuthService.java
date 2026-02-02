package com.backend.movie_matrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.movie_matrix.dto.AuthResponse;
import com.backend.movie_matrix.dto.LoginRequest;
import com.backend.movie_matrix.dto.RegisterRequest;
import com.backend.movie_matrix.entity.User;
import com.backend.movie_matrix.exception.MobileNumberAlreadyExistsException;
import com.backend.movie_matrix.exception.UserAlreadyExistsException;
import com.backend.movie_matrix.exception.UserNotFoundException;
import com.backend.movie_matrix.repository.UserRepo;
import com.backend.movie_matrix.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final UserRepo repo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil JwtUtil;

    public String register(RegisterRequest request) {

        if (repo.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email Already Exists");
        }

        if (repo.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            throw new MobileNumberAlreadyExistsException("Mobile number already registered");
        }

        User user = User
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .build();

        repo.save(user);

        return login(new LoginRequest(
                request.getEmail(),
                request.getPassword()));

    }

    public String login(LoginRequest request) {
        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Please Register First"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(null);
        }

        return JwtUtil.generateToken(user.getEmail());
    }

    public String getEmailFromToken(String token) {
        return JwtUtil.extractEmail(token); 
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
