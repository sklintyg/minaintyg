package se.inera.certificate.integration.converter;

import org.joda.time.LocalDate;
import se.inera.certificate.integration.builder.CertificateMetaTypeBuilder;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() { }

    public static CertificateMetaType toCertificateMetaType(CertificateMetaData source) {

        return new CertificateMetaTypeBuilder()
            .certificateId(source.getId())
            .certificateType(source.getType())
            .validity(new LocalDate(source.getValidFromDate()), new LocalDate(source.getValidToDate()))
            .issuerName(source.getSigningDoctorName())
            .facilityName(source.getCareUnitName())
            .signDate(new LocalDate(source.getSignedDate()))
            .available(source.getDeleted() ? "borttaget" : "ja") // TODO - Makulerat?
            .build();

            // TODO - convert certificate status
       /* if (source.getStatus() != null) {

                    List<CertificateStatus> list = meta.getStatus();
                    for (final CertificateStatus s : list) {
                        final CertificateStatusType type = new CertificateStatusType();
                        type.setTarget(s.getDestinator());
                        type.setTimestamp(toDateTime(s.getWhen()));

                        for (final StatusType t : StatusType.values()) {
                            if (t.name().equals(s.getType().name())) {
                                type.setType(t);
                                break;
                            }
                        }

                        metaType.getStatus().add(type);
                    }
                }  */
    }


}
