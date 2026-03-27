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
package se.inera.intyg.minaintyg.integration.intygsadmin.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DateUtilTest {

  @Nested
  class AfterOrEquals {

    @Test
    void shouldReturnTrueIfDateMatch() {
      assertTrue(DateUtil.afterOrEquals(LocalDateTime.now().plusSeconds(10)));
    }

    @Test
    void shouldReturnTrueIfDateIsAfter() {
      assertTrue(DateUtil.afterOrEquals(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void shouldReturnFalseIfDateIsBefore() {
      assertFalse(DateUtil.afterOrEquals(LocalDateTime.now().minusDays(1)));
    }
  }

  @Nested
  class BeforeOrEquals {

    @Test
    void shouldReturnTrueIfDateMatch() {
      assertTrue(DateUtil.beforeOrEquals(LocalDateTime.now()));
    }

    @Test
    void shouldReturnTrueIfDateIsBefore() {
      assertTrue(DateUtil.beforeOrEquals(LocalDateTime.now().minusDays(1)));
    }

    @Test
    void shouldReturnFalseIfDateIsAfter() {
      assertFalse(DateUtil.beforeOrEquals(LocalDateTime.now().plusDays(1)));
    }
  }
}
