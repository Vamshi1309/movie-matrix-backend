package com.backend.movie_matrix.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.dto.AuthResponse;
import com.backend.movie_matrix.dto.LoginRequest;
import com.backend.movie_matrix.dto.RegisterRequest;
import com.backend.movie_matrix.dto.UserProfileResponseDto;
import com.backend.movie_matrix.entity.User;
import com.backend.movie_matrix.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            String email = authService.getEmailFromToken(token);

            User user = authService.getUserByEmail(email);

            UserProfileResponseDto userProfile = UserProfileResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .mobileNumber(user.getMobileNumber())
                    .build();

            return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully", userProfile));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Invalid token", null));
        }
    }

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