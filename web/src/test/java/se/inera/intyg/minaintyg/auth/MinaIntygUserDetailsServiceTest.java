package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.PERSON_ID_ATTRIBUTE;

import java.util.Collections;
import java.util.List;
import org.opensaml.xml.XMLObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.schema.XSString;
import org.w3c.dom.Element;
import org.springframework.security.saml.SAMLCredential;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailsServiceTest {
    @Mock
    private SAMLCredential samlCredential;

    @Mock
    private Assertion assertion;

    @Mock
    private AttributeStatement attributeStatement;
    @Mock
    private Attribute attribute;
    @Mock
    private XMLObject xmlObject;

    @Mock
    private XSString xsString;

    @Mock
    private Element element;

    @InjectMocks
    private MinaIntygUserDetailsService minaIntygUserDetailsService;

    private static final String PERSONAL_NUMBER = "191212121212";

    @Test
    void shallThrowExceptionEmptyAttributeStatement() {
        when(samlCredential.getAuthenticationAssertion()).thenReturn(assertion);
        when(assertion.getAttributeStatements()).thenReturn(Collections.emptyList());

        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            minaIntygUserDetailsService.loadUserBySAML(samlCredential);
        });

        assertEquals("Could not extract attribute '" + PERSON_ID_ATTRIBUTE + "' from SAMLCredential.", exception.getMessage());
    }

    @Test
    void shallThrowExceptionCannotParseSAML2Response() {
        when(samlCredential.getAuthenticationAssertion()).thenReturn(assertion);
        when(assertion.getAttributeStatements()).thenReturn(List.of(attributeStatement));
        when(attributeStatement.getAttributes()).thenReturn(List.of(attribute));
        when(attribute.getName()).thenReturn(PERSON_ID_ATTRIBUTE);
        when(attribute.getAttributeValues()).thenReturn(List.of(xmlObject));

        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            minaIntygUserDetailsService.loadUserBySAML(samlCredential);
        });

        assertEquals("Cannot parse SAML2 response attribute '" + PERSON_ID_ATTRIBUTE + "', is not XSString or DOM is null",
            exception.getMessage());
    }

    @Test
    void shallLoadUserBySAMLFromValue() {
        when(samlCredential.getAuthenticationAssertion()).thenReturn(assertion);
        when(assertion.getAttributeStatements()).thenReturn(List.of(attributeStatement));
        when(attributeStatement.getAttributes()).thenReturn(List.of(attribute));
        when(attribute.getName()).thenReturn(PERSON_ID_ATTRIBUTE);
        when(attribute.getAttributeValues()).thenReturn(List.of(xsString));
        when(xsString.getValue()).thenReturn(PERSONAL_NUMBER);

        final var user = minaIntygUserDetailsService.loadUserBySAML(samlCredential);
        assertTrue(user instanceof MinaIntygUserImpl);
        assertEquals(PERSONAL_NUMBER, ((MinaIntygUserImpl) user).getPersonId());
    }

    @Test
    void shallLoadUserBySAMLFromTextContent() {
        when(samlCredential.getAuthenticationAssertion()).thenReturn(assertion);
        when(assertion.getAttributeStatements()).thenReturn(List.of(attributeStatement));
        when(attributeStatement.getAttributes()).thenReturn(List.of(attribute));
        when(attribute.getName()).thenReturn(PERSON_ID_ATTRIBUTE);
        when(attribute.getAttributeValues()).thenReturn(List.of(xmlObject));
        when(xmlObject.getDOM()).thenReturn(element);
        when(element.getTextContent()).thenReturn(PERSONAL_NUMBER);

        final var user = minaIntygUserDetailsService.loadUserBySAML(samlCredential);
        assertTrue(user instanceof MinaIntygUserImpl);
        assertEquals(PERSONAL_NUMBER, ((MinaIntygUserImpl) user).getPersonId());
    }
}