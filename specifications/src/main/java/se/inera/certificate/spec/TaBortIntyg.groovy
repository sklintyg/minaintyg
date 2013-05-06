package se.inera.certificate.spec

import groovyx.net.http.RESTClient
import se.inera.certificate.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON

public class TaBortIntyg extends RestClientFixture {

    String id

    public void execute() {
        def restClient = new RESTClient(baseUrl)
        restClient.delete(
                path: 'certificate/' + id,
                requestContentType: JSON
        )
    }

}
