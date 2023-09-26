package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.ELEG_PARTY_REGISTRATION_ID;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.UNKNOWN_PARTY_REGISTRATION_ID;

import org.junit.jupiter.api.Test;

class MinaIntygUserTest {

  @Test
  void shallReturnELEGAsRelyingPartyRegistrationIdIfLoginMethodIsELVA77() {
    assertEquals(ELEG_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder()
            .loginMethod(LoginMethod.ELVA77)
            .build()
            .getRelyingPartyRegistrationId()
    );
  }

  @Test
  void shallReturnUnknownAsRelyingPartyRegistrationIdIfLoginMethodIsFAKE() {
    assertEquals(UNKNOWN_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder()
            .loginMethod(LoginMethod.FAKE)
            .build()
            .getRelyingPartyRegistrationId()
    );
  }

  @Test
  void shallReturnUnknownAsRelyingPartyRegistrationIdIfLoginMethodIsNULL() {
    assertEquals(UNKNOWN_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder()
            .build()
            .getRelyingPartyRegistrationId()
    );
  }
}