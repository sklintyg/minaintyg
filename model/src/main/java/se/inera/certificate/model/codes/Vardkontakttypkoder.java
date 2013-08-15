package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 *
 */
public interface Vardkontakttypkoder {
    Kod MIN_UNDERSOKNING_AV_PATIENTEN = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "5880005");
    Kod MIN_TELEFONKONTAKT_MED_PATIENTEN = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "185317003");
}
