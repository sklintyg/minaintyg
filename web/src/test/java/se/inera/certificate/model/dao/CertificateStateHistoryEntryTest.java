package se.inera.certificate.model.dao;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class CertificateStateHistoryEntryTest {
    @Test
    public void testOrdering() {
        CertificateStateHistoryEntry e1 = new CertificateStateHistoryEntry();
        e1.setTimestamp(LocalDateTime.parse("2013-08-20T12:00:01"));
        CertificateStateHistoryEntry e2 = new CertificateStateHistoryEntry();
        e2.setTimestamp(LocalDateTime.parse("2013-08-21T12:00:01"));
        CertificateStateHistoryEntry e3 = new CertificateStateHistoryEntry();
        e3.setTimestamp(LocalDateTime.parse("2013-08-20T14:00:01"));
        CertificateStateHistoryEntry e4 = new CertificateStateHistoryEntry();
        e4.setTimestamp(null);

        Collection<CertificateStateHistoryEntry> c = Arrays.asList( e1, e2, e3, e4 );

        List<CertificateStateHistoryEntry> r = CertificateStateHistoryEntry.byTimestampDesc.sortedCopy(c);

        assertNull(r.get(0).getTimestamp());
        assertEquals(LocalDateTime.parse("2013-08-21T12:00:01"), r.get(1).getTimestamp());
        assertEquals(LocalDateTime.parse("2013-08-20T14:00:01"), r.get(2).getTimestamp());
        assertEquals(LocalDateTime.parse("2013-08-20T12:00:01"), r.get(3).getTimestamp());
    }
    
}
