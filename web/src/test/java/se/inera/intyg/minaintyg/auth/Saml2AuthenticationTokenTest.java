package se.inera.intyg.minaintyg.auth;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;

@ExtendWith(MockitoExtension.class)
class Saml2AuthenticationTokenTest {

    private static final String PRINCIPAL_NAME = "principalName";
    private static final String CORRECT_ATTRIBUTE_NAME = "Subject_SerialNumber";
    private static final String INVALID_ATTRIBUTE_NAME = "invalidAttributeName";
    private static final String ATTRIBUTE_VALUE = "191212121212";
    private static final String ATTRIBUTE_VALUE_NAME = "nameFromPU";
    @Mock
    private Saml2Authentication saml2Authentication;
    @InjectMocks
    private Saml2AuthenticationToken saml2AuthenticationToken;

    @Test
    void shouldThrowExceptionIfAttributeIsMissing() {
        when(saml2Authentication.isAuthenticated()).thenReturn(true);
        when(saml2Authentication.getPrincipal()).thenReturn(getAuthenticatedPrincipal(INVALID_ATTRIBUTE_NAME, INVALID_ATTRIBUTE_NAME));
        assertThrows(IllegalArgumentException.class, () -> saml2AuthenticationToken.getPrincipal());
    }

    @Test
    void shouldReturnNullIfNotAuthenticated() {
        when(saml2Authentication.isAuthenticated()).thenReturn(false);
        final var result = saml2AuthenticationToken.getPrincipal();
        assertNull(result);
    }

    @Test
    void shouldReturnPrincipalOfCorrectType() {
        when(saml2Authentication.isAuthenticated()).thenReturn(true);
        when(saml2Authentication.getPrincipal()).thenReturn(getAuthenticatedPrincipal(CORRECT_ATTRIBUTE_NAME, ATTRIBUTE_VALUE));
        final var result = saml2AuthenticationToken.getPrincipal();
        assertTrue(result instanceof MinaIntygUser);
    }

    @Test
    void shouldReturnPrincipalWithPersonId() {
        when(saml2Authentication.isAuthenticated()).thenReturn(true);
        when(saml2Authentication.getPrincipal()).thenReturn(getAuthenticatedPrincipal(CORRECT_ATTRIBUTE_NAME, ATTRIBUTE_VALUE));
        final var result = (MinaIntygUser) saml2AuthenticationToken.getPrincipal();
        assertEquals(ATTRIBUTE_VALUE, result.getPatientId());
    }

    @Test
    void shouldReturnPrincipalWithUsername() {
        when(saml2Authentication.isAuthenticated()).thenReturn(true);
        when(saml2Authentication.getPrincipal()).thenReturn(getAuthenticatedPrincipal(CORRECT_ATTRIBUTE_NAME, ATTRIBUTE_VALUE));
        final var result = (MinaIntygUser) saml2AuthenticationToken.getPrincipal();
        assertEquals(ATTRIBUTE_VALUE_NAME, result.getPatientName());
    }

    private static DefaultSaml2AuthenticatedPrincipal getAuthenticatedPrincipal(String attributeName, String attributeValue) {
        return new DefaultSaml2AuthenticatedPrincipal(PRINCIPAL_NAME, Map.of(attributeName, List.of(attributeValue)));
    }
}