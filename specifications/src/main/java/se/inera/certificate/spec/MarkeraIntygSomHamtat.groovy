package se.inera.certificate.spec

import org.joda.time.LocalDateTime
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderService
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum

/**
 *
 * @author andreaskaltenbach
 */
class MarkeraIntygSomHamtat extends WsClientFixture {

    private SetCertificateStatusResponderService setCertificateStatusService = new SetCertificateStatusResponderService();
    private SetCertificateStatusResponderInterface setCertificateStatusResponder = setCertificateStatusService.setCertificateStatusResponderPort

    String personnr
    String intyg

    String kommentar

    public MarkeraIntygSomHamtat() {
        setEndpoint(setCertificateStatusResponder, "set-certificate-status/v1.0")
    }

    public String svar() {
        SetCertificateStatusRequestType parameters = new SetCertificateStatusRequestType()
        parameters.nationalIdentityNumber = personnr
        parameters.certificateId = intyg

        parameters.target = "fk"
        parameters.timestamp = LocalDateTime.now()
        parameters.status = StatusType.SENT

        SetCertificateStatusResponseType response = setCertificateStatusResponder.setCertificateStatus(logicalAddress, parameters)

        if (response.result.resultCode == ResultCodeEnum.OK) {
            return "ok"
        } else {
            return response.result.errorText
        }
    }
}
