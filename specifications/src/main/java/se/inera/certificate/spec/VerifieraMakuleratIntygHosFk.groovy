package se.inera.certificate.spec

import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator.HeadersDecorator;
import se.inera.certificate.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON

public class VerifieraMakuleratIntygHosFk extends RestClientFixture {

    String id
    def response
    
    public void execute() {
        def restClient = new RESTClient(baseUrl)
        response = restClient.get(
                path: 'fk/certificates',
                requestContentType: JSON,
                contentType: 'application/json'
        )
    }

    
    public String makulerat() {
        def row = response.data[id]
        if (row != null) {
            return row['Makulerad']
        } else {
            return "Nej"
        }
    }
}
