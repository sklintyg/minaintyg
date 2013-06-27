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
                validToDate:new Date().parse("yyyy-MM-dd", datum).plus(28).format("yyyy-MM-dd"),
         careUnitName: 'Närhälsan i Majorna',
         document: document()
        ]
    }

    private document() {
        if ((typ == 'fk7263')||(typ == 'FK7263')) {
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
        certificate.skickatDatum = datum

        certificate.vardkontakter.each {
            it.vardkontaktstid = datum
        }

        certificate.referenser.each {
            it.datum = datum
        }

        certificate.aktivitetsbegransningar.arbetsformaga.arbetsformagaNedsattningar[0].each {
            it.varaktighetFrom = datum
            it.varaktighetTom = new Date().parse("yyyy-MM-dd", datum).plus(28).format("yyyy-MM-dd");
        }
        JsonOutput.toJson(certificate)
    }

}
