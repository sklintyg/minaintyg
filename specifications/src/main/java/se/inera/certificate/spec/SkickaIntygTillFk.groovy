package se.inera.certificate.spec

import iso.v21090.dt.v1.II

import org.joda.time.LocalDateTime

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType
import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderService
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType

/**
 *
 * @author andreaskaltenbach
 */
class SkickaIntygTillFk extends WsClientFixture {

    private SendMedicalCertificateResponderService sendService = new SendMedicalCertificateResponderService();
    private SendMedicalCertificateResponderInterface sendResponder = sendService.sendMedicalCertificateResponderPort

    String personnummer
    String intyg

    SendMedicalCertificateResponseType response

    public SkickaIntygTillFk() {
        setEndpoint(sendResponder, "send-certificate/v1.0")
    }

    public void execute() {
        SendMedicalCertificateRequestType sendRequestType = new SendMedicalCertificateRequestType()
        SendType sendType = new SendType()
        sendRequestType.setSend(sendType)
        sendType.vardReferensId = 1
        sendType.avsantTidpunkt = new LocalDateTime("2013-05-01T11:00:00")
        sendType.adressVard = new VardAdresseringsType()        
        sendType.adressVard.hosPersonal = new HosPersonalType()
        sendType.adressVard.hosPersonal.personalId = new II()
        sendType.adressVard.hosPersonal.personalId.extension = "personalid"
        sendType.adressVard.hosPersonal.enhet = new EnhetType()
        sendType.adressVard.hosPersonal.enhet.enhetsId = new II()
        sendType.adressVard.hosPersonal.enhet.enhetsId.extension = "1"
        sendType.adressVard.hosPersonal.enhet.enhetsnamn = "Enhetsnamn"
        sendType.adressVard.hosPersonal.enhet.vardgivare = new VardgivareType()
        sendType.adressVard.hosPersonal.enhet.vardgivare.vardgivareId = new II()
        sendType.adressVard.hosPersonal.enhet.vardgivare.vardgivareId.extension = "1"
        sendType.adressVard.hosPersonal.enhet.vardgivare.vardgivarnamn = "Vårdgivarnamn"
        
        sendType.lakarutlatande = new LakarutlatandeEnkelType()
        sendType.lakarutlatande.lakarutlatandeId = intyg
        sendType.lakarutlatande.signeringsTidpunkt = new LocalDateTime("2013-05-01T11:00:00")
        sendType.lakarutlatande.patient = new PatientType()
        sendType.lakarutlatande.patient.personId = new II()
        sendType.lakarutlatande.patient.personId.extension = personnummer
        
        response = sendResponder.sendMedicalCertificate(null, sendRequestType)
    }

    public String svar() {
        resultAsString(response)
    }
}
