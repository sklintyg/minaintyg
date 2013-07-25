package se.inera.certificate.schema.adapter;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Test;

/**
 * @author andreaskaltenbach
 */
public class PartialAdapterTest {

    private static final String YEAR = "2013";
    private static final String YEAR_MONTH = "2013-12";
    private static final String YEAR_MONTH_DAY = "2013-12-24";

    private static final Partial YEAR_PARTIAL = new Partial(DateTimeFieldType.year(), 2013);
    private static final Partial YEAR_MONTH_PARTIAL = YEAR_PARTIAL.with(DateTimeFieldType.monthOfYear(), 12);
    private static final Partial YEAR_MONTH_DAY_PARTIAL = YEAR_MONTH_PARTIAL.with(DateTimeFieldType.dayOfMonth(), 24);

    @Test
    public void testParseYear() {
        Partial partial = PartialAdapter.parsePartial(YEAR);
        assertEquals(YEAR_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonth() {
        Partial partial = PartialAdapter.parsePartial(YEAR_MONTH);
        assertEquals(YEAR_MONTH_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonthDay() {
        Partial partial = PartialAdapter.parsePartial(YEAR_MONTH_DAY);
        assertEquals(YEAR_MONTH_DAY_PARTIAL, partial);
    }

    @Test
    public void testPrintYear() {
        String date = PartialAdapter.printPartial(YEAR_PARTIAL);
        assertEquals(YEAR, date);
    }

    @Test
    public void testPrintYearMonth() {
        String date = PartialAdapter.printPartial(YEAR_MONTH_PARTIAL);
        assertEquals(YEAR_MONTH, date);
    }

    @Test
    public void testPrintYearMonthDay() {
        String date = PartialAdapter.printPartial(YEAR_MONTH_DAY_PARTIAL);
        assertEquals(YEAR_MONTH_DAY, date);
    }
}
