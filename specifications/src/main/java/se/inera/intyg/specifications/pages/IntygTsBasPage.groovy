package se.inera.intyg.specifications.pages

class IntygTsBasPage extends IntygPage {

    static content = {
        patientnamn { $("#patient-name") }
        patientpersonnummer { $("#patient-crn") }

        intygAvser { $("#intygAvser") }
        identitet { $("#identitet ") }
        synfaltsdefekter { $("#synfaltsdefekter") } // 1a
        nattblindhet { $("#nattblindhet ") }        // 1b
        progressivOgonsjukdom { $("#progressivOgonsjukdom ") } // 1c
        diplopi { $("#diplopi") }                   // 1d
        nystagmus { $("#nystagmus") }               // 1e
        hogerOgautanKorrektion { $("#hogerOgautanKorrektion") }
        hogerOgamedKorrektion { $("#hogerOgamedKorrektion") }
        hogerOgakontaktlins { $("#hogerOgakontaktlins") }
        vansterOgautanKorrektion { $("#vansterOgautanKorrektion") }
        vansterOgamedKorrektion { $("#vansterOgamedKorrektion") }
        vansterOgakontaktlins { $("#vansterOgakontaktlins") }
        binokulartutanKorrektion { $("#binokulartutanKorrektion") }
        binokulartmedKorrektion { $("#binokulartmedKorrektion") }
        korrektionsglasensStyrka { $("#korrektionsglasensStyrka") }
        horselBalansbalansrubbningar { $("#horselBalansbalansrubbningar") }
        horselBalanssvartUppfattaSamtal4Meter { $("#horselBalanssvartUppfattaSamtal4Meter") }
        funktionsnedsattning { $("#funktionsnedsattning ") }
        funktionsnedsattningbeskrivning { $("#funktionsnedsattningbeskrivning") }
        funktionsnedsattningotillrackligRorelseformaga { $("#funktionsnedsattningotillrackligRorelseformaga") }
        hjartKarlSjukdom { $("#hjartKarlSjukdom") }
        hjarnskadaEfterTrauma { $("#hjarnskadaEfterTrauma") }
        riskfaktorerStroke { $("#riskfaktorerStroke") }
        beskrivningRiskfaktorer { $("#beskrivningRiskfaktorer") }
        harDiabetes { $("#harDiabetes") }
        diabetesTyp { $("#diabetesTyp") }
        kost { $("#kost") }
        tabletter { $("#tabletter") }
        insulin { $("#insulin") }
        neurologiskSjukdom { $("#neurologiskSjukdom") }
        medvetandestorning { $("#medvetandestorning") }
        medvetandestorningbeskrivning { $("#medvetandestorningbeskrivning") }
        nedsattNjurfunktion { $("#nedsattNjurfunktion") }
        sviktandeKognitivFunktion { $("#sviktandeKognitivFunktion") }
        teckenSomnstorningar { $("#teckenSomnstorningar") }
        teckenMissbruk { $("#teckenMissbruk") }
        foremalForVardinsats { $("#foremalForVardinsats") }
        provtagningBehovs { $("#provtagningBehovs") }
        lakarordineratLakemedelsbruk { $("#lakarordineratLakemedelsbruk") }
        lakemedelOchDos { $("#lakemedelOchDos") }
        psykiskSjukdom { $("#psykiskSjukdom") }
        psykiskUtvecklingsstorning { $("#psykiskUtvecklingsstorning") }
        harSyndrom { $("#harSyndrom") }
        stadigvarandeMedicinering { $("#stadigvarandeMedicinering") }
        medicineringbeskrivning { $("#medicineringbeskrivning") }
        kommentar { $("#kommentar") }
        kommentarEjAngivet { $("#kommentarEjAngivet") }
        bedomning { $("#bedomning") }
        bedomningKanInteTaStallning { $("#bedomningKanInteTaStallning") }
        lakareSpecialKompetens { $("#lakareSpecialKompetens") }
        signeringsdatum { $("#signeringsdatum") }
        vardperson_namn { $("#physician-name-id") }
        vardperson_enhetsnamn { $("#careunit-name-id") }
        vardenhet_postadress { $("#careunit-postal_address") }
        vardenhet_postnummer { $("#careunit-postal_code") }
        vardenhet_postort { $("#careunit-postal_city") }
        vardenhet_telefonnummer { $("#careunit-postal_phone") }
    }
}
