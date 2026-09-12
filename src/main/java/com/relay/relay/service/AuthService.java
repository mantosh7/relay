package com.relay.relay.service;

import com.relay.relay.dto.AuthResponseDTO;
import com.relay.relay.dto.LoginRequestDTO;
import com.relay.relay.dto.SignupRequestDTO;
import com.relay.relay.entity.User;
import com.relay.relay.repository.UserRepository;
import com.relay.relay.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO signup(SignupRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        // Store the encoded password instead of the user's raw password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("LOCAL");

        userRepository.save(user);

        // Generate a JWT after successfully creating the user
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getEmail());
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // Spring Security validates the provided credentials against the configured UserDetailsService
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponseDTO(token, request.getEmail());
    }
}