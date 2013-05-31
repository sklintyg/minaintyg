package se.inera.certificate.integration.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static se.inera.certificate.model.CertificateState.DELETED;
import static se.inera.certificate.model.CertificateState.PROCESSED;
import static se.inera.certificate.model.CertificateState.RESTORED;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.builder.CertificateBuilder;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

/**
 * @author andreaskaltenbach
 */
public class ModelConverterTest {

    private static final LocalDateTime MARCH_FIRST = new LocalDateTime(2013, 3, 1, 0, 0, 0);
    private static final LocalDateTime APRIL_FIRST = new LocalDateTime(2013, 4, 1, 0, 0, 0);

    @Test
    public void testToCertificateMetaTypeConversion() {

        Certificate certificate = createCertificate();

        CertificateMetaType metaType = ModelConverter.toCertificateMetaType(certificate);

        assertEquals("112233", metaType.getCertificateId());
        assertEquals("fk7263", metaType.getCertificateType());
        assertEquals(new LocalDate(2000, 1, 1), metaType.getValidFrom());
        assertEquals(new LocalDate(2020, 1, 1), metaType.getValidTo());

        assertEquals("London Bridge Hospital", metaType.getFacilityName());

        assertEquals("Doctor Who", metaType.getIssuerName());
        assertEquals(new LocalDate(1999, 12, 31), metaType.getSignDate());

        assertEquals("true", metaType.getAvailable());
    }

    @Test
    public void testDeletedCertificateConversion() {

        Certificate certificate = createCertificate();
        certificate.setDeleted(true);

        CertificateMetaType metaType = ModelConverter.toCertificateMetaType(certificate);

        assertEquals("false", metaType.getAvailable());
    }

    @Test
    public void testCertificateStateConversion() {

        Certificate certificate = createCertificate();

        CertificateMetaType metaType = ModelConverter.toCertificateMetaType(certificate);

        assertEquals(3, metaType.getStatus().size());

        assertEquals("fk", metaType.getStatus().get(0).getTarget());
        assertEquals(StatusType.PROCESSED, metaType.getStatus().get(0).getType());
        assertNull(metaType.getStatus().get(0).getTimestamp());

        assertEquals("fk", metaType.getStatus().get(1).getTarget());
        assertEquals(StatusType.DELETED, metaType.getStatus().get(1).getType());
        assertEquals(MARCH_FIRST, metaType.getStatus().get(1).getTimestamp());

        assertEquals("fk", metaType.getStatus().get(2).getTarget());
        assertEquals(StatusType.RESTORED, metaType.getStatus().get(2).getType());
        assertEquals(APRIL_FIRST, metaType.getStatus().get(2).getTimestamp());
    }

    private Certificate createCertificate() {
        return new CertificateBuilder("112233")
                .certificateType("fk7263")
                .validity(new LocalDate(2000, 1, 1), new LocalDate(2020, 1, 1))
                .signingDoctorName("Doctor Who")
                .signedDate(new LocalDateTime(1999, 12, 31, 23, 59))
                .careUnitName("London Bridge Hospital")
                .deleted(false)
                .state(PROCESSED, "fk")
                .state(DELETED, "fk", MARCH_FIRST)
                .state(RESTORED, "fk", APRIL_FIRST)
                .build();
    }
}
