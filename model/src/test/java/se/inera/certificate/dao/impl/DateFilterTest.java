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
package se.inera.certificate.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

public class DateFilterTest {

    @Test
    public void testFilterWithDates() throws Exception {
        List<CertificateMetaData> data = Arrays.asList(new CertificateMetaData []{
           createCertificateMetaData("1", "2013-04-13", "2013-05-13"),
           createCertificateMetaData("2", "2013-03-28", "2013-04-28"),
           createCertificateMetaData("3", "2013-03-01", "2013-03-27"),
           createCertificateMetaData("4", "2013-03-01", "2013-03-28"),
           createCertificateMetaData("5", "2013-03-01", "2013-03-27"),
           createCertificateMetaData("6", "2013-04-29", "2013-05-03"),
           createCertificateMetaData("7", "2013-04-01", "2013-05-03")
        });

        List<CertificateMetaData> result = new DateFilter(data).filter(new LocalDate("2013-03-28"), new LocalDate("2013-04-28"));
        List<String> ids = new ArrayList<String>();
        for (CertificateMetaData cmd : result) {
            ids.add(cmd.getId());
        }
        assertTrue(ids.contains("1"));
        assertTrue(ids.contains("2"));
        assertFalse(ids.contains("3"));
        assertTrue(ids.contains("4"));
        assertFalse(ids.contains("5"));
        assertFalse(ids.contains("6"));
        assertTrue(ids.contains("7"));
    }

    private CertificateMetaData createCertificateMetaData(String id, String from, String to) {
        Certificate c = new Certificate(id, "");
        CertificateMetaData cmd = new CertificateMetaData(c);
        cmd.setValidFromDate(new LocalDate(from));
        cmd.setValidToDate(new LocalDate(to));
        return cmd;
    }

    @Test
    public void testFilterNullDates() throws Exception {
        List<CertificateMetaData> data = Arrays.asList(new CertificateMetaData []{
                createCertificateMetaData("1", "2013-04-13", "2013-05-13"),
                createCertificateMetaData("2", "2013-03-28", "2013-04-28"),
                createCertificateMetaData("3", "2013-03-01", "2013-03-27"),
                createCertificateMetaData("7", "2013-04-01", "2013-05-03")
             });
        DateFilter dateFilter = new DateFilter(data);
        List<CertificateMetaData> result1 = dateFilter.filter(null, null);
        assertEquals(data.size(), result1.size());
        List<CertificateMetaData> result2 = dateFilter.filter(new LocalDate("2013-03-28"), null);
        assertEquals(data.size(), result2.size());
        List<CertificateMetaData> result3 = dateFilter.filter(null, new LocalDate("2013-04-28"));
        assertEquals(data.size(), result3.size());
    }


}
