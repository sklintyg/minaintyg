/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.schema.adapter;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Adapter for converting XML Schema types to Java dates and vice versa.
 *
 * @author andreaskaltenbach
 */
public final class LocalDateAdapter {

    private static final int DATE_END_INDEX = 10;
    private static final int TIME_END_INDEX = 23;

    private LocalDateAdapter() {
    }


    /**
     *  Converts an xs:date to a Joda Time LocalDate.
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString.length() > DATE_END_INDEX) {
            return new LocalDate(dateString.substring(0, DATE_END_INDEX));
        } else {
            return new LocalDate(dateString);
        }
    }

    /**
     *  Converts an xs:datetime to a Joda Time LocalDateTime.
     */
    public static LocalDateTime parseDateTime(String dateString) {
        if (dateString.length() > TIME_END_INDEX) {
            return new LocalDateTime(dateString.substring(0, TIME_END_INDEX));
        } else {
            return new LocalDateTime(dateString);
        }
    }

    /**
     * Converts a Joda Time LocalDateTime to an xs:datetime.
     */
    public static String printDateTime(LocalDateTime dateTime) {
        return dateTime.toString();
    }

    /**
     * Converts a Joda Time LocalDate to an xs:date.
     */
    public static String printDate(LocalDate date) {
        return date.toString();
    }
}
