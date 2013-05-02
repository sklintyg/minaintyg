package se.inera.certificate.model.builder;

import org.joda.time.LocalDate;
import org.junit.Test;
import se.inera.certificate.model.Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CertificateBuilderTest {

    private static final LocalDate FROM_DATE = new LocalDate(2013, 3, 1);
    private static final LocalDate TO_DATE = new LocalDate(2013, 3, 20);
    private static final LocalDate SIGNED_DATE = new LocalDate(2013, 3, 1);

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
