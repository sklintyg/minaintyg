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
package se.inera.certificate.model.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.Partial;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.schema.adapter.PartialAdapter;

/**
 * Class that filter a collection of {@link Certificate} that has validity dated within a
 * specific date interval.
 *
 * @author parwenaker
 *
 */
class DateFilter {

    /** The collection to filter. */
    private final List<Certificate> data;

    /**
     * Constructor.
     *
     * @param data initial collection of {@link Certificate} to filter.
     */
    public DateFilter(List<Certificate> data) {
        this.data = new ArrayList<Certificate>(data);
    }

    /**
     * Filters the collection.
     *
     * @param fromDate first date in validity interval
     * @param toDate last date in validity interval
     *
     * @return filtered collection where validity interval in the {@link Certificate} is within the validity interval.
     */
    List<Certificate> filter(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return Collections.unmodifiableList(data);
        }

        List<Certificate> filteredData = new ArrayList<Certificate>(this.data.size());
        for (Certificate meta: data) {
            if (isWithin(PartialAdapter.parsePartial(meta.getValidFromDate()), fromDate, toDate) || isWithin(PartialAdapter.parsePartial(meta.getValidToDate()), fromDate, toDate)) {
                filteredData.add(meta);
            }
        }
        return filteredData;
    }

    /**
     * Checks if date is within interval (from, to).
     *
     * @param date date to check
     * @param from first date in interval
     * @param to last date in interval
     *
     * @return true if date is within interval
     */
    private boolean isWithin(Partial date, LocalDate from, LocalDate to) {
        return date.compareTo(from) >= 0 && date.compareTo(to) <= 0;
    }
}
