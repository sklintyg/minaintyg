package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 *
 */
public interface Sysselsattningskoder {
    Kod NUVARANDE_ARBETE = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "224375002");
    Kod ARBETSLOSHET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "73438004");
    Kod FORALDRALEDIGHET = new Kod("X.Y.Z", "TEMPORARY", null, "Foraldraledigt");
}
