package se.inera.certificate.spec

import static groovyx.net.http.ContentType.JSON
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

import org.springframework.core.io.ClassPathResource

import se.inera.certificate.spec.util.RestClientFixture

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
         signingDoctorName: 'Lennart Ström',
                validFromDate:datum,
                validToDate:datum,
         careUnitName: 'Närhälsan i Majorna',
         document: document()
        ]
    }

    private document() {
        if (typ == 'FK7263') {
            "\"" + document("fk7263") + "\""
        }
        else {
            "\"" + document("rli") + "\""
        }

    }

    private document(typ) {
        // slurping the FK7263 template
        def certificate = new JsonSlurper().parse(new InputStreamReader(new ClassPathResource(typ + "_template.json").getInputStream()))

        // setting the certificate ID
        certificate.'id' = id

        // setting personnr in certificate XML
        certificate.patient.'id' = personnr

        // setting the signing date, from date and to date
        certificate.signeringsDatum = datum
        certificate.skickatSatum = datum

        certificate.aktivitetsbegransningar.arbetsformaga.arbetsformagaNedsattningar[0][0].varaktighetFrom = datum
        certificate.aktivitetsbegransningar.arbetsformaga.arbetsformagaNedsattningar[0][0].varaktighetTom = datum

        JsonOutput.toJson(certificate)
    }

}
