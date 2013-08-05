package se.inera.certificate.spec
import org.skyscreamer.jsonassert.JSONAssert

import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequest
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderService
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponse
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum
import fitnesse.slim.SlimError

/**
 *
 * @author andreaskaltenbach
 */
public class HamtaIntygsinnehall extends WsClientFixture {

    private GetCertificateContentResponderService getCertificateService = new GetCertificateContentResponderService();
    private GetCertificateContentResponderInterface getCertificateResponder = getCertificateService.getCertificateContentResponderPort

    public HamtaIntygsinnehall() {
        setEndpoint(getCertificateResponder, "get-certificate-content/v1.0")
    }

    String personnummer
    String intyg
	String förväntatSvar
	private String faktisktSvar
	
    GetCertificateContentResponse response

    public void execute() {
		faktisktSvar = null
        GetCertificateContentRequest request = new GetCertificateContentRequest()
        request.setNationalIdentityNumber(personnummer)
        request.setCertificateId(intyg)

        response = getCertificateResponder.getCertificateContent(logicalAddress, request)
    }

    public String resultat() {
		if (response) {
	        switch (response.result.resultCode) {
	            case ResultCodeEnum.OK:
					if (förväntatSvar)
					faktisktSvar = asJson(response.certificate)
	                try {
						JSONAssert.assertEquals(förväntatSvar, faktisktSvar, false)
						return "OK"
					} catch (AssertionError e) {
						throw new Exception("message:<<${e.message.replace(System.getProperty('line.separator'), ' ')}>>")
					}
	            case ResultCodeEnum.INFO:
	                return "[${response.result.resultCode.toString()}] - ${response.result.infoText}"
	            default:
					return "[${response.result.resultCode.toString()}] - ${response.result.errorText}"
	        }
		}
		else "UNDEFINED"
    }

	public String svar() {
		faktisktSvar
	}

    public String status() {
        response.meta.status.collect{it.type.toString()}
    }

}
