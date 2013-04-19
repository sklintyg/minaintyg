package se.inera.certificate.integration.converter;

import se.inera.certificate.model.CertificateMetaData;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class ModelConverter {

    public static CertificateMetaType ws(CertificateMetaData certificateMetaData) {
        CertificateMetaType metaType = new CertificateMetaType();
        return metaType;
    }
}
