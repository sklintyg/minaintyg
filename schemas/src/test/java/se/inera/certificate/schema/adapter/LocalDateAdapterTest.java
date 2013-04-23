package se.inera.certificate.schema.adapter;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author andreaskaltenbach
 */
public final class LocalDateAdapterTest {

    private static final String DATE_TIME_STRING_WITH_TIME_ZONE = "2012-11-13T13:55:50.844+01:00";
    private static final String DATE_TIME_STRING = "2012-11-13T13:55:50.844";
    private static final String DATE_STRING = "2012-11-13";

    private static final LocalDate DATE = new LocalDate(2012, 11, 13);
    private static final LocalDateTime DATE_TIME = new LocalDateTime(2012, 11, 13, 13, 55, 50, 844);
    private static final LocalDateTime DATE_TIME_START_OF_DAY_STRING = new LocalDateTime(2012, 11, 13, 0, 0, 0);

    @Test
    public void testParseDateTimeWithTimeZoneInformation() {
        LocalDateTime date = LocalDateAdapter.parseDateTime(DATE_TIME_STRING_WITH_TIME_ZONE);
        assertEquals(DATE_TIME, date);
    }

    @Test
    public void testParseDateWithTimeZoneInformation() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_TIME_STRING_WITH_TIME_ZONE);
        assertEquals(DATE, date);
    }

    @Test
    public void testParseDateWithDate() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_STRING);
        assertEquals(DATE, date);
    }

    @Test
    public void testParseDateTimeWithDate() {
        LocalDateTime date = LocalDateAdapter.parseDateTime(DATE_STRING);
        assertEquals(DATE_TIME_START_OF_DAY_STRING, date);
    }

    @Test
    public void testParseDateWithDateTime() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_TIME_STRING);
        assertEquals(DATE, date);
    }

    @Test
    public void testParseDateTimeWithDateTime() {
        LocalDateTime date = LocalDateAdapter.parseDateTime(DATE_TIME_STRING);
        assertEquals(DATE_TIME, date);
    }

    @Test
    public void testPrintDate() {
        String dateString = LocalDateAdapter.printDate(DATE);
        assertEquals(DATE_STRING, dateString);
    }

    @Test
    public void testPrintDateTime() {
        String dateString = LocalDateAdapter.printDateTime(DATE_TIME);
        assertEquals(DATE_TIME_STRING, dateString);
    }
}
