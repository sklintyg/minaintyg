package se.inera.certificate.spec
import javax.xml.bind.JAXBContext

import org.skyscreamer.jsonassert.JSONAssert

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
	String förväntatSvar
	private String faktisktSvar
	private String resultat
	
    private GetCertificateResponseType response

	public void reset() {
		response = null
		resultat = null
		förväntatSvar = null
		faktisktSvar = null
	}
	
    public void execute() {
        GetCertificateRequestType request = new GetCertificateRequestType()
        request.setNationalIdentityNumber(personnummer)
        request.setCertificateId(intyg)

        response = getCertificateResponder.getCertificate(logicalAddress, request)
        switch (response.result.resultCode) {
            case ResultCodeEnum.OK:
	            CertificateType certificate = response.certificate
				JAXBContext payloadContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
				org.w3c.dom.Node node = (org.w3c.dom.Node) certificate.any[0]
				faktisktSvar = asJson(payloadContext.createUnmarshaller().unmarshal(node).value)
				resultat = "OK"
				break
            case ResultCodeEnum.INFO:
                resultat = "[${response.result.resultCode.toString()}] - ${response.result.infoText}"
				break
            default:
				resultat = "[${response.result.resultCode.toString()}] - ${response.result.errorText}"
		} 
    }

    public String resultat() {
        if (response.result.resultCode == ResultCodeEnum.OK && förväntatSvar) {
            try {  
				JSONAssert.assertEquals(förväntatSvar, faktisktSvar, false)
				return "OK"
			} catch (AssertionError e) {
				asErrorMessage(e.message)
			}
		} else {
			resultat
		}
    }

	public String svar() {
		faktisktSvar
	}

    public String status() {
        response.meta.status.collect{it.type.toString()}
    }

}
