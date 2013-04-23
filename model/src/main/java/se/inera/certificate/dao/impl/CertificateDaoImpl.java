package se.inera.certificate.dao.impl;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.CertificateMetaData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<CertificateMetaData> findCertificateMetaData(String civicRegistrationNumber, List<String> types, LocalDate fromDate, LocalDate toDate) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CertificateMetaData> query = criteriaBuilder.createQuery(CertificateMetaData.class);
        Root<CertificateMetaData> root = query.from(CertificateMetaData.class);

        if (civicRegistrationNumber == null) {
            return Collections.emptyList();
        }

        List<Predicate> predicates = new ArrayList<>();

        // meta data has to match civic registration number
        predicates.add(criteriaBuilder.equal(root.get("civicRegistrationNumber"), civicRegistrationNumber));

        // filter by certificate types
        if (types != null && !types.isEmpty()) {
            predicates.add(root.get("type").in(types));
        }

        query.where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public CertificateMetaData getCertificate(String certificateId) {
        return entityManager.find(CertificateMetaData.class, certificateId);
    }

    @Override
    public void store(CertificateMetaData certificateMetaData) {
        entityManager.persist(certificateMetaData);
    }

}
