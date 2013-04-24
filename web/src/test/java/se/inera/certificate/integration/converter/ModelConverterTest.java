package se.inera.certificate.integration.converter;

import org.joda.time.LocalDate;
import org.junit.Test;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.model.builder.CertificateMetaDataBuilder;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

import static org.junit.Assert.assertEquals;

/**
 * @author andreaskaltenbach
 */
public class ModelConverterTest {

    @Test
    public void testToCertificateMetaTypeConversion() {

        CertificateMetaData metaData = createCertificateMetaData();

        CertificateMetaType metaType = ModelConverter.toCertificateMetaType(metaData);

        assertEquals("112233", metaType.getCertificateId());
        assertEquals("fk7263", metaType.getCertificateType());
        assertEquals(new LocalDate(2000,1,1), metaType.getValidFrom());
        assertEquals(new LocalDate(2020,1,1), metaType.getValidTo());

        assertEquals("London Bridge Hospital", metaType.getFacilityName());

        assertEquals("Doctor Who", metaType.getIssuerName());
        assertEquals(new LocalDate(1999,12,31), metaType.getSignDate());

        assertEquals("ja", metaType.getAvailable());
    }

    @Test
    public void testDeletedCertificateConversion() {

        CertificateMetaData metaData = createCertificateMetaData();
        metaData.setDeleted(true);

        CertificateMetaType metaType = ModelConverter.toCertificateMetaType(metaData);

        assertEquals("borttaget", metaType.getAvailable());
    }

    private CertificateMetaData createCertificateMetaData() {
         return new CertificateMetaDataBuilder("112233")
                     .certificateType("fk7263")
                     .validity(new LocalDate(2000, 1, 1).toDate(), new LocalDate(2020, 1, 1).toDate())
                     .signingDoctorName("Doctor Who")
                     .signedDate(new LocalDate(1999, 12, 31).toDate())
                     .careUnitName("London Bridge Hospital")
                     .deleted(false)
                     .build();
    }
}
