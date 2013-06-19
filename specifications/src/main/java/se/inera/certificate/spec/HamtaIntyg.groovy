package se.inera.certificate.spec
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderService
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType

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
        GetCertificateRequestType request = new GetCertificateRequestType()
        request.setNationalIdentityNumber(personnummer)
        request.setCertificateId(intyg)

        response = getCertificateResponder.getCertificate(logicalAddress, request)
    }

    public String svar() {
        resultAsString(response)
    }

    public String status() {
        response.getMeta().getStatus().collect{it.getType().toString()}
    }

}
