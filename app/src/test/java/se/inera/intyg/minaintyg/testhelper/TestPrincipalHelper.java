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
package se.inera.intyg.minaintyg.testhelper;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

public class TestPrincipalHelper {

  public static void setMinaIntygUserAsPrincipal(final MinaIntygUser user) {
    Authentication auth =
        new AbstractAuthenticationToken(null) {
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

  public static void setUnknownPrincipal(final Object user) {
    Authentication auth =
        new AbstractAuthenticationToken(null) {
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
