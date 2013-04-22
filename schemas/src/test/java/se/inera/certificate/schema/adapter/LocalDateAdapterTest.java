package se.inera.certificate.schema.adapter;

import org.joda.time.LocalDate;
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

    @Test
    public void testParseDateTimeWithTimeZoneInformation() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_TIME_STRING_WITH_TIME_ZONE);
        assertEquals(DATE, date);
    }

    @Test
    public void testParseDateTime() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_TIME_STRING);
        assertEquals(DATE, date);
    }

    @Test
    public void testParseDateWithoutTimeInformation() {
        LocalDate date = LocalDateAdapter.parseDate(DATE_STRING);
        assertEquals(DATE, date);
    }

    @Test
    public void testPrintDate() {
        String dateString = LocalDateAdapter.printDate(DATE);
        assertEquals(DATE_STRING, dateString);
    }
}
