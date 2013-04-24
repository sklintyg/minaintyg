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

import java.util.List;

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
    private List<CertificateSupport> supportedCertificates;

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType parameters) {

        CertificateMetaData metaData = certificateService.getCertificate(parameters.getNationalIdentityNumber(), parameters.getCertificateId());

        GetCertificateResponseType response = new GetCertificateResponseType();

        if (metaData != null) {
            response.setMeta(ModelConverter.toCertificateMetaType(metaData));

            CertificateSupport certificateSupport = retrieveCertificateSupport(metaData.getType());

            CertificateType certificateType = new CertificateType();
            certificateType.getAny().add(certificateSupport.deserializeCertificate(metaData.getCertificate().getDocument()));
            response.setCertificate(certificateType);

            response.setResult(okResult());
        }
        else {
            response.setResult(failResult("Unknown certificate ID"));
        }

        return response;
    }

    private CertificateSupport retrieveCertificateSupport(String certificateType) {
        for (CertificateSupport certificateSupport : supportedCertificates) {
            if (certificateSupport.certificateType().equals(certificateType)) {
                return certificateSupport;
            }
        }
        return null;
    }


}
