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
package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;

class UserToolkitTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  @Test
  void shouldReturnOptionalEmptyIfAuthenticationIsNull() {
    TestPrincipalHelper.setUnknownPrincipal(null);
    final var userFromPrincipal = UserToolkit.getUserFromPrincipal();
    assertNull(userFromPrincipal.orElse(null));
  }

  @Test
  void shouldReturnOptionalEmptyPrincipalIsNotInstanceOfMinaIntygUser() {
    final var expectedResult =
        MinaIntygUser.builder()
            .personId(PERSON_ID)
            .personName(PERSON_NAME)
            .loginMethod(LOGIN_METHOD)
            .build();
    TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedResult);
    final var userFromPrincipal = UserToolkit.getUserFromPrincipal();
    assertEquals(expectedResult, userFromPrincipal.orElse(null));
  }
}
