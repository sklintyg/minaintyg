package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Referenstypkoder {
    public static final Kod JOURNALUPPGIFT = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "419891008");
    public static final Kod ANNAT = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "74964007");

    public static final Map<Kod, String> mapToFk7263 = new HashMap<Kod, String>();

    static {
        mapToFk7263.put(JOURNALUPPGIFT, "Journaluppgifter");
        mapToFk7263.put(ANNAT, "Annat");
    }
}
