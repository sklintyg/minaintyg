package se.inera.certificate.support;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.builder.CertificateBuilder;

/**
 * @author andreaskaltenbach
 */
public final class CertificateFactory {

    private CertificateFactory() {
    }

    public static final String CERTIFICATE_ID = "123456";
    public static final String CERTIFICATE_DOCUMENT = "{\"name\":\"Some JSON\"}";
    public static final String CIVIC_REGISTRATION_NUMBER = "19001122-3344";
    public static final String FK7263 = "fk7263";

    public static final LocalDate VALID_FROM = new LocalDate(2000, 1, 1);
    public static final LocalDate VALID_TO = new LocalDate(2000, 12, 31);

    public static final LocalDateTime SIGNED_DATE = new LocalDateTime(1999, 12, 31, 10, 32);
    public static final String SIGNING_DOCTOR = "Dr. Oetker";

    public static final String CARE_UNIT_NAME = "London Bridge Hospital";

    public static Certificate buildCertificate() {
        return buildCertificate(CERTIFICATE_ID);
    }

    public static Certificate buildCertificate(String certificateId) {
        return buildCertificate(certificateId, VALID_FROM, VALID_TO);
    }

    public static Certificate buildCertificate(String certificateId, String certificateType) {
        return buildCertificate(certificateId, certificateType, VALID_FROM, VALID_TO);
    }

    public static Certificate buildCertificate(String certificateId, String validFrom, String validTo) {
        return buildCertificate(certificateId, new LocalDate(validFrom), new LocalDate(validTo));
    }

    public static Certificate buildCertificate(String certificateId, LocalDate validFrom, LocalDate validTo) {
        return buildCertificate(certificateId, FK7263, validFrom, validTo);
    }

    public static Certificate buildCertificate(String certificateId, String certificateType, LocalDate validFrom, LocalDate validTo) {
        return new CertificateBuilder(certificateId, CERTIFICATE_DOCUMENT)
                .civicRegistrationNumber(CIVIC_REGISTRATION_NUMBER)
                .certificateType(certificateType)
                .validity(validFrom, validTo)
                .signedDate(SIGNED_DATE)
                .signingDoctorName(SIGNING_DOCTOR)
                .careUnitName(CARE_UNIT_NAME)
                .build();
    }
}
