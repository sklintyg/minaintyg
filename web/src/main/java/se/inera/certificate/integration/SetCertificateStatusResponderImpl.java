package se.inera.certificate.integration;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;
import static se.inera.certificate.integration.converter.ModelConverter.toCertificateState;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.exception.InvalidCertificateIdentifierException;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;

/**
 * @author andreaskaltenbach
 */
@Transactional(rollbackFor = {InvalidCertificateIdentifierException.class})
@SchemaValidation
public class SetCertificateStatusResponderImpl implements SetCertificateStatusResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Override
    public SetCertificateStatusResponseType setCertificateStatus(AttributedURIType logicalAddress, SetCertificateStatusRequestType request) {

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();

        try {
            certificateService.setCertificateState(request.getNationalIdentityNumber(), request.getCertificateId(), request.getTarget(), toCertificateState(request.getStatus()), request.getTimestamp());
            response.setResult(okResult());
        } catch (InvalidCertificateIdentifierException e) {
            response.setResult(failResult(e.getMessage()));
        }

        return response;
    }
}
