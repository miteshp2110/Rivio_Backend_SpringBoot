package com.cts.rivio.modules.auth.service;

import com.cts.rivio.modules.auth.dto.PasswordResetRequest;
import com.cts.rivio.modules.auth.entity.User;
import com.cts.rivio.modules.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void resetPassword(Integer userId, PasswordResetRequest request) { // Changed Long to Integer
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String hashedSafePassword = passwordEncoder.encode(request.newPassword());
        user.setPasswordHash(hashedSafePassword);

        userRepository.save(user);

        // Fix: Use getEmail() because your entity has 'email', not 'username'
        System.out.println("Notification: Password reset for user " + user.getEmail());
    }
}