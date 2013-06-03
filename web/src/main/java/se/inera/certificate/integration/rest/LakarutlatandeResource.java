package se.inera.certificate.integration.rest;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.service.CertificateService;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    @Autowired
    private CertificateService certificateService;
    
    @Override
    public String getCertificate(String certificateId) {
        Certificate certificate = certificateService.getCertificate(null, certificateId);
        if (certificate != null) {
            return certificate.getDocument();
        } else {
            return null;
        }
    }

    private Lakarutlatande lakarutlatandeFk7263(String id) {
        Lakarutlatande lakarutlatande = new Lakarutlatande();
        lakarutlatande.setId(id);

        Patient patient = new Patient();
        patient.setId("19001122-3344");
        patient.setFornamn("Erik");
        patient.setEfternamn("Eriksson");
        lakarutlatande.setPatient(patient);
        lakarutlatande.setKommentar("Lite svag i knäna");
        lakarutlatande.setSjukdomsfarlopp("Ramlade i trappan hemma.");
        lakarutlatande.setTyp("fk7263");
        lakarutlatande.setSigneringsdatum(new LocalDateTime());
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId("HOSID");

        lakarutlatande.setSkapadAv(hosPersonal);
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId("vardenhets-id");
        vardenhet.setNamn("Vårdcentrum i väst");
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId("vardgivar-id");
        vardgivare.setNamn("Vård i Väst");
        vardenhet.setVardgivare(vardgivare);
        lakarutlatande.setVardenhet(vardenhet);
        return lakarutlatande;
    }

    private Lakarutlatande lakarutlatande(String id) {
        Lakarutlatande lakarutlatande = new Lakarutlatande();

        lakarutlatande.setId(id);
        lakarutlatande.setTyp("rli");
        Patient patient = new Patient();
        patient.setId("19001122-3344");
        patient.setFornamn("Stig Helmer");
        patient.setEfternamn("Olsson");

        lakarutlatande.setPatient(patient);
        lakarutlatande.setSjukdomsfarlopp("Återkommande panikattacker inför flygningar.");
        lakarutlatande.setKommentar("Flygrädd som bara den!");
        lakarutlatande.setSigneringsdatum(new LocalDateTime());
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId("HOSID");

        lakarutlatande.setSkapadAv(hosPersonal);
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId("vardenhets-id");
        vardenhet.setNamn("Flygrädslecentrum i väst");
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId("vardgivar-id");
        vardgivare.setNamn("Min privata klinik");
        vardenhet.setVardgivare(vardgivare);
        lakarutlatande.setVardenhet(vardenhet);

        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData("{\"resmal\":\"San Francisco\"}");
        lakarutlatande.setOvrigt(ovrigt);

        return lakarutlatande;
    }
}
