package com.transitops.controller;

import com.transitops.domain.User;
import com.transitops.dto.LoginRequestDTO;
import com.transitops.dto.LoginResponseDTO;
import com.transitops.security.AuthUser;
import com.transitops.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication and authorization requests.
 * Exposes endpoints for user login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest DTO containing the user's email and password
     * @return ResponseEntity containing the JWT token and user details on success
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = authUser.getUser();
        String jwtToken = jwtService.generateToken(user);

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Constructs a new AuthController with the required dependencies.
     *
     * @param authenticationManager The Spring Security authentication manager
     * @param jwtService The service for generating and validating JWT tokens
     */
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
}
