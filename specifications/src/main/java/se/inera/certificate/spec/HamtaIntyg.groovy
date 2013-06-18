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

    public String svar() {
        GetCertificateRequestType request = new GetCertificateRequestType()
        request.setNationalIdentityNumber(personnummer)
        request.setCertificateId(intyg)

        GetCertificateResponseType response = getCertificateResponder.getCertificate(logicalAddress, request)

        resultAsString(response)
    }

}
