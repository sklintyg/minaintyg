package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 *
 */
public interface Prognoskoder {
    Kod ATERSTALLAS_HELT = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO1");
    Kod ATERSTALLAS_DELVIS = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO2");
    Kod INTE_ATERSTALLAS = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO3");
    Kod DET_GAR_INTE_ATT_BEDOMA = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO4");
}
