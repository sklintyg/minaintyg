package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 *
 */
public interface Referenstypkoder {
    Kod JOURNALUPPGIFT = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "419891008");
    Kod ANNAT = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "74964007");
}
