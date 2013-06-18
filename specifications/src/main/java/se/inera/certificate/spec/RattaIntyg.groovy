package se.inera.certificate.spec

import iso.v21090.dt.v1.II

import org.joda.time.LocalDateTime

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderService
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType

/**
 *
 * @author andreaskaltenbach
 */
class RattaIntyg extends WsClientFixture {

    private RevokeMedicalCertificateResponderService revokeService = new RevokeMedicalCertificateResponderService();
    private RevokeMedicalCertificateResponderInterface revokeResponder = revokeService.revokeMedicalCertificateResponderPort

    String personnummer
    String intyg

    public RattaIntyg() {
        setEndpoint(revokeResponder, "revoke-certificate/v1.0")
    }

    public void execute() {
        RevokeMedicalCertificateRequestType revokeRequestType = new RevokeMedicalCertificateRequestType()
        RevokeType revokeType = new RevokeType();
        revokeRequestType.setRevoke(revokeType)
        revokeType.vardReferensId = 1
        revokeType.avsantTidpunkt = new LocalDateTime("2013-05-01T11:00:00")
        revokeType.meddelande = "Makulerat"
        revokeType.adressVard = new VardAdresseringsType()
        revokeType.adressVard.hosPersonal = new HosPersonalType()
        revokeType.adressVard.hosPersonal.personalId = new II()
        revokeType.adressVard.hosPersonal.personalId.extension = "personalid"
        revokeType.adressVard.hosPersonal.enhet = new EnhetType()
        revokeType.adressVard.hosPersonal.enhet.enhetsId = new II()
        revokeType.adressVard.hosPersonal.enhet.enhetsId.extension = "1"
        revokeType.adressVard.hosPersonal.enhet.enhetsnamn = "Enhetsnamn"
        revokeType.adressVard.hosPersonal.enhet.vardgivare = new VardgivareType()
        revokeType.adressVard.hosPersonal.enhet.vardgivare.vardgivareId = new II()
        revokeType.adressVard.hosPersonal.enhet.vardgivare.vardgivareId.extension = "1"
        revokeType.adressVard.hosPersonal.enhet.vardgivare.vardgivarnamn = "Vårdgivarnamn"
        
        revokeType.lakarutlatande = new LakarutlatandeEnkelType()
        revokeType.lakarutlatande.lakarutlatandeId = intyg
        revokeType.lakarutlatande.signeringsTidpunkt = new LocalDateTime("2013-05-01T11:00:00")
        revokeType.lakarutlatande.patient = new PatientType()
        revokeType.lakarutlatande.patient.personId = new II()
        revokeType.lakarutlatande.patient.personId.extension = personnummer

        
        RevokeMedicalCertificateResponseType response = revokeResponder.revokeMedicalCertificate(null, revokeRequestType)
    }
}
