package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public interface ObservationsKoder {

    // Observationskategorier
    Kod DIAGNOS = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "439401001");
    Kod KROPPSFUNKTIONER = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "b");
    Kod AKTIVITETER_OCH_DELAKTIGHET = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "d");

    // Observationskoder
    Kod ARBETSFORMAGA = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "302119000");
    Kod FORLOPP = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "288524001");
    Kod SJUKDOM = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "39104002");
    Kod GRAVIDITET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "289908002");
    Kod KOMPLIKATION_VID_GRAVIDITET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "90821003");
}
