package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public interface ObservationsKoder {

    Kod SJUKDOMSFORLOPP = new Kod("BedömtTillstånd/Sjukdomsförlopp");
    Kod DIAGNOS = new Kod("Diagnos");
    Kod AKTIVITET = new Kod("Aktivitet");
    Kod KROPPSFUNKTION = new Kod("Kroppsfunktion");
}
