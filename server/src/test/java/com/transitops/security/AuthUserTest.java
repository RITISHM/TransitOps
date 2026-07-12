package com.transitops.security;

import com.transitops.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AuthUserTest {

    @Test
    void authUser_shouldCorrectlyMapUserPropertiesToUserDetails() {
        User user = User.builder()
                .id(42L)
                .email("driver@transitops.io")
                .passwordHash("mysecretpasswordhash")
                .role("DRIVER")
                .isActive(true)
                .name("John Driver")
                .build();

        AuthUser authUser = new AuthUser(user);

        assertEquals(42L, authUser.getId());
        assertSame(user, authUser.getUser());
        assertEquals("driver@transitops.io", authUser.getUsername());
        assertEquals("mysecretpasswordhash", authUser.getPassword());
        
        assertTrue(authUser.isAccountNonExpired());
        assertTrue(authUser.isCredentialsNonExpired());
        
        // Active maps to accountNonLocked and enabled
        assertTrue(authUser.isAccountNonLocked());
        assertTrue(authUser.isEnabled());

        // Role checks
        assertTrue(authUser.hasRole("DRIVER"));
        assertFalse(authUser.hasRole("FLEET_MANAGER"));

        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_DRIVER", authorities.iterator().next().getAuthority());
    }

    @Test
    void authUser_shouldReflectInactiveUserStatus() {
        User user = User.builder()
                .id(43L)
                .email("inactive@transitops.io")
                .passwordHash("hash")
                .role("FLEET_MANAGER")
                .isActive(false)
                .build();

        AuthUser authUser = new AuthUser(user);

        assertFalse(authUser.isAccountNonLocked());
        assertFalse(authUser.isEnabled());
    }
}
