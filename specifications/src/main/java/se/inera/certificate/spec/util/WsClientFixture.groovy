package se.inera.certificate.spec.util
import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.message.Message
import org.w3.wsaddressing10.AttributedURIType
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum

class WsClientFixture {

    static String baseUrl = System.getProperty("certificate.baseUrl", "http://localhost:8080/inera-certificate/")
    AttributedURIType logicalAddress = new AttributedURIType()

    def setEndpoint(def responder, String serviceName) {
        Client client = ClientProxy.getClient(responder)
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, baseUrl + serviceName)
    }

    def result(def response) {
        switch (response.getResult().getResultCode()) {
            case ResultCodeEnum.OK:
                return "ok"
            default:
                return "[" + result.resultCode.toString() + "] - " + result.errorText
        }
    }
}
