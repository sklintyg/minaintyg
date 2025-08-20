package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class AuthenticationEventListenerTest {

  private static final String PERSON_ID = "1912121212";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  @Mock
  private MonitoringLogService monitoringLogService;

  @InjectMocks
  private AuthenticationEventListener authenticationEventListener;

  @Nested
  class LoginSuccess {

    private InteractiveAuthenticationSuccessEvent interactiveAuthenticationSuccessEvent;

    @BeforeEach
    void setUp() {
      interactiveAuthenticationSuccessEvent = new InteractiveAuthenticationSuccessEvent(
          new Saml2AuthenticationToken(
              MinaIntygUser.builder()
                  .userId(PERSON_ID)
                  .loginMethod(LOGIN_METHOD)
                  .build(), mock(Saml2Authentication.class)
          ), this.getClass()
      );
    }

    @Test
    void shallLogPatientIdWhenSuccessfullyLogin() {
      final var stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

      authenticationEventListener.onLoginSuccess(interactiveAuthenticationSuccessEvent);

      verify(monitoringLogService).logUserLogin(stringArgumentCaptor.capture(), anyString());

      assertEquals(PERSON_ID, stringArgumentCaptor.getValue());
    }

    @Test
    void shallLogLoginMethodWhenSuccessfullyLogin() {
      final var stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

      authenticationEventListener.onLoginSuccess(interactiveAuthenticationSuccessEvent);

      verify(monitoringLogService).logUserLogin(anyString(), stringArgumentCaptor.capture());

      assertEquals(LOGIN_METHOD.value(), stringArgumentCaptor.getValue());
    }

    @Test
    void shallNotLogAnythingIfPrincipalIsNotOfCorrectType() {
      interactiveAuthenticationSuccessEvent = new InteractiveAuthenticationSuccessEvent(
          new Saml2AuthenticationToken(
              mock(Saml2AuthenticatedPrincipal.class), mock(Saml2Authentication.class)
          ), this.getClass()
      );

      authenticationEventListener.onLoginSuccess(interactiveAuthenticationSuccessEvent);

      verifyNoInteractions(monitoringLogService);
    }
  }

  @Nested
  class LogoutSuccess {

    private LogoutSuccessEvent logoutSuccessEvent;

    @BeforeEach
    void setUp() {
      logoutSuccessEvent = new LogoutSuccessEvent(
          new Saml2AuthenticationToken(
              MinaIntygUser.builder()
                  .userId(PERSON_ID)
                  .loginMethod(LOGIN_METHOD)
                  .build(), mock(Saml2Authentication.class)
          )
      );
    }

    @Test
    void shallLogPatientIdWhenSuccessfullyLogout() {
      final var stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

      authenticationEventListener.onLogoutSuccess(logoutSuccessEvent);

      verify(monitoringLogService).logUserLogout(stringArgumentCaptor.capture(), anyString());

      assertEquals(PERSON_ID, stringArgumentCaptor.getValue());
    }

    @Test
    void shallLogLoginMethodWhenSuccessfullyLogout() {
      final var stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

      authenticationEventListener.onLogoutSuccess(logoutSuccessEvent);

      verify(monitoringLogService).logUserLogout(anyString(), stringArgumentCaptor.capture());

      assertEquals(LOGIN_METHOD.value(), stringArgumentCaptor.getValue());
    }

    @Test
    void shallNotLogAnythingIfPrincipalIsNotOfCorrectType() {
      logoutSuccessEvent = new LogoutSuccessEvent(
          new Saml2AuthenticationToken(
              mock(Saml2AuthenticatedPrincipal.class), mock(Saml2Authentication.class)
          )
      );

      authenticationEventListener.onLogoutSuccess(logoutSuccessEvent);

      verifyNoInteractions(monitoringLogService);
    }
  }
}