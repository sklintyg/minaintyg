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
        List<CertificateMetaData> result1 = new DateFilter(data).filter(null, null);
        assertEquals(data.size(), result1.size());
        List<CertificateMetaData> result2 = new DateFilter(data).filter(new LocalDate("2013-03-28"), null);
        assertEquals(data.size(), result2.size());
        List<CertificateMetaData> result3 = new DateFilter(data).filter(null, new LocalDate("2013-04-28"));
        assertEquals(data.size(), result3.size());
    }


}
