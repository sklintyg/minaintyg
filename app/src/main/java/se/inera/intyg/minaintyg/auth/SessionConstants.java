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

public final class SessionConstants {

  public static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session";
  public static final String SESSION_STATUS_PING = "/ping";
  public static final String LAST_ACCESS_ATTRIBUTE = "LastAccess";
  public static final String SECONDS_UNTIL_EXPIRE = "SecondsUntilExpire";

  private SessionConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
