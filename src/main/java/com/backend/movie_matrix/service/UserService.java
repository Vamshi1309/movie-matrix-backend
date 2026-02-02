package com.backend.movie_matrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.movie_matrix.dto.UserProfileRequestDto;
import com.backend.movie_matrix.dto.UserProfileResponseDto;
import com.backend.movie_matrix.entity.User;
import com.backend.movie_matrix.exception.MobileNumberAlreadyExistsException;
import com.backend.movie_matrix.exception.UserNotFoundException;
import com.backend.movie_matrix.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepo userRepo;

    public UserProfileResponseDto getUserProfile() {
        User user = getCurrentUser();

        return UserProfileResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .build();
    }

    public UserProfileResponseDto updateUserProfile(UserProfileRequestDto request) {
        User user = getCurrentUser();

        if (!user.getMobileNumber().equals(request.getMobileNumber())) {
            if (userRepo.findByMobileNumber(request.getMobileNumber()).isPresent()) {
                throw new MobileNumberAlreadyExistsException("Mobile number already registered");
            }
        }

        user.setMobileNumber(request.getMobileNumber());
        user.setName(request.getName());

        User updatedUser = userRepo.save(user);

        return UserProfileResponseDto.builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .name(updatedUser.getName())
                .mobileNumber(updatedUser.getMobileNumber())
                .build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }
}
