package se.inera.certificate.schema.adapter;

import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.monthOfYear;
import static org.joda.time.DateTimeFieldType.year;
import static org.joda.time.format.ISODateTimeFormat.yearMonth;
import static org.joda.time.format.ISODateTimeFormat.yearMonthDay;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author andreaskaltenbach
 */
public class PartialAdapter {

    /**
     * Converts an intyg:common-model:1:partialDate to a Joda Partial.
     */
    public static Partial parsePartial(String dateString) {

        LocalDate localDate = new LocalDate(dateString);

        switch (dateString.length()) {
            case 4: // only year provided
                return new Partial(year(), localDate.get(year()));
            case 7: // year and month provided
                return new Partial(new DateTimeFieldType[]{year(), monthOfYear()},
                        new int[]{localDate.get(year()), localDate.get(monthOfYear())});
            default:
                return new Partial(new DateTimeFieldType[]{year(), monthOfYear(), dayOfMonth()},
                        new int[]{localDate.get(year()), localDate.get(monthOfYear()), localDate.get(dayOfMonth())});
        }
    }

    /**
     * Converts a Joda Partial to an intyg:common-model:1:partialDate.
     */
    public static String printPartial(Partial partial) {

        if (!partial.isSupported(dayOfMonth()) && !partial.isSupported(monthOfYear())) {
            return partial.toString(ISODateTimeFormat.year());
        }

        if (!partial.isSupported(dayOfMonth())) {
            return partial.toString(yearMonth());
        }

        return partial.toString(yearMonthDay());
    }
}
