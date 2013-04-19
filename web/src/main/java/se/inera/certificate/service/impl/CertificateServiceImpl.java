package se.inera.certificate.service.impl;

import org.springframework.stereotype.Service;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;

import java.util.Collections;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    @Override
    public List<CertificateMetaData> listCertificates(String civicRegistrationNumber, List<String> type) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Certificate getCertificate(String civicRegistrationNumber, String id) {
        return new Certificate();
    }
}
