package se.inera.certificate.integration;

import java.io.IOException;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.infoResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import riv.insuranceprocess.healthreporting.getcertificatecontent.GetCertificateContentResponderInterface;
import riv.insuranceprocess.healthreporting.getcertificatecontentresponder._1.GetCertificateContentRequest;
import riv.insuranceprocess.healthreporting.getcertificatecontentresponder._1.GetCertificateContentResponse;
import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.converter.UtlatandeToUtlatandeJaxbConverter;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.service.CertificateService;

/**
 * @author andreaskaltenbach
 */
@Transactional
@SchemaValidation
public class GetCertificateContentResponderImpl implements GetCertificateContentResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(GetCertificateResponderImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificateService certificateService;

    @Override
    public GetCertificateContentResponse getCertificateContent(AttributedURIType logicalAddress, GetCertificateContentRequest request) {

        GetCertificateContentResponse response = new GetCertificateContentResponse();

        Certificate certificate;
        try {
            certificate = certificateService.getCertificate(request.getNationalIdentityNumber(), request.getCertificateId());
        } catch (MissingConsentException ex) {
            // return ERROR if user has not given consent
            LOG.info("Tried to get certificate '" + request.getCertificateId() + "' but user '" + request.getNationalIdentityNumber() + "' has not given consent.");
            response.setResult(failResult(String.format("Missing consent for patient %s", request.getNationalIdentityNumber())));
            return response;
        }

        if (certificate == null) {
            // return ERROR if no such certificate does exist
            LOG.info("Tried to get certificate '" + request.getCertificateId() + "' but no such certificate does exist for user '" + request.getNationalIdentityNumber() + "'.");
            response.setResult(failResult(String.format("Unknown certificate ID: %s", request.getCertificateId())));
            return response;
        }

        if (certificate.isRevoked()) {
            // return INFO if certificate is revoked
            LOG.info("Tried to get certificate '" + request.getCertificateId() + "' but certificate has been revoked'.");
            response.setResult(infoResult("Certificate '" + request.getCertificateId() + "' has been revoked"));
            return response;
        }

        attachCertificateDocument(certificate, response);
        return response;

    }

    private void attachCertificateDocument(Certificate certificate, GetCertificateContentResponse response) {
        Utlatande utlatande;
        try {
            utlatande = objectMapper.readValue(certificate.getDocument(), Utlatande.class);
        } catch (IOException e) {
            LOG.error("Failed to read certificate document for certificate #" + certificate.getId());
            throw new RuntimeException("Failed to read certificate document for certificate #" + certificate.getId(), e);
        }

        se.inera.certificate.common.v1.Utlatande utlatandeType = new UtlatandeToUtlatandeJaxbConverter(utlatande).convert();

        response.setCertificate(utlatandeType);
    }
}
