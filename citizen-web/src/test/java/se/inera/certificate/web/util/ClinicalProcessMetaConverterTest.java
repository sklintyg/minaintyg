package se.inera.certificate.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateStatusType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.inera.certificate.integration.builder.ClinicalProcessCertificateMetaTypeBuilder;

public class ClinicalProcessMetaConverterTest {

    private static final String AVAILABLE = "available";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String TYPE = "type";
    private static final String FACILITY_NAME = "facilityName";
    private static final String ISSUER_NAME = "issuerName";

    private static final LocalDateTime FIRST_TIMESTAMP = new LocalDateTime(2013, 1, 2, 20, 0);
    private static final LocalDateTime LATER_TIMESTAMP = new LocalDateTime(2013, 1, 3, 20, 0);
    private static final String TARGET = "FK";

    private static CertificateStatusType unhandledStatus;
    private static CertificateStatusType deletedStatus;
    private static CertificateStatusType sentStatus;
    private static CertificateStatusType cancelledStatus;

    private ClinicalProcessCertificateMetaTypeBuilder builder;

    @BeforeClass
    public static void setup() {
        unhandledStatus = new CertificateStatusType();
        unhandledStatus.setType(StatusType.UNHANDLED);
        unhandledStatus.setTarget(TARGET);
        unhandledStatus.setTimestamp(FIRST_TIMESTAMP);

        deletedStatus = new CertificateStatusType();
        deletedStatus.setType(StatusType.DELETED);
        deletedStatus.setTarget(TARGET);
        deletedStatus.setTimestamp(LATER_TIMESTAMP);

        sentStatus = new CertificateStatusType();
        sentStatus.setType(StatusType.SENT);
        sentStatus.setTarget(TARGET);
        sentStatus.setTimestamp(FIRST_TIMESTAMP);

        cancelledStatus = new CertificateStatusType();
        cancelledStatus.setType(StatusType.CANCELLED);
        cancelledStatus.setTarget(TARGET);
        cancelledStatus.setTimestamp(LATER_TIMESTAMP);
    }

    @Before
    public void setupCertificateMetaTypeBuilder() {
        builder = new ClinicalProcessCertificateMetaTypeBuilder();
        builder.available(AVAILABLE)
                .certificateId(CERTIFIED_ID)
                .certificateType(TYPE)
                .facilityName(FACILITY_NAME)
                .issuerName(ISSUER_NAME)
                .signDate(new LocalDateTime())
                .validity(new LocalDate(), new LocalDate());
    }

    @Test
    public void testIsCertificateCancelledWithNullList() {
        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithoutCancelStatus() {
        builder.status(sentStatus);

        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithCancelStatus() {
        builder.status(sentStatus);
        builder.status(cancelledStatus);

        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertTrue(meta.getCancelled());
    }

    @Test
    public void testConvertStatusWithNullList() {
        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertEquals(0, meta.getStatuses().size());
    }

    @Test
    public void testConvertStatusWithoutSentCancel() {
        builder.status(deletedStatus);

        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertEquals(0, meta.getStatuses().size());
    }

    @Test
    public void testConvertStatusWithSentCancel() {
        builder.status(deletedStatus);
        builder.status(cancelledStatus);
        builder.status(sentStatus);
        builder.status(unhandledStatus);

        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        assertEquals(2, meta.getStatuses().size());
    }

    @Test
    public void testStatusOrder() {
        builder.status(sentStatus); // Has an earlier timestamp
        builder.status(cancelledStatus); // Has a later timestamp

        CertificateMeta meta = ClinicalProcessMetaConverter.toCertificateMeta(builder.build());
        // Late timestamp should be first
        assertEquals(cancelledStatus.getType().toString(), meta.getStatuses().get(0).getType());
        // Early timestamp should be second
        assertEquals(sentStatus.getType().toString(), meta.getStatuses().get(1).getType());
    }
}
