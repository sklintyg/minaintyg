package se.inera.certificate.spec

import groovy.xml.XmlUtil
import groovyx.net.http.RESTClient
import org.springframework.core.io.ClassPathResource
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
         document: document()
        ]
    }

    private document() {
        if (typ == 'FK7263') {
            fk7263Document()
        }
        else {
            '<certificate></certificate>'
        }

    }

    private fk7263Document() {
        // slurping the FK7263 template
        def registerMedicalCertificate = new XmlSlurper().parse(new ClassPathResource("fk7263_template.xml").getInputStream())

        // setting the certificate ID
        registerMedicalCertificate.lakarutlatande.'lakarutlatande-id' = id

        // setting personnr in certificate XML
        registerMedicalCertificate.lakarutlatande.patient.'person-id'.setProperty('@extension', personnr)

        // setting the signing date, from date and to date
        registerMedicalCertificate.lakarutlatande.signeringsdatum = datum
        registerMedicalCertificate.lakarutlatande.signeringsdatum = datum
        registerMedicalCertificate.lakarutlatande.funktionstillstand.arbetsformaga.arbetsformagaNedsattning.varaktighetFrom = datum
        registerMedicalCertificate.lakarutlatande.funktionstillstand.arbetsformaga.arbetsformagaNedsattning.varaktighetTom = datum


        XmlUtil.serialize(registerMedicalCertificate);
    }

}
