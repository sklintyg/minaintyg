package se.inera.certificate.integration;

import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

import javax.jws.WebParam;

/**
 * @author andreaskaltenbach
 */
public class ListCertificatesResponderImpl implements ListCertificatesResponderInterface {

    @Override
    public ListCertificatesResponseType listCertificates(@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress, @WebParam(partName = "parameters", name = "ListCertificatesRequest", targetNamespace = "urn:riv:insuranceprocess:healthreporting:ListCertificatesResponder:1") ListCertificatesRequestType parameters) {
        ListCertificatesResponseType response = new ListCertificatesResponseType();
        return response;
    }
}
