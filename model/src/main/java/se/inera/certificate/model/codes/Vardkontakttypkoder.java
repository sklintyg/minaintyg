package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Vardkontakttypkoder {
    public static final Kod MIN_UNDERSOKNING_AV_PATIENTEN = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "5880005");
    public static final Kod MIN_TELEFONKONTAKT_MED_PATIENTEN = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "185317003");

    public static final Map<Kod, String> mapToFk7263 = new HashMap<Kod, String>();

    static {
        mapToFk7263.put(MIN_UNDERSOKNING_AV_PATIENTEN, "Min_undersokning_av_patienten");
        mapToFk7263.put(MIN_TELEFONKONTAKT_MED_PATIENTEN, "Min_telefonkontakt_med_patienten");
    }
}
