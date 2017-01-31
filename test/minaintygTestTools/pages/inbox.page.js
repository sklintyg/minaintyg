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

/* globals browser */

/**
 * Created by erik
 */

'use strict';
var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var MinaintygStartPageBase = MinaintygBasePage._extend({

    init: function init() {
        init._super.call(this);
        this.at = element(by.id('inboxHeader'));
    },
    get: function () {
        this.getPage('start');
    },
    isAt: function isAt() {
        specHelper.waitForAngularTestability();
        return isAt._super.call(this);
    },
    certificateTableIsShown: function() {
        return element(by.id('certTable')).isPresent();
    },
    noCertificatesIsShown: function() {
        return element(by.id('noCerts')).isPresent();
    },
    certificateExists: function(intygId) {
        return element(by.id('certificate-' + intygId)).isPresent();
    },
    complementaryInfo: function(intygId) {
        return element(by.id('certificate-period-' + intygId)).getText();
    },
    cancelledCertificateDisplayed: function(intygId) {
        return element(by.id('viewCertificateBtn-' + intygId)).isDisplayed().then(function (isVisible) { return !isVisible; });
    },
    viewCertificate: function(intygId) {
        element(by.id('viewCertificateBtn-' + intygId)).click();
    },
    archiveCertificate: function(intygId) {
        element(by.id('archiveCertificateBtn-' + intygId)).click();
    },
    confirmArchiveCertificate: function() {
        element(by.id('archive-button')).click();
    },
    archiveDialogIsDisplayed: function() {
        return element(by.id('archive-confirmation-dialog')).isDisplayed();
    },
    eventExists: function(intygId, eventId) {
        var attrId = 'event-' + intygId + (eventId === undefined ? '' : '-' + eventId);
        return element(by.id(attrId)).isPresent();
    },
    eventHasText: function(text, intygId, eventId) {
        var attrId = 'event-' + intygId + (eventId === undefined ? '' : '-' + eventId);
        var el = element(by.id(attrId));
        return el.getText().then(function(txt) {
            return txt === text;
        });
    },
    showEvents: function(intygId) {
        element(by.id('showEventsBtn-' + intygId)).click();
    }

});

module.exports = new MinaintygStartPageBase();
