package se.inera.certificate.spec

import groovyx.net.http.RESTClient
import se.inera.certificate.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON

public class TaBortIntyg extends RestClientFixture {

    String personnr
    String id
    String idTemplate
    int from
    int to

	private String template
	
	public void reset() {
		template = null
	}
	
    public void execute() {
		if (from && to && personnr && !idTemplate) {
			template = "test-${personnr}-intyg-%1\$s"
		} else if (idTemplate) {
			template = idTemplate
		}
        def restClient = new RESTClient(baseUrl)
        for (i in from..to) {
            if (template) {
                id = String.format(template, i)
            }
            restClient.delete(
                    path: 'certificate/' + id,
                    requestContentType: JSON
            )
        }
    }

}
