package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Aktivitetskoder {
    public static final Kod PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT1");
    public static final Kod PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT2");
    public static final Kod ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT3");
    public static final Kod ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT4");
    public static final Kod GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT5");
    public static final Kod PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT6");
    public static final Kod PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT7");
    public static final Kod FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT8");
    public static final Kod FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT9");
    public static final Kod KONTAKT_MED_FK_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT10");
    public static final Kod AVSTANGNING_ENLIGT_SML_PGA_SMITTA = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT11");
    public static final Kod OVRIGT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT12");

    public static final Map<Kod, String> mapToFk7263 = new HashMap<Kod, String>();

    static {
        mapToFk7263.put(ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, "Arbetslivsinriktad_rehabilitering_ar_aktuell");
        mapToFk7263.put(ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL,"Arbetslivsinriktad_rehabilitering_ar_ej_aktuell");
        mapToFk7263.put(GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, "Gar_ej_att_bedomma_om_arbetslivsinriktad_rehabilitering_ar_aktuell");
        mapToFk7263.put(FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT, "Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt");
        mapToFk7263.put(FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT, "Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt");
        mapToFk7263.put(PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN, "Planerad_eller_pagaende_behandling_eller_atgard_inom_sjukvarden");
        mapToFk7263.put(PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD, "Planerad_eller_pagaende_annan_atgard");
        mapToFk7263.put(KONTAKT_MED_FK_AR_AKTUELL, "Kontakt_med_Forsakringskassan_ar_aktuell");
        mapToFk7263.put(PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN, "Patienten_behover_fa_kontakt_med_foretagshalsovarden");
        mapToFk7263.put(AVSTANGNING_ENLIGT_SML_PGA_SMITTA, "Avstangning_enligt_SmL_pga_smitta");
        mapToFk7263.put(OVRIGT, "Ovrigt");
        mapToFk7263.put(PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN, "Patienten_behover_fa_kontakt_med_Arbetsformedlingen");
    }
}
