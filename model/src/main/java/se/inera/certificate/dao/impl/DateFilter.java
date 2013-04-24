package se.inera.certificate.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.CertificateMetaData;

public class DateFilter {

    private final List<CertificateMetaData> data;

    public DateFilter(List<CertificateMetaData> data) {
        this.data = new ArrayList<CertificateMetaData>(data);
    }

    List<CertificateMetaData> filter(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return data;
        }
        Iterator<CertificateMetaData> iter = data.iterator();
        while (iter.hasNext()) {
            CertificateMetaData meta = iter.next();
            if (!(meta.getValidFromDate().compareTo(fromDate) >= 0 && meta.getValidFromDate().compareTo(toDate) <= 0
                    ||
                meta.getValidToDate().compareTo(fromDate) >= 0 && meta.getValidToDate().compareTo(toDate) <= 0)) {
                iter.remove();
            }
        }
        return data;
    }
}
