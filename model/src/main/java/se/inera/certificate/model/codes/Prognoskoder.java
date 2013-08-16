package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Prognoskoder {
    public static final Kod ATERSTALLAS_HELT = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO1");
    public static final Kod ATERSTALLAS_DELVIS = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO2");
    public static final Kod INTE_ATERSTALLAS = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO3");
    public static final Kod DET_GAR_INTE_ATT_BEDOMA = new Kod("3de65a8b-ae2c-48ce-b6fe-35bdd1f60cf7", "kv_prognos_intyg", null, "PRO4");

    public static final Map<Kod, String> mapToFk7263 = new HashMap<Kod, String>();

    static {
        mapToFk7263.put(ATERSTALLAS_HELT, "Aterstallas_helt");
        mapToFk7263.put(ATERSTALLAS_DELVIS, "Aterstallas_delvis");
        mapToFk7263.put(INTE_ATERSTALLAS, "Inte_aterstallas");
        mapToFk7263.put(DET_GAR_INTE_ATT_BEDOMA, "Det_gar_inte_att_bedomma");
    }
}
