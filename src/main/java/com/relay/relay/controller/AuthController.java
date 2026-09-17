package com.relay.relay.controller;

import com.relay.relay.dto.AuthResponseDTO;
import com.relay.relay.dto.LoginRequestDTO;
import com.relay.relay.dto.SignupRequestDTO;
import com.relay.relay.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody SignupRequestDTO request,
                                                  HttpServletResponse response) {
        AuthResponseDTO result = authService.signup(request);
        setJwtCookie(response, result.getToken());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
                                                 HttpServletResponse response) {
        AuthResponseDTO result = authService.login(request);
        setJwtCookie(response, result.getToken());
        return ResponseEntity.ok(result);
    }

    // Returns the currently authenticated user's info, used by frontend to check login status
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDTO> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(new AuthResponseDTO(null, email));
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}