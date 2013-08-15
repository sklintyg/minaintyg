package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public interface ObservationsKoder {
    @Deprecated Kod ARBETSFORMAGA = new Kod("Arbetsförmåga");
    Kod ARBETSFORMAGA_NEDSATT = new Kod("X.Y.Z", "TEMPORARY", null, "Arbetsformaga_nedsatt");
    @Deprecated Kod NEDSATTNING = new Kod("Nedsättning");

    @Deprecated Kod FUNKTIONSNEDSATTNING = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "21134002");

    @Deprecated Kod BEDOMT_TILLSTAND = new Kod("BedömtTillstånd");
    Kod ANAMNES = new Kod("X.Y.Z", "TEMPORARY", null, "Anamnes");

    @Deprecated Kod KROPPSFUNKTION = new Kod("Kroppsfunktion");
    Kod KROPPSFUNKTIONER = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "b");
    Kod AKTIVITETER_OCH_DELAKTIGHET = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "d");


    @Deprecated Kod MEDICINSKT_TILLSTAND = new Kod("MedicinsktTillstånd");
    Kod DIAGNOS = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "439401001");
    @Deprecated Kod AKTIVITET = new Kod("Aktivitet");
}
