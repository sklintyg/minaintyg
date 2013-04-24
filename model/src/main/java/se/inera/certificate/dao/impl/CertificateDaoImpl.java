package se.inera.certificate.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.CertificateMetaData;

/**
 * Implementation of {@link CertificateDao}.
 */
@Repository
public class CertificateDaoImpl implements CertificateDao {

    /** Injected EntityManager object. */
    @PersistenceContext
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * @see se.inera.certificate.dao.CertificateDao#findCertificateMetaData(java.lang.String, java.util.List, org.joda.time.LocalDate, org.joda.time.LocalDate)
     */
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
        List<CertificateMetaData> result = entityManager.createQuery(query).getResultList();

        // expect a small number, so lets filter in memory
        return new DateFilter(result).filter(fromDate, toDate);
    }

    /*
     * (non-Javadoc)
     * @see se.inera.certificate.dao.CertificateDao#getCertificate(java.lang.String)
     */
    @Override
    public CertificateMetaData getCertificate(String certificateId) {
        return entityManager.find(CertificateMetaData.class, certificateId);
    }

    /*
     * (non-Javadoc)
     * @see se.inera.certificate.dao.CertificateDao#store(se.inera.certificate.model.CertificateMetaData)
     */
    @Override
    public void store(CertificateMetaData certificateMetaData) {
        entityManager.persist(certificateMetaData);
    }

}
