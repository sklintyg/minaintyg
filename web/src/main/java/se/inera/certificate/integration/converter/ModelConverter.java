package se.inera.certificate.integration.converter;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter(){}

    public static CertificateMetaType toCertificateMetaType(CertificateMetaData source) {
        return new CertificateMetaType();
    }

    public static CertificateMetaType toCertificateMetaType(Certificate source) {
        return new CertificateMetaType();
    }
}
