package se.inera.certificate.integration;

import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;

import javax.jws.WebParam;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateResponderImpl implements GetCertificateResponderInterface {

    @Override
    public GetCertificateResponseType getCertificate(@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress, @WebParam(partName = "parameters", name = "GetCertificateRequest", targetNamespace = "urn:riv:insuranceprocess:healthreporting:GetCertificateResponder:1") GetCertificateRequestType parameters) {
        GetCertificateResponseType response = new GetCertificateResponseType();
        return response;
    }
}
