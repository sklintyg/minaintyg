package se.inera.certificate.integration;

import javax.jws.WebParam;

import org.w3.wsaddressing10.AttributedURIType;
import riv.insuranceprocess.healthreporting.getcertificatecontent.GetCertificateContentResponderInterface;
import riv.insuranceprocess.healthreporting.getcertificatecontentresponder._1.GetCertificateContentRequest;
import riv.insuranceprocess.healthreporting.getcertificatecontentresponder._1.GetCertificateContentResponse;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateContentResponderImpl implements GetCertificateContentResponderInterface {
    @Override
    public GetCertificateContentResponse getCertificateContent(@WebParam( partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true ) AttributedURIType logicalAddress, @WebParam( partName = "parameters", name = "GetCertificateContentRequest", targetNamespace = "urn:riv:insuranceprocess:healthreporting:GetCertificateContentResponder:1" ) GetCertificateContentRequest parameters) {
        GetCertificateContentResponse response = new GetCertificateContentResponse();
        return response;
    }
}
