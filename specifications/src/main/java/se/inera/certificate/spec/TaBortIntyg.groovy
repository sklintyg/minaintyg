package se.inera.certificate.spec

import groovyx.net.http.RESTClient
import se.inera.certificate.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON

public class TaBortIntyg extends RestClientFixture {

    String id
    String idTemplate
    int from
    int to
    public void execute() {
        def restClient = new RESTClient(baseUrl)
        for (i in from..to) {
            if (idTemplate) {
                id = String.format(idTemplate, i)
            }
            restClient.delete(
                    path: 'certificate/' + id,
                    requestContentType: JSON
            )
        }
    }

}
