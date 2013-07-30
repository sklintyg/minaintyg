package se.inera.certificate.model.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import se.inera.certificate.model.dao.Certificate;

public class CertificateBuilderTest {

    private static final String FROM_DATE = "2013-03-01";
    private static final String TO_DATE = "2013-03-20";
    private static final LocalDateTime SIGNED_DATE = new LocalDateTime(2013, 3, 1, 11, 32);

    @Test
    public void setAllFields() {
        Certificate certificate = new CertificateBuilder("certificateId")
            .certificateType("certificateType")
            .validity(FROM_DATE, TO_DATE)
            .signingDoctorName("signingDoctorName")
            .careUnitName("careUnitName")
            .signedDate(SIGNED_DATE)
            .deleted(false)
            .build();

        assertEquals("certificateId", certificate.getId());
        assertEquals("careUnitName", certificate.getCareUnitName());
        assertEquals("certificateType", certificate.getType());
        assertEquals("signingDoctorName", certificate.getSigningDoctorName());
        assertEquals(SIGNED_DATE, certificate.getSignedDate());
        assertEquals(FROM_DATE, certificate.getValidFromDate());
        assertEquals(TO_DATE, certificate.getValidToDate());
        assertFalse(certificate.getDeleted());
    }
}
