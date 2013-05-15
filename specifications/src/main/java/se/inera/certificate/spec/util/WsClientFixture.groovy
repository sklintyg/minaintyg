package se.inera.certificate.spec.util

import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.message.Message
import org.w3.wsaddressing10.AttributedURIType

class WsClientFixture {

	static String baseUrl = System.getProperty("certificate.baseUrl")
	AttributedURIType logicalAddress = new AttributedURIType()
	
	def setEndpoint(def responder, String serviceName) {
	Client client = ClientProxy.getClient(responder);
		client.getRequestContext().put(Message.ENDPOINT_ADDRESS, baseUrl + serviceName) ;
	}
}
