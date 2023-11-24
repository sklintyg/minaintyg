package se.inera.intyg.minaintyg.integration.intygsadmin.util;

import java.time.LocalDateTime;

public final class DateUtil {

  private DateUtil() {
    throw new IllegalStateException("Utility class!");
  }

  public static boolean afterOrEquals(LocalDateTime dateTime) {
    return dateTime.isAfter(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now());
  }

  public static boolean beforeOrEquals(LocalDateTime dateTime) {
    return dateTime.isBefore(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now());
  }
}
