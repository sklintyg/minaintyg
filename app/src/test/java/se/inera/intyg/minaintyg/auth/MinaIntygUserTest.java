/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.ELEG_PARTY_REGISTRATION_ID;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.UNKNOWN_PARTY_REGISTRATION_ID;

import org.junit.jupiter.api.Test;

class MinaIntygUserTest {

  @Test
  void shallReturnELEGAsRelyingPartyRegistrationIdIfLoginMethodIsELVA77() {
    assertEquals(
        ELEG_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder()
            .loginMethod(LoginMethod.ELVA77)
            .build()
            .getRelyingPartyRegistrationId());
  }

  @Test
  void shallReturnUnknownAsRelyingPartyRegistrationIdIfLoginMethodIsFAKE() {
    assertEquals(
        UNKNOWN_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder()
            .loginMethod(LoginMethod.FAKE)
            .build()
            .getRelyingPartyRegistrationId());
  }

  @Test
  void shallReturnUnknownAsRelyingPartyRegistrationIdIfLoginMethodIsNULL() {
    assertEquals(
        UNKNOWN_PARTY_REGISTRATION_ID,
        MinaIntygUser.builder().build().getRelyingPartyRegistrationId());
  }
}
