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
