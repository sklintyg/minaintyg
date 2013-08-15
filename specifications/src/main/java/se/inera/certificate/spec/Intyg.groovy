package se.inera.certificate.spec

import static groovyx.net.http.ContentType.JSON
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

import org.springframework.core.io.ClassPathResource

import se.inera.certificate.spec.util.RestClientFixture

public class Intyg extends RestClientFixture {

    String personnr
    String utfärdat
	String giltigtFrån
	String giltigtTill
	String utfärdare
	String enhet
    String typ
    String id
    String idTemplate
	String mall = "M"
    int from
    int to
	
	public void reset() {
		mall = "M"
	}
	
    public void execute() {
        def restClient = new RESTClient(baseUrl)
        for (int day in from..to) {
            if (idTemplate) {
                id = String.format(idTemplate, day, utfärdat, personnr, typ)
            }
            restClient.post(
                    path: 'certificate',
                    body: certificateJson(),
                    requestContentType: JSON
                    )
            utfärdat = new Date().parse("yyyy-MM-dd", utfärdat).plus(1).format("yyyy-MM-dd")
        }
    }

    private certificateJson() {
        [id:String.format(id, utfärdat),
            type:typ,
            civicRegistrationNumber:personnr,
            signedDate:utfärdat,
            signingDoctorName: utfärdare,
            validFromDate:giltigtFrån,
            validToDate:giltigtTill,
            careUnitName: enhet,
			states:
			  [
				 [state:"RECEIVED",
				  target:"MI",
				  timestamp:"2013-08-05T14:30:03.227"
				 ]
			  ],
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
        def certificate = new JsonSlurper().parse(new InputStreamReader(new ClassPathResource("${typ}_${mall}_template.json").getInputStream()))

        // setting the certificate ID
        certificate.'id'.extension = id

        // setting personnr in certificate XML
        certificate.patient.'id'.extension = personnr

		certificate.skapadAv.namn = utfärdare
		certificate.skapadAv.vardenhet.'id'.extension = enhet
		certificate.skapadAv.vardenhet.namn = enhet
		
        // setting the signing date, from date and to date
        certificate.signeringsDatum = utfärdat
        certificate.skickatDatum = utfärdat

        certificate.vardkontakter.each {
			it.vardkontaktstid.start = utfärdat
			it.vardkontaktstid.end = utfärdat
		}

        certificate.referenser.each { it.datum = utfärdat }

		/*
        certificate.aktivitetsbegransningar.arbetsformaga.arbetsformagaNedsattningar[0].each {
            it.varaktighetFrom = giltigtFrån
            it.varaktighetTom = giltigtTill
        }
        */
        JsonOutput.toJson(certificate)
    }
}
