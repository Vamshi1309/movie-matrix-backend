package com.backend.movie_matrix.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.dto.AuthResponse;
import com.backend.movie_matrix.dto.LoginRequest;
import com.backend.movie_matrix.dto.RegisterRequest;
import com.backend.movie_matrix.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        String token = authService.register(request);

        ApiResponse<AuthResponse> response = new ApiResponse<AuthResponse>(
                true,
                "User Registred Successfully",
                new AuthResponse(token));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        String token = authService.login(request);
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                true,
                "Login successful",
                new AuthResponse(token));

        return ResponseEntity.ok(response);
    }

}