package se.inera.certificate.model.builder;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.model.CertificateMetaData;

public class CertificateMetaDataBuilderTest {

    private static final LocalDate FROM_DATE = new LocalDate(2013, 3, 1);
    private static final LocalDate TO_DATE = new LocalDate(2013, 3, 20);
    private static final LocalDate SIGNED_DATE = new LocalDate(2013, 3, 1);

    @Test
    public void setAllFields() {
        CertificateMetaData meta = new CertificateMetaDataBuilder("certificateId")
            .certificateType("certificateType")
            .validity(FROM_DATE, TO_DATE)
            .signingDoctorName("signingDoctorName")
            .careUnitName("careUnitName")
            .signedDate(SIGNED_DATE)
            .deleted(false)
            .build();

        assertEquals("certificateId", meta.getId());
        assertEquals("careUnitName", meta.getCareUnitName());
        assertEquals("certificateType", meta.getType());
        assertEquals("signingDoctorName", meta.getSigningDoctorName());
        assertEquals(SIGNED_DATE, meta.getSignedDate());
        assertEquals(FROM_DATE, meta.getValidFromDate());
        assertEquals(TO_DATE, meta.getValidToDate());
        assertFalse(meta.getDeleted());
    }
}
