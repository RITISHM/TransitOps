package com.transitops.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transitops.domain.User;
import com.transitops.dto.LoginRequestDTO;
import com.transitops.security.AuthUser;
import com.transitops.security.CustomUserDetailsService;
import com.transitops.security.JwtAuthFilter;
import com.transitops.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void login_shouldReturnTokenAndUserInfoWhenCredentialsAreValid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("driver@transitops.io");
        request.setPassword("Passw0rd!");

        User user = User.builder()
                .id(5L)
                .email("driver@transitops.io")
                .role("DRIVER")
                .isActive(true)
                .build();

        AuthUser authUser = new AuthUser(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(authUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.email").value("driver@transitops.io"))
                .andExpect(jsonPath("$.role").value("DRIVER"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void login_shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("driver@transitops.io");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication failed: Bad credentials"))
                .andExpect(jsonPath("$.status").value(401));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldReturnBadRequestWhenEmailOrPasswordIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(""); // blank email
        request.setPassword(""); // blank password

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_shouldReturnBadRequestWhenEmailFormatIsInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("invalid-email-format");
        request.setPassword("somepassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"));

        verify(authenticationManager, never()).authenticate(any());
    }
}
