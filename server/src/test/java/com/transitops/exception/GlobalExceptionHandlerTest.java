package com.transitops.exception;

import com.transitops.security.CustomUserDetailsService;
import com.transitops.security.JwtAuthFilter;
import com.transitops.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {GlobalExceptionHandlerTest.TestController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @RestController
    static class TestController {
        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new ResourceNotFoundException("Resource was not found");
        }

        @GetMapping("/test/unauthorized")
        public void throwUnauthorized() {
            throw new BadCredentialsException("Invalid password");
        }

        @GetMapping("/test/forbidden")
        public void throwForbidden() {
            throw new AccessDeniedException("Forbidden action");
        }

        @GetMapping("/test/generic")
        public void throwGeneric() {
            throw new RuntimeException("Internal database error");
        }
    }

    @Test
    void handleResourceNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource was not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleAuthenticationException_shouldReturn401() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Authentication failed: Invalid password"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleAccessDeniedException_shouldReturn403() throws Exception {
        mockMvc.perform(get("/test/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Access denied: Forbidden action"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleGlobalException_shouldReturn500() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Internal database error"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
