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
/*globals pages */
/*globals describe,it,helpers */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;
var textHelper = miTestTools.helpers.text;

var welcomePage = miTestTools.pages.welcomePage;
var consentPage = miTestTools.pages.consentPage;
var inboxPage = miTestTools.pages.inboxPage;
var viewPage = miTestTools.pages.viewPage;

var luae_fs = miTestTools.testdata.luae_fs;

describe('Verifiera LUAE_FS intyg', function() {

    var texts = null;
    var intygsId = null;

    beforeAll(function() {
        //Load and cache expected dynamictext-values for this intygstype.
        textHelper.readTextsFromFkTextFile('texterMU_LUAE_FS_v1.0.xml').then(function(textResources) {
            texts = textResources;
        }, function(err) {
            fail('Error during text lookup ' + err);
        });

        // Ta bort tidigare samtycken
        restHelper.deleteConsent();

        var intyg = luae_fs.getIntyg();
        intygsId = intyg.id;
        restHelper.createIntyg(intyg);
    });

    describe('Logga in', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        // Logga in
        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login();
            specHelper.waitForAngularTestability();
        });

        it('Ge samtycke', function() {
            expect(consentPage.isAt()).toBeTruthy();
            consentPage.clickGiveConsent();
        });

    });

    describe('Verifiera intyg', function() {

        it('Header ska var Inkorgen', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            expect(element(by.id('inboxHeader')).getText()).toBe('Inkorgen');
        });

        it('Intyget ska finnas i listan', function() {
            expect(element(by.id('certificate-' + intygsId)).isPresent());
            expect(element(by.id('viewCertificateBtn-' + intygsId)).isPresent());
        });

        it('Visa intyg', function() {
            inboxPage.viewCertificate('viewCertificateBtn-' + intygsId);
            expect(viewPage.isAt()).toBeTruthy();
        });

        it('Verifiera intygshuvud', function() {
            expect(element(by.id('patient-name')).getText()).toBe('Tolvan Tolvansson');
            expect(element(by.id('patient-crn')).getText()).toBe('191212121212');
            expect(element(by.id('careunit')).getText()).toBe('WebCert Enhet 1');
            expect(element(by.id('caregiver')).getText()).toBe('WebCert Vårdgivare 1');
        });

        it('Verifiera grund för medicinskt underlag', function() {
            expect(viewPage.getDynamicLabelText('KAT_1.RBK')).toBe(texts['KAT_1.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_1.RBK')).toBe(texts['FRG_1.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.UNDERSOKNING.RBK')).toBe(texts['KV_FKMU_0001.UNDERSOKNING.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.JOURNALUPPGIFTER.RBK')).toBe(texts['KV_FKMU_0001.JOURNALUPPGIFTER.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.ANHORIG.RBK')).toBe(texts['KV_FKMU_0001.ANHORIG.RBK']);
            expect(viewPage.getDynamicLabelText('KV_FKMU_0001.ANNAT.RBK')).toBe(texts['KV_FKMU_0001.ANNAT.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_2.RBK')).toBe(texts['FRG_2.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_3.RBK')).toBe(texts['FRG_3.RBK']);

            // Utlåtande baserat på
            expect(element(by.id('undersokningAvPatienten')).element(by.binding('cert.undersokningAvPatienten')).getText()).toBe('| 26 maj 2016');
            expect(element(by.id('journaluppgifter')).element(by.binding('cert.journaluppgifter')).getText()).toBe('| 26 maj 2016');
            expect(element(by.id('anhorigsBeskrivningAvPatienten')).element(by.binding('cert.anhorigsBeskrivningAvPatienten')).getText()).toBe('| 26 maj 2016');
            expect(element(by.id('annatGrundForMU')).element(by.binding('cert.annatGrundForMU')).getText()).toBe('| 26 maj 2016');
            expect(element(by.binding('cert.annatGrundForMUBeskrivning')).getText()).toBe('Uppgifter från habiliteringscentrum.');

            // Känt patienten sedan
            expect(element(by.binding('cert.kannedomOmPatient')).getText()).toBe('20 maj 2016');

            // Andra medicinska underlag
            expect(element(by.id('underlagFinns-yes')).getText()).toBe('Ja');

            expect(element(by.id('underlag_0_typ')).getText()).toBe('Underlag från psykolog');
            expect(element(by.id('underlag_0_datum')).getText()).toBe('3 september 2015');
            expect(element(by.id('underlag_0_hamtasFran')).getText()).toBe('Skickas med posten');
            expect(element(by.id('underlag_1_typ')).getText()).toBe('Underlag från habiliteringen');
            expect(element(by.id('underlag_1_datum')).getText()).toBe('4 november 2015');
            expect(element(by.id('underlag_1_hamtasFran')).getText()).toBe('Arkivet');
        });

        it('Verifiera diagnos', function() {
            expect(viewPage.getDynamicLabelText('KAT_3.RBK')).toBe(texts['KAT_3.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_3.RBK')).toBe(texts['FRG_3.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_6.2.RBK')).toBe(texts['DFR_6.2.RBK']);

            expect(element(by.id('diagnosKod0')).getText()).toBe('S47');
            expect(element(by.id('diagnosBeskrivning0')).getText()).toBe('Klämskada skuldra');
            expect(element(by.id('diagnosKod1')).getText()).toBe('J22');
            expect(element(by.id('diagnosBeskrivning1')).getText()).toBe('Icke specificerad akut infektion i nedre luftvägarna');
        });

        it('Verifiera funktionsnedsättning', function() {
            expect(viewPage.getDynamicLabelText('KAT_4.RBK')).toBe(texts['KAT_4.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_15.RBK')).toBe(texts['FRG_15.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_16.RBK')).toBe(texts['FRG_16.RBK']);

            expect(element(by.binding('cert.funktionsnedsattningDebut')).getText()).toBe('Skoldansen');
            expect(element(by.binding('cert.funktionsnedsattningPaverkan')).getText()).toBe('Haltar när han dansar');
        });

        it('Verifiera övrigt', function() {
            expect(viewPage.getDynamicLabelText('KAT_5.RBK')).toBe(texts['KAT_5.RBK']);
            expect(viewPage.getDynamicLabelText('FRG_25.RBK')).toBe(texts['FRG_25.RBK']);

            expect(element(by.binding('cert.ovrigt')).getText()).toBe('Detta skulle kunna innebära sämre möjlighet att få ställa upp i danstävlingar');
        });

        it('Verifiera kontakt', function() {
            expect(viewPage.getDynamicLabelText('KAT_6.RBK')).toBe(texts['KAT_6.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_26.1.RBK')).toBe(texts['DFR_26.1.RBK']);
            expect(viewPage.getDynamicLabelText('DFR_26.2.RBK')).toBe(texts['DFR_26.2.RBK']);

            expect(element(by.id('kontaktFk-yes')).getText()).toBe('Ja');
            expect(element(by.binding('cert.anledningTillKontakt')).getText()).toBe('Vill stämma av ersättningen');
        });

        it('Verifiera tilläggsfrågor', function() {
            expect(viewPage.getDynamicLabelText('KAT_9999.RBK')).toBe(texts['KAT_9999.RBK']);
            // These test should work when the internal transport format changes
            //expect(viewPage.getDynamicLabelText('DFR_9001.1.RBK')).toBe(texts['DFR_9001.1.RBK']);
            //expect(viewPage.getDynamicLabelText('DFR_9002.1.RBK')).toBe(texts['DFR_9002.1.RBK']);

            expect(element(by.id('tillaggsfraga_0')).getText()).toBe('Tämligen påverkad');
            expect(element(by.id('tillaggsfraga_1')).getText()).toBe('Minst 3 fot');
        });

        it('Verifiera intygsfot', function() {
            expect(element(by.binding('cert.grundData.signeringsdatum')).getText()).toBe('2016-05-26');
            expect(element(by.binding('cert.grundData.skapadAv.fullstandigtNamn')).getText()).toBe('Jan Nilsson');
            expect(element(by.binding('cert.grundData.skapadAv.vardenhet.enhetsnamn')).getText()).toBe('WebCert Enhet 1');
            expect(element(by.binding('cert.grundData.skapadAv.vardenhet.postadress')).getText()).toBe('Enhetsg. 1');
            expect(element(by.binding('cert.grundData.skapadAv.vardenhet.postnummer')).getText()).toBe('100 10 Stadby');
            expect(element(by.binding('cert.grundData.skapadAv.vardenhet.telefonnummer')).getText()).toBe('0812341234');
        });

    });

    afterAll(function() {
        restHelper.deleteIntyg(intygsId);
    });

});
