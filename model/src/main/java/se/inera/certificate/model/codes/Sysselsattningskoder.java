package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Sysselsattningskoder {
    public static final Kod NUVARANDE_ARBETE = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "224375002");
    public static final Kod ARBETSLOSHET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "73438004");
    public static final Kod FORALDRALEDIGHET = new Kod("X.Y.Z", "TEMPORARY", null, "Foraldraledigt");

    public static final Map<Kod, String> mapToFk7263 = new HashMap<Kod, String>();

    static {
        mapToFk7263.put(NUVARANDE_ARBETE, "Nuvarande_arbete");
        mapToFk7263.put(ARBETSLOSHET, "Arbetsloshet");
        mapToFk7263.put(FORALDRALEDIGHET, "Foraldraledighet");
    }

}
