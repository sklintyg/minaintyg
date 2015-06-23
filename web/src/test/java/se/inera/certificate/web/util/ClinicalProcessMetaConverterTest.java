package se.inera.certificate.web.util;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.builder.ClinicalProcessCertificateMetaTypeBuilder;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;


public class ClinicalProcessMetaConverterTest {

    private static final String AVAILABLE = "available";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String TYPE = "type";
    private static final String FACILITY_NAME = "facilityName";
    private static final String ISSUER_NAME = "issuerName";

    private static final LocalDateTime FIRST_TIMESTAMP = new LocalDateTime(2013, 1, 2, 20, 0);
    private static final LocalDateTime LATER_TIMESTAMP = new LocalDateTime(2013, 1, 3, 20, 0);
    private static final String TARGET = "FK";

    private static UtlatandeStatus unhandledStatus;
    private static UtlatandeStatus deletedStatus;
    private static UtlatandeStatus sentStatus;
    private static UtlatandeStatus cancelledStatus;

    private ClinicalProcessCertificateMetaTypeBuilder builder;

    @BeforeClass
    public static void setup() {
        unhandledStatus = new UtlatandeStatus();
        unhandledStatus.setType(StatusType.UNHANDLED);
        unhandledStatus.setTarget(TARGET);
        unhandledStatus.setTimestamp(FIRST_TIMESTAMP);

        deletedStatus = new UtlatandeStatus();
        deletedStatus.setType(StatusType.DELETED);
        deletedStatus.setTarget(TARGET);
        deletedStatus.setTimestamp(LATER_TIMESTAMP);

        sentStatus = new UtlatandeStatus();
        sentStatus.setType(StatusType.SENT);
        sentStatus.setTarget(TARGET);
        sentStatus.setTimestamp(FIRST_TIMESTAMP);

        cancelledStatus = new UtlatandeStatus();
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
                .validity(new LocalDate(), new LocalDate())
                .complemantaryInfo("Test");
    }

    @Test
    public void testConvertStatusWithNullList() {
        UtlatandeMetaData meta = ClinicalProcessMetaConverter.toUtlatandeMetaData(builder.build());
        assertEquals(0, meta.getStatuses().size());
    }

    @Test
    public void testConvertStatusWithoutSentCancel() {
        builder.status(deletedStatus);

        UtlatandeMetaData meta = ClinicalProcessMetaConverter.toUtlatandeMetaData(builder.build());
        assertEquals(0, meta.getStatuses().size());
    }

    @Test
    public void testConvertStatusWithSentCancel() {
        builder.status(deletedStatus);
        builder.status(cancelledStatus);
        builder.status(sentStatus);
        builder.status(unhandledStatus);

        UtlatandeMetaData meta = ClinicalProcessMetaConverter.toUtlatandeMetaData(builder.build());
        assertEquals(2, meta.getStatuses().size());
    }

    @Test
    public void testStatusOrder() {
        builder.status(sentStatus); // Has an earlier timestamp
        builder.status(cancelledStatus); // Has a later timestamp

        UtlatandeMetaData meta = ClinicalProcessMetaConverter.toUtlatandeMetaData(builder.build());
        // Late timestamp should be first
        assertEquals(cancelledStatus.getType().name(), meta.getStatuses().get(0).getType().name());
        // Early timestamp should be second
        assertEquals(sentStatus.getType().name(), meta.getStatuses().get(1).getType().name());
    }

    @Test
    public void testAdditionalInfo() {
        UtlatandeMetaData meta = ClinicalProcessMetaConverter.toUtlatandeMetaData(builder.build());
        CertificateMetaType certMeta = builder.build();

        assertEquals("Test", meta.getComplemantaryInfo());
        assertEquals("Test", certMeta.getComplemantaryInfo());
    }

}
