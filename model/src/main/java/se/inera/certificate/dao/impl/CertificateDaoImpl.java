package se.inera.certificate.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public CertificateMetaData getCertificate(String certificateId) {
        return entityManager.find(CertificateMetaData.class, certificateId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void store(CertificateMetaData certificateMetaData) {
        entityManager.persist(certificateMetaData);
    }

}
