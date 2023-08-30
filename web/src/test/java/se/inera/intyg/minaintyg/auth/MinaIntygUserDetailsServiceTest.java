package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailsServiceTest {

    @InjectMocks
    private MinaIntygUserDetailsService minaIntygUserDetailsService;

    private final static String USERNAME = "username";

    @Test
    void shouldSetUsername() {
        final var result = minaIntygUserDetailsService.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, result.getUsername());
    }

    @Test
    void shouldSetRole() {
        final var expectedRole = Collections.singleton(new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));
        final var result = minaIntygUserDetailsService.loadUserByUsername(USERNAME);
        assertEquals(expectedRole, result.getAuthorities());
    }
}