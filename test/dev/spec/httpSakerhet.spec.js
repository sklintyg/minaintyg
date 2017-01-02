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
var genericTestDataBuilder = miTestTools.testdata.generic;

var welcomePage = miTestTools.pages.welcomePage;
var inboxPage = miTestTools.pages.inboxPage;

var request = require('request');

describe('Http-säkerhet', function() {

    var fk7263IntygsId = null;

    beforeAll(function() {
        // Givet samtycke
        restHelper.setConsent();

        var fk7263Intyg = genericTestDataBuilder.getFk7263();
        fk7263IntygsId = fk7263Intyg.id;
        restHelper.createIntyg(fk7263Intyg);
    });

    afterAll(function() {
        restHelper.deleteConsent();
        restHelper.deleteIntyg(fk7263IntygsId);
    });

    describe('Verifiera headers', function() {

        beforeEach(function() {
            browser.ignoreSynchronization = false;
        });

        it('Api svar ska ej kunna cachas', function() {
            restHelper.get('api/certificates/map', true).then(function(response) {
                expect(response.statusCode).toEqual(200);
                expect(response.headers['cache-control']).toEqual('no-cache, no-store, max-age=0, must-revalidate');
            }, function(error) {
                fail('Error calling api ' + error);
            });
        });

        it('ModuleApi svar ska ej kunna cachas', function() {
            restHelper.get('moduleapi/certificate/fk7263/' + fk7263IntygsId, true).then(function(response) {
                expect(response.statusCode).toEqual(200);
                expect(response.headers['cache-control']).toEqual('no-cache, no-store, max-age=0, must-revalidate');
            }, function(error) {
                fail('Error calling moduleapi ' + error);
            });
        });

        it('En sida ska inte kunna sättas i frame', function() {
            restHelper.get('web/start', true).then(function(response) {
                expect(response.statusCode).toEqual(200);
                expect(response.headers['x-frame-options']).toEqual('DENY');
            }, function(error) {
                fail('Error calling start ' + error);
            });
        });

    });

});
