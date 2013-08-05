package se.inera.certificate.spec
import javax.xml.bind.JAXBContext

import se.inera.certificate.integration.json.CustomObjectMapper
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderService
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum

/**
 *
 * @author andreaskaltenbach
 */
public class HamtaIntyg extends WsClientFixture {

    private GetCertificateResponderService getCertificateService = new GetCertificateResponderService();
    private GetCertificateResponderInterface getCertificateResponder = getCertificateService.getCertificateResponderPort

    public HamtaIntyg() {
        setEndpoint(getCertificateResponder, "get-certificate/v1.0")
    }

    String personnummer
    String intyg

    GetCertificateResponseType response

    public void execute() {
		response = null
        GetCertificateRequestType request = new GetCertificateRequestType()
        request.setNationalIdentityNumber(personnummer)
        request.setCertificateId(intyg)

        response = getCertificateResponder.getCertificate(logicalAddress, request)
    }

    public String resultat() {
        resultAsString(response)
    }

	public String svar() {
		if (response) {
	        switch (response.result.resultCode) {
	            case ResultCodeEnum.OK:
	                CertificateType certificate = response.certificate
					JAXBContext payloadContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
					org.w3c.dom.Node node = (org.w3c.dom.Node) certificate.any[0]
					return asJson(payloadContext.createUnmarshaller().unmarshal(node).value)
	            default:
	                return ""
	        }
		}
		else null
	}

    public String status() {
        response.meta.status.collect{it.type.toString()}
    }

}
