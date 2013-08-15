package se.inera.certificate.model.codes;

import se.inera.certificate.model.Kod;

/**
 *
 */
public interface Aktivitetskoder {
    Kod PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT1");
    Kod PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT2");
    Kod ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT3");
    Kod ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT4");
    Kod GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT5");
    Kod PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT6");
    Kod PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT7");
    Kod FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT8");
    Kod FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT9");
    Kod KONTAKT_MED_FK_AR_AKTUELL = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT10");
    Kod AVSTANGNING_ENLIGT_SML_PGA_SMITTA = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT11");
    Kod OVRIGT = new Kod("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null, "AKT12");
}
