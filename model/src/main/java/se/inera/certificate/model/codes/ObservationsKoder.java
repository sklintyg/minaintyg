package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public interface ObservationsKoder {
    Kod ARBETSFORMAGA = new Kod("Arbetsförmåga");
    Kod NEDSATTNING = new Kod("Nedsättning");
    Kod BEDOMT_TILLSTAND = new Kod("BedömtTillstånd");
    Kod KROPPSFUNKTION = new Kod("Kroppsfunktion");
    Kod MEDICINSKT_TILLSTAND = new Kod("MedicinsktTillstånd");
    Kod AKTIVITET = new Kod("Aktivitet");
}
