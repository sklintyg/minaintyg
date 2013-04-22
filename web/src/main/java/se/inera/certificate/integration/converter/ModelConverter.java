package se.inera.certificate.integration.converter;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public class ModelConverter {

    private ModelConverter(){}

    public static CertificateMetaType toCertificateMetaType(CertificateMetaData source) {
        CertificateMetaType metaType = new CertificateMetaType();
        return metaType;
    }

    public static CertificateMetaType toCertificateMetaType(Certificate source) {
        CertificateMetaType metaType = new CertificateMetaType();
        return metaType;
    }
}
