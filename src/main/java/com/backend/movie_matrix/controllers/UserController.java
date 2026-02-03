package com.backend.movie_matrix.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.dto.UserProfileRequestDto;
import com.backend.movie_matrix.dto.UserProfileResponseDto;
import com.backend.movie_matrix.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        UserProfileResponseDto user = userService.getUserProfile();

        ApiResponse<UserProfileResponseDto> response = new ApiResponse<>(true, "Profile fetched successfully", user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileRequestDto request) {
        UserProfileResponseDto updatedProfile = userService.updateUserProfile(request);

        ApiResponse<UserProfileResponseDto> response = new ApiResponse<>(
                true,
                "Profile updated successfully",
                updatedProfile);

        return ResponseEntity.ok(response);
    }
}