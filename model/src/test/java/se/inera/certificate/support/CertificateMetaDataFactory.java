package se.inera.certificate.support;

import org.joda.time.LocalDate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.model.builder.CertificateMetaDataBuilder;

/**
 * @author andreaskaltenbach
 */
public class CertificateMetaDataFactory {

    private CertificateMetaDataFactory() {
    }

    public static final String CERTIFICATE_ID = "123456";
    public static final String CERTIFICATE_DOCUMENT = "<RegisterMedicalCertificate xmlns=\"urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3\"><lakarutlatande></lakarutlatande></RegisterMedicalCertificate>";
    public static final String CIVIC_REGISTRATION_NUMBER = "19001122-3344";
    public static final String FK7263 = "fk7263";

    public static final LocalDate VALID_FROM = new LocalDate(2000, 1, 1);
    public static final LocalDate VALID_TO = new LocalDate(2000, 12, 31);

    public static final LocalDate SIGNED_DATE = new LocalDate(1999, 12, 31);
    public static final String SIGNING_DOCTOR = "Dr. Oetker";

    public static final String CARE_UNIT_NAME = "London Bridge Hospital";

    public static CertificateMetaData buildCertificateMetaData() {
        return buildCertificateMetaData(CERTIFICATE_ID);
    }

    public static CertificateMetaData buildCertificateMetaData(String certificateId) {
        return buildCertificateMetaData(certificateId, VALID_FROM, VALID_TO);
    }

    public static CertificateMetaData buildCertificateMetaData(String certificateId, String certificateType) {
        return buildCertificateMetaData(certificateId, certificateType, VALID_FROM, VALID_TO);
    }

    public static CertificateMetaData buildCertificateMetaData(String certificateId, String validFrom, String validTo) {
        return buildCertificateMetaData(certificateId, new LocalDate(validFrom), new LocalDate(validTo));
    }

    public static CertificateMetaData buildCertificateMetaData(String certificateId, LocalDate validFrom, LocalDate validTo) {
        return buildCertificateMetaData(certificateId, FK7263, validFrom, validTo);
    }

    public static CertificateMetaData buildCertificateMetaData(String certificateId, String certificateType, LocalDate validFrom, LocalDate validTo) {
        return new CertificateMetaDataBuilder(certificateId, CERTIFICATE_DOCUMENT)
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(certificateType)
                .validity(validFrom, validTo)
                .signedDate(SIGNED_DATE)
                .signingDoctorName(SIGNING_DOCTOR)
                .careUnitName(CARE_UNIT_NAME)
                .build();
    }
}
