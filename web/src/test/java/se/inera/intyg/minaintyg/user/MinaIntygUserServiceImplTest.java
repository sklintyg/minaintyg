package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

class MinaIntygUserServiceImplTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";

  private final MinaIntygUserServiceImpl minaIntygUserService = new MinaIntygUserServiceImpl();

  @Test
  void shouldReturnUserFromPrincipal() {
    final var expectedUser = new MinaIntygUser(PERSON_ID, PERSON_NAME);
    setUserAsPrincipal(expectedUser);
    final var actualUser = minaIntygUserService.getUser();
    assertEquals(expectedUser, actualUser);
  }

  private void setUserAsPrincipal(final MinaIntygUser user) {
    Authentication auth = new AbstractAuthenticationToken(null) {
      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return user;
      }
    };
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}