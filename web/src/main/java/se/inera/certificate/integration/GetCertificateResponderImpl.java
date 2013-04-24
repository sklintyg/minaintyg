package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.integration.ResultOfCallUtil.applicationErrorResult;
import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
@Transactional
public class GetCertificateResponderImpl implements GetCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    List<CertificateSupport> supportedCertificates = new ArrayList<>();

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType parameters) {

        CertificateMetaData metaData = certificateService.getCertificate(parameters.getNationalIdentityNumber(), parameters.getCertificateId());

        GetCertificateResponseType response = new GetCertificateResponseType();

        if (metaData != null) {
            response.setMeta(ModelConverter.toCertificateMetaType(metaData));
            attachCertificateDocument(metaData, response);
        }
        else {
            response.setResult(failResult(String.format("Unknown certificate ID: %s", parameters.getCertificateId())));
        }

        return response;
    }

    private void attachCertificateDocument(CertificateMetaData metaData, GetCertificateResponseType response) {
        CertificateType certificateType = new CertificateType();

        CertificateSupport certificateSupport = retrieveCertificateSupportForCertificateType(metaData.getType());
        if (certificateSupport == null) {
            // given certificate type is not supported
            response.setResult(applicationErrorResult(String.format("Unsupported certificate type: %s", metaData.getType())));
        }
        else {
            // certificate type is supported and we use a CertificateSupport implementation to get the JAXB element from the certificate string
            certificateType.getAny().add(certificateSupport.deserializeCertificate(metaData.getCertificate().getDocument()));
            response.setCertificate(certificateType);
            response.setResult(okResult());
        }
    }

    private CertificateSupport retrieveCertificateSupportForCertificateType(String certificateType) {
        for (CertificateSupport certificateSupport : supportedCertificates) {
            if (certificateSupport.certificateType().equals(certificateType)) {
                return certificateSupport;
            }
        }
        return null;
    }


}
