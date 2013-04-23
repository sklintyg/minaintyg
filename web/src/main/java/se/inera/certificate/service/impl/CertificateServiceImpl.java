package se.inera.certificate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateDao certificateDao;

    @Override
    public List<CertificateMetaData> listCertificates(String civicRegistrationNumber, List<String> type) {
        return certificateDao.findMetaDataByCivicRegistrationNumberAndType(civicRegistrationNumber, type);
    }

    @Override
    public Certificate getCertificate(String civicRegistrationNumber, String id) {
        return certificateDao.getCertificate(id);
    }
}
