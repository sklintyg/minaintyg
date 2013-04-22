package se.inera.certificate.schema.adapter;

import org.joda.time.LocalDate;

/**
 * Adapter for converting xs:date entities to LocalDate (Joda Time) and vice versa.
 *
 * @author andreaskaltenbach
 */
public final class LocalDateAdapter {

  private static final int TIME_ZONE_INDEX = 10;

  private LocalDateAdapter() {}

  public static LocalDate parseDate(String dateString) {
    if (dateString.length() > TIME_ZONE_INDEX) {
        return new LocalDate(dateString.substring(0, TIME_ZONE_INDEX));
    }
    else {
        return new LocalDate(dateString);
    }
  }

  public static String printDate(LocalDate date) {
    return date.toString();
  }
}
