package se.inera.certificate.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<CertificateMetaData> findMetaDataByCivicRegistrationNumberAndType(String civicRegistrationNumber, List<String> types) {
        return entityManager.createNamedQuery("CertificateMetaData.findByCivicRegistrationNumberAndType", CertificateMetaData.class)
                .setParameter("civicRegistrationNumber", civicRegistrationNumber)
                .setParameter("types", types)
                .getResultList();
    }

    @Override
    public Certificate getCertificate(String certificateId) {
        return entityManager.find(Certificate.class, certificateId);
    }

}
