package com.transitops.security;

import com.transitops.domain.User;
import com.transitops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {
        User user = User.builder()
                .id(100L)
                .email("test@transitops.io")
                .passwordHash("hashedpassword")
                .role("FLEET_MANAGER")
                .isActive(true)
                .build();

        when(userRepository.findByEmail("test@transitops.io")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@transitops.io");

        assertNotNull(userDetails);
        assertEquals("test@transitops.io", userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FLEET_MANAGER")));
        
        verify(userRepository, times(1)).findByEmail("test@transitops.io");
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("unknown@transitops.io")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
                customUserDetailsService.loadUserByUsername("unknown@transitops.io"));

        verify(userRepository, times(1)).findByEmail("unknown@transitops.io");
    }
}
