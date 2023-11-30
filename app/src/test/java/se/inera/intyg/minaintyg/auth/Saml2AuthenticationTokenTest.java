package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;

@ExtendWith(MockitoExtension.class)
class Saml2AuthenticationTokenTest {

  @Mock
  private MinaIntygUser minaIntygUser;

  @Mock
  private Saml2Authentication saml2Authentication;

  @Test
  void shallReturnNameFromAuthentication() {
    final var expected = "name";

    doReturn(expected).when(saml2Authentication).getName();

    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);

    assertEquals(expected, token.getName());
  }

  @Test
  void shallReturnPrincipleFromAuthentication() {
    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    assertEquals(minaIntygUser, token.getPrincipal());
  }

  @Test
  void shallReturnCredentialsFromAuthentication() {
    final var expected = "Credentials";

    doReturn(expected).when(saml2Authentication).getCredentials();

    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    assertEquals(expected, token.getCredentials());
  }

  @Test
  void shallReturnTrueWhenEquals() {
    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    final var tokenTwo = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    assertTrue(token.equals(tokenTwo));
  }

  @Test
  void shallReturnFalseWhenNotEquals() {
    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    final var tokenTwo = new Saml2AuthenticationToken(mock(MinaIntygUser.class),
        saml2Authentication);
    assertFalse(token.equals(tokenTwo));
  }

  @Test
  void shallReturnSameHashCodesWhenEqual() {
    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    final var tokenTwo = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    assertEquals(token.hashCode(), tokenTwo.hashCode());
  }

  @Test
  void shallReturnDifferentHashCodesWhenNotEqual() {
    final var token = new Saml2AuthenticationToken(minaIntygUser, saml2Authentication);
    final var tokenTwo = new Saml2AuthenticationToken(mock(MinaIntygUser.class),
        saml2Authentication);
    assertNotEquals(token.hashCode(), tokenTwo.hashCode());
  }
}