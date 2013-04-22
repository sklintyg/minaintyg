package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.integration.converter.ModelConverter;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

import javax.jws.WebParam;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class ListCertificatesResponderImpl implements ListCertificatesResponderInterface {

    @Autowired
    CertificateService certificateService;

    @Override
    public ListCertificatesResponseType listCertificates(@WebParam(partName = "LogicalAddress", name = "To", targetNamespace = "http://www.w3.org/2005/08/addressing", header = true) AttributedURIType logicalAddress, @WebParam(partName = "parameters", name = "ListCertificatesRequest", targetNamespace = "urn:riv:insuranceprocess:healthreporting:ListCertificatesResponder:1") ListCertificatesRequestType parameters) {

        ListCertificatesResponseType response = new ListCertificatesResponseType();

        List<CertificateMetaData> certificates = certificateService.listCertificates(parameters.getNationalIdentityNumber(), parameters.getCertificateType());

        for (CertificateMetaData certificate : certificates) {
            response.getMeta().add(ModelConverter.ws(certificate));
        }

        response.setResult(okResult());

        return response;
    }

    private ResultOfCall okResult() {
        ResultOfCall result = new ResultOfCall();
        result.setResultCode(ResultCodeEnum.OK);
        return result;
    }
}
