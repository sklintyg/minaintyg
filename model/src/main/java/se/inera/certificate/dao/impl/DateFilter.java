package se.inera.certificate.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.CertificateMetaData;

/**
 * Class that filter a collection of {@link CertificateMetaData} that has validity dated within a
 * specific date interval.
 *
 * @author parwenaker
 *
 */
class DateFilter {

    /** The collection to filter. */
    private final List<CertificateMetaData> data;

    /**
     * Constructor.
     *
     * @param data initial collection of {@link CertificateMetaData} to filter.
     */
    public DateFilter(List<CertificateMetaData> data) {
        this.data = new ArrayList<CertificateMetaData>(data);
    }

    /**
     * Filters the collection.
     *
     * @param fromDate first date in validity interval
     * @param toDate last date in validity interval
     *
     * @return filtered collection where validity interval in the {@link CertificateMetaData} is within the validity interval.
     */
    List<CertificateMetaData> filter(LocalDate fromDate, LocalDate toDate) {
        List<CertificateMetaData> data = new ArrayList<CertificateMetaData>(this.data);
        if (fromDate == null || toDate == null) {
            return data;
        }

        Iterator<CertificateMetaData> iter = data.iterator();
        while (iter.hasNext()) {
            CertificateMetaData meta = iter.next();
            if (not(isWithin(meta.getValidFromDate(), fromDate, toDate) || isWithin(meta.getValidToDate(), fromDate, toDate))) {
                iter.remove();
            }
        }
        return data;
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
    private boolean isWithin(LocalDate date, LocalDate from, LocalDate to) {
        return date.compareTo(from) >= 0 && date.compareTo(to) <= 0;
    }

    private boolean not(boolean cond) {
        return !cond;
    }
}
