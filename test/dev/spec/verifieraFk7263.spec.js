/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*globals browser */
/*globals describe, beforeAll, afterAll, it */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Visa intyg Fk7263', function() {

    var personId = '19010101-0101';
    var intygsId = null;

    beforeAll(function() {
        restHelper.setConsent(personId);

        var fk7263Intyg = genericTestDataBuilder.getFk7263(personId);
        intygsId = fk7263Intyg.id;
        restHelper.createIntyg(fk7263Intyg);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
        restHelper.deleteIntyg(intygsId);
    });

    describe('Verifiera Fk7263', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
        });

        it('Visa ett intyg och verifiera att det är rätt intyg och av rätt typ', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(intygsId)).toBeTruthy();
            inboxPage.viewCertificate(intygsId);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('fk7263');
            expect(viewPage.certificateId()).toEqual(intygsId);
        });

        it('Verifiera patient och utfärdare', function() {
            expect(viewPage.getTextContent('patient-name')).toEqual('Test Testorsson');
            expect(viewPage.getTextContent('patient-crn')).toEqual(personId);
            expect(viewPage.getTextContent('careunit')).toEqual('WebCert Enhet 1');
            expect(viewPage.getTextContent('caregiver')).toEqual('WebCert Vårdgivare 1');
        });

        it('Verifiera fält 1: Smittskydd', function() {
            expect(viewPage.fieldNotShown('smittskydd-yes')).toBeTruthy();
            expect(viewPage.getTextContent('smittskydd-no')).toEqual('Nej');
        });

        it('Verifiera fält 2: Diagnoskod och -beskrivning', function() {
            expect(viewPage.getTextContent('diagnosKod')).toEqual('S47');
            expect(viewPage.getTextContent('diagnosBeskrivning')).toEqual('Klämskada på skuldra och överarm. Samsjuklighet föreligger. Z233 Vaccination avseende pest. Z600 Problem med anpassning till övergångar i livscykeln. Här förtydligar vi våra diagnoser ytterligare och skriver litegrann för att göra saker tydligare för andra som ska läsa på tex Försäkringskassan.');
        });

        it('Verifiera fält 3: Aktuell sjukdomsförlopp', function() {
            expect(viewPage.getTextContent('sjukdomsforlopp')).toEqual('Patienten klämde höger överarm vid olycka i hemmet. Problemen har pågått en längre tid.');
        });

        it('Verifiera fält 4: Funktionsnedsättning', function() {
            expect(viewPage.getTextContent('funktionsnedsattning')).toEqual('Kraftigt nedsatt rörlighet i överarmen pga skadan. Böj- och sträckförmågan är mycket dålig. Smärtar vid rörelse vilket ger att patienten inte kan använda armen särkilt mycket.');
        });

        it('Verifiera fält 4b: Intyg baseras på', function() {
            expect(viewPage.getTextContent('undersokningAvPatienten')).toEqual('Min undersökning av patienten den 1 april 2013');
            expect(viewPage.getTextContent('telefonkontaktMedPatienten')).toEqual('Min telefonkontakt med patienten den 1 april 2013');
            expect(viewPage.getTextContent('journaluppgifter')).toEqual('Journaluppgifter, den 1 april 2013');
            expect(viewPage.fieldNotShown('annanReferens')).toBeTruthy();
        });

        it('Verifiera fält 5: Aktivitetsbegränsning', function() {
            expect(viewPage.getTextContent('aktivitetsbegransning')).toEqual('Patienten bör/kan inte använda armen förrän skadan läkt. Skadan förvärras vid för tidigt påtvingad belastning. Patienten kan inte lyfta armen utan den ska hållas riktad nedåt och i fast läge så mycket som möjligt under tiden för läkning.');
        });

        it('Verifiera fält 6a: Rekommendationer', function() {
            expect(viewPage.getTextContent('rekommendationKontaktArbetsformedlingen')).toEqual('Kontakt med Arbetsförmedlingen');
            expect(viewPage.getTextContent('rekommendationKontaktForetagshalsovarden')).toEqual('Kontakt med företagshälsovården');
            expect(viewPage.getTextContent('rekommendationOvrigt')).toEqual('När skadan förbättrats rekommenderas muskeluppbyggande sjukgymnastik');
        });

        it('Verifiera fält 6b: Planerad eller pågående behandling/åtgärd', function() {
            expect(viewPage.getTextContent('atgardSjukvard')).toEqual('Utreds om operation är nödvändig');
            expect(viewPage.getTextContent('atgardAnnan')).toEqual('Patienten ansvarar för att armen hålls i stillhet');
        });

        it('Verifiera fält 7: Arbetsinriktad rehabilitering', function() {
            expect(viewPage.fieldNotShown('rehabilitering-yes')).toBeTruthy();
            expect(viewPage.fieldNotShown('rehabilitering-no')).toBeTruthy();
            expect(viewPage.getTextContent('rehabilitering-unjudgeable')).toEqual('Går inte att bedöma');
        });

        it('Verifiera fält 8a: Arbetsförmåga bedömning', function() {
            expect(viewPage.getTextContent('nuvarandeArbetsuppgifter')).toEqual('Dirigent. Dirigerar en större orkester på deltid');
            expect(viewPage.getTextContent('arbetslos')).toEqual('Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden');
            expect(viewPage.getTextContent('foraldraledig')).toEqual('Föräldraledighet med föräldrapenning - att vårda sitt barn');
        });

        it('Verifiera fält 8b: Arbetsförmåga nedsatthet', function() {
            expect(viewPage.getTextContent('nedsattMed25')).toEqual('Nedsatt med 1/4\nFrån och med 1 april 2011 – längst till och med 31 maj 2011');
            expect(viewPage.getTextContent('nedsattMed50')).toEqual('Nedsatt med hälften\nFrån och med 7 mars 2011 – längst till och med 31 mars 2011');
            expect(viewPage.getTextContent('nedsattMed75')).toEqual('Nedsatt med 3/4\nFrån och med 14 februari 2011 – längst till och med 6 mars 2011');
            expect(viewPage.getTextContent('nedsattMed100')).toEqual('Helt nedsatt\nFrån och med 26 januari 2011 – längst till och med 13 februari 2011');
        });

        it('Verifiera fält 9: Arbetsförmåga nedsatthet längre tid', function() {
            expect(viewPage.getTextContent('arbetsformagaPrognos')).toEqual('Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.');
        });

        it('Verifiera fält 10: Prognos', function() {
            expect(viewPage.fieldNotShown('arbetsformagaPrognos-yes')).toBeTruthy();
            expect(viewPage.fieldNotShown('arbetsformagaPrognos-partialyes')).toBeTruthy();
            expect(viewPage.fieldNotShown('arbetsformagaPrognos-no')).toBeTruthy();
            expect(viewPage.getTextContent('arbetsformagaPrognos-unjudgeable')).toEqual('Går inte att bedöma');
        });

        it('Verifiera fält 11: Resor till och från arbetet', function() {
            expect(viewPage.fieldNotShown('resaTillArbetet-yes')).toBeTruthy();
            expect(viewPage.getTextContent('resaTillArbetet-no')).toEqual('Nej');
        });

        it('Verifiera fält 12: Kontakt önskas med FK', function() {
            expect(viewPage.getTextContent('kontaktFk-yes')).toEqual('Ja');
            expect(viewPage.fieldNotShown('kontaktFk-no')).toBeTruthy();
        });

        it('Verifiera fält 13: Övriga upplysningar', function() {
            expect(viewPage.getTextContent('kommentar')).toEqual('Prognosen att återgå till arbete är svår att bedömma förrän utredningen är genomförd.');
        });

        it('Verifiera fält 14: Signeringsdatum', function() {
            expect(viewPage.getTextContent('signeringsdatum')).toEqual('26 maj 2016');
        });

        it('Verifiera fält 15: Skapad av', function() {
            expect(viewPage.getTextContent('vardperson-namn')).toEqual('Jan Nilsson');
            expect(viewPage.getTextContent('vardperson-enhet')).toEqual('WebCert Enhet 1');
            expect(viewPage.getTextContent('vardperson-postadress')).toEqual('Lasarettsvägen 13');
            expect(viewPage.getTextContent('vardperson-postnummer-ort')).toEqual('85150 Sundsvall');
            expect(viewPage.getTextContent('vardperson-telefonnummer')).toEqual('060-1818000');
        });

        it('Verifiera fält 17: Förskrivarkod och arbetsplatskod', function() {
            expect(viewPage.getTextContent('forskrivarkodOchArbetsplatskod')).toEqual('1234567 - 123456789011');
        });

    });

});
