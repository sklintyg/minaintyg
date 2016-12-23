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

/*globals browser,protractor */
/*globals pages */
/*globals describe,it,helpers */
'use strict';

var specHelper = miTestTools.helpers.spec;
var restHelper = miTestTools.helpers.rest;

var welcomePage = miTestTools.pages.welcomePage;
var viewPage = miTestTools.pages.viewPage;
var inboxPage = miTestTools.pages.inboxPage;
var archivedPage = miTestTools.pages.archivedPage;

var genericTestDataBuilder = miTestTools.testdata.generic;

describe('Arkivera samt återställ intyg', function() {

	var personId = '19010101-0101';
	var fk7263IntygsId = null;
	var tsBasIntygsId = null;
	var tsDiabetesIntygsId = null;

    beforeAll(function() {
        // Givet samtycke
        restHelper.setConsent(personId);

        var fk7263Intyg = genericTestDataBuilder.getFk7263(personId);
        fk7263IntygsId = fk7263Intyg.id;
        restHelper.createIntyg(fk7263Intyg);

        var tsBasIntyg = genericTestDataBuilder.getTsBas(personId);
        tsBasIntygsId = tsBasIntyg.id;
        restHelper.createIntyg(tsBasIntyg);

        var tsDiabetesIntyg = genericTestDataBuilder.getTsDiabetes(personId);
        tsDiabetesIntygsId = tsDiabetesIntyg.id;
        restHelper.createIntyg(tsDiabetesIntyg);
    });

    afterAll(function() {
        restHelper.deleteConsent(personId);
        restHelper.deleteIntyg(fk7263IntygsId);
        restHelper.deleteIntyg(tsBasIntygsId);
        restHelper.deleteIntyg(tsDiabetesIntygsId);
    });

    describe('Arkivera intyg', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Logga in', function() {
            welcomePage.get();
            specHelper.waitForAngularTestability();
            welcomePage.login(personId, false);
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
        });

        it('Intyget ska finnas i listan', function() {
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeTruthy();
        });

        it('Arkivera intyg', function() {
            expect(inboxPage.isAt()).toBeTruthy();
            inboxPage.archiveCertificate(fk7263IntygsId);
            expect(inboxPage.archiveDialogIsDisplayed()).toBeTruthy();
            inboxPage.confirmArchiveCertificate();
        });

        it('Intyget ska inte längre finnas i listan', function() {
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeFalsy();
        });

        it('Intyget finns i listan med arkiverade intyg', function() {
            inboxPage.viewArchivedList();
            specHelper.waitForAngularTestability();
            expect(archivedPage.isAt()).toBeTruthy();
            expect(archivedPage.certificateExists(fk7263IntygsId)).toBeTruthy();
        });

        it('Återställ intyg', function() {
            expect(archivedPage.isAt()).toBeTruthy();
            archivedPage.restoreCertificate(fk7263IntygsId);
            expect(archivedPage.restoreDialogIsDisplayed()).toBeTruthy();
            archivedPage.confirmRestoreCertificate();
        });

        it('Intyget ska inte längre finnas i listan med arkiverade intyg', function() {
            expect(archivedPage.certificateExists(fk7263IntygsId)).toBeFalsy();
        });

        it('Intyget finns i listan i inkorgen', function() {
            archivedPage.viewInbox();
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeTruthy();
        });
    });

    describe('Arkivera intyg från visa Fk7263', function() {

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
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeTruthy();
            inboxPage.viewCertificate(fk7263IntygsId);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('fk7263');
            expect(viewPage.certificateId()).toEqual(fk7263IntygsId);
        });

        it('Arkivera intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.archiveCertificate();
            expect(viewPage.archiveDialogIsDisplayed()).toBeTruthy();
            viewPage.confirmArchiveCertificate();
        });

        it('Verifera att intyget inte längre finns i listan men att det finns i listan med arkiverade intyg', function() {
            viewPage.viewInbox();
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(fk7263IntygsId)).toBeFalsy();
            inboxPage.viewArchivedList();
            specHelper.waitForAngularTestability();
            expect(archivedPage.isAt()).toBeTruthy();
            expect(archivedPage.certificateExists(fk7263IntygsId)).toBeTruthy();
        });
    });

    describe('Arkivera intyg från visa TS-bas', function() {

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
            expect(inboxPage.certificateExists(tsBasIntygsId)).toBeTruthy();
            inboxPage.viewCertificate(tsBasIntygsId);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('ts-bas');
            expect(viewPage.certificateId()).toEqual(tsBasIntygsId);
        });

        it('Arkivera intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.archiveCertificate();
            expect(viewPage.archiveDialogIsDisplayed()).toBeTruthy();
            viewPage.confirmArchiveCertificate();
        });

        it('Verifera att intyget inte längre finns i listan men att det finns i listan med arkiverade intyg', function() {
            viewPage.viewInbox();
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(tsBasIntygsId)).toBeFalsy();
            inboxPage.viewArchivedList();
            specHelper.waitForAngularTestability();
            expect(archivedPage.isAt()).toBeTruthy();
            expect(archivedPage.certificateExists(tsBasIntygsId)).toBeTruthy();
        });
    });

    describe('Arkivera intyg från visa TS-diabetes', function() {

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
            expect(inboxPage.certificateExists(tsDiabetesIntygsId)).toBeTruthy();
            inboxPage.viewCertificate(tsDiabetesIntygsId);
            expect(viewPage.isAt()).toBeTruthy();
            expect(browser.getCurrentUrl()).toContain('ts-diabetes');
            expect(viewPage.certificateId()).toEqual(tsDiabetesIntygsId);
        });

        it('Arkivera intyget', function() {
            expect(viewPage.isAt()).toBeTruthy();
            viewPage.archiveCertificate();
            expect(viewPage.archiveDialogIsDisplayed()).toBeTruthy();
            viewPage.confirmArchiveCertificate();
        });

        it('Verifera att intyget inte längre finns i listan men att det finns i listan med arkiverade intyg', function() {
            viewPage.viewInbox();
            specHelper.waitForAngularTestability();
            expect(inboxPage.isAt()).toBeTruthy();
            expect(inboxPage.certificateExists(tsDiabetesIntygsId)).toBeFalsy();
            inboxPage.viewArchivedList();
            specHelper.waitForAngularTestability();
            expect(archivedPage.isAt()).toBeTruthy();
            expect(archivedPage.certificateExists(tsDiabetesIntygsId)).toBeTruthy();
        });
    });

});
