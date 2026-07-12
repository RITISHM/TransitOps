package com.transitops.security;

import com.transitops.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "TransitOpsSecretKeyForJWTTokenSigningMustBeAtLeast256BitsLong2024";
    private final long testExpirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", testExpirationMs);
    }

    @Test
    void generateToken_shouldProduceValidJwt() {
        User user = User.builder()
                .id(1L)
                .email("driver@transitops.io")
                .role("DRIVER")
                .name("Driver John")
                .isActive(true)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("driver@transitops.io", jwtService.extractUsername(token));
        
        Integer userIdClaim = jwtService.extractClaim(token, claims -> claims.get("userId", Integer.class));
        String roleClaim = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        
        assertEquals(1, userIdClaim);
        assertEquals("DRIVER", roleClaim);
    }

    @Test
    void isTokenValid_shouldReturnTrueForCorrectUserAndNotExpired() {
        User user = User.builder()
                .id(2L)
                .email("manager@transitops.io")
                .role("FLEET_MANAGER")
                .name("Manager Bob")
                .isActive(true)
                .build();

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, "manager@transitops.io"));
        assertFalse(jwtService.isTokenValid(token, "other@transitops.io"));
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() {
        // Set short expiration
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", -1000L); // expired 1s ago

        User user = User.builder()
                .id(3L)
                .email("driver2@transitops.io")
                .role("DRIVER")
                .name("Driver Jane")
                .isActive(true)
                .build();

        String token = jwtService.generateToken(user);

        // Since it's expired, parsing it or validating it will throw an ExpiredJwtException or isTokenValid returns false
        assertThrows(Exception.class, () -> jwtService.isTokenValid(token, "driver2@transitops.io"));
    }
}
