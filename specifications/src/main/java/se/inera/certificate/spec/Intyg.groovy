package se.inera.certificate.spec
import groovyx.net.http.RESTClient
import se.inera.certificate.spec.util.RestClientFixture

import static groovyx.net.http.ContentType.JSON

public class Intyg extends RestClientFixture {

	String personnr
	String datum
	String typ
	String id

	public void execute() {

        def restClient = new RESTClient(baseUrl)
        restClient.post(
                path: 'certificate',
                body: certificateJson(),
                requestContentType: JSON

        )
	}

    private certificateJson() {
        [id:id,
         type:typ,
         civicRegistrationNumber:personnr,
         signedDate:datum,
         signingDoctorName: 'Doctor Ruth',
                validFromDate:datum,
                validToDate:datum,
         careUnitName: 'Betty Ford Center',
         document: '<certificate></certificate>'
        ]
    }

}
