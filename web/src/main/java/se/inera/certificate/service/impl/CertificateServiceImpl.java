package se.inera.certificate.service.impl;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateDao certificateDao;

    @Override
    public List<CertificateMetaData> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate) {
        return certificateDao.findCertificateMetaData(civicRegistrationNumber, certificateTypes, fromDate, toDate);
    }

    @Override
    public CertificateMetaData getCertificate(String civicRegistrationNumber, String id) {
        return certificateDao.getCertificate(id);
    }
}
