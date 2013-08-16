package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public interface ObservationsKoder {
    Kod ARBETSFORMAGA_NEDSATT = new Kod("X.Y.Z", "TEMPORARY", null, "Arbetsformaga_nedsatt");
    Kod ANAMNES = new Kod("X.Y.Z", "TEMPORARY", null, "Anamnes");
    Kod KROPPSFUNKTIONER = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "b");
    Kod AKTIVITETER_OCH_DELAKTIGHET = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "d");
    Kod DIAGNOS = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "439401001");
}
