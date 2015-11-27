package se.inera.certificate.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;

public class CertificateMetaConverterTest {

    private static final String AVAILABLE = "available";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String TYPE = "type";
    private static final String FACILITY_NAME = "facilityName";
    private static final String ISSUER_NAME = "issuerName";

    private static final LocalDateTime FIRST_TIMESTAMP = new LocalDateTime(2013, 1, 2, 20, 0);
    private static final LocalDateTime LATER_TIMESTAMP = new LocalDateTime(2013, 1, 3, 20, 0);
    private static final String TARGET = "FK";

    private static Status sentStatus;
    private static Status cancelledStatus;

    private UtlatandeMetaBuilder builder;

    @BeforeClass
    public static void setup() {
        sentStatus = new Status(CertificateState.SENT, TARGET, FIRST_TIMESTAMP);
        cancelledStatus = new Status(CertificateState.CANCELLED, TARGET, LATER_TIMESTAMP);
    }

    @Before
    public void setupCertificateMetaTypeBuilder() {
        builder = new UtlatandeMetaBuilder();
        builder.id(CERTIFIED_ID)
                .type(TYPE)
                .issuerName(ISSUER_NAME)
                .facilityName(FACILITY_NAME)
                .signDate(new LocalDateTime())
                .available(AVAILABLE)
                .additionalInfo("2013-01-01 till 2014-01-01");
    }

    @Test
    public void testIsCertificateCancelledWithNullList() {
        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithoutCancelStatus() {
        builder.addStatus(sentStatus);

        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithCancelStatus() {
        builder.addStatus(sentStatus);
        builder.addStatus(cancelledStatus);

        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertTrue(meta.getCancelled());
    }

    @Test
    public void testAdditionalInfo() {
        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        UtlatandeMetaData utlatandeMeta = builder.build();

        assertEquals("2013-01-01 till 2014-01-01", utlatandeMeta.getComplemantaryInfo());
        assertEquals("2013-01-01 till 2014-01-01", meta.getComplementaryInfo());
    }
}
