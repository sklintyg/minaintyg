package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;
import riv.insuranceprocess.healthreporting.setcertificatestatus._1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.certificate.exception.InvalidCertificateIdentifierException;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;

import static se.inera.certificate.integration.converter.ModelConverter.toCertificateState;

import static se.inera.certificate.integration.ResultOfCallUtil.*;

/**
 * @author andreaskaltenbach
 */
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
