package se.inera.certificate.spec.util
import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.message.Message
import org.w3.wsaddressing10.AttributedURIType

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum

class WsClientFixture {

	private CustomObjectMapper jsonMapper = new CustomObjectMapper();
	def asJson(def object) {
		StringWriter sw = new StringWriter()
		jsonMapper.writeValue(sw, object)
		return sw.toString()
	}
	
    static String baseUrl = System.getProperty("certificate.baseUrl", "http://localhost:8080/inera-certificate/")
    AttributedURIType logicalAddress = new AttributedURIType()

    def setEndpoint(def responder, String serviceName) {
        Client client = ClientProxy.getClient(responder)
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, baseUrl + serviceName)
    }
	
	def resultAsString(response) {
		if (response) {
	        switch (response.result.resultCode) {
	            case ResultCodeEnum.OK:
	                return response.result.resultCode.toString()
	            case ResultCodeEnum.INFO:
	                return "[${response.result.resultCode.toString()}] - ${response.result.infoText}"
	            default:
					return "[${response.result.resultCode.toString()}] - ${response.result.errorText}"
	        }
		}
		else null
	}

}
