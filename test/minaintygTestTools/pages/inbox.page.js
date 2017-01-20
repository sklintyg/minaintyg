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

by.addLocator('attr',
    /**
     * Find element(s), where attribute = value
     * @param {string} attr
     * @param {string} value
     * @param {Element} [parentElement=]
     * @returns {Array.<Element>}
     */
    function (attr, value, parentElement) {
        parentElement = parentElement || document;
        var nodes = parentElement.querySelectorAll('[' + attr + ']');
        return Array.prototype.filter.call(nodes, function (node) {
            return (node.getAttribute(attr) === value);
        });
    });

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
    eventExists: function(intygId, attr, attrVal) {
        return element(by.id('certificate-' + intygId)).element(by.attr(attr, attrVal)).isPresent();
    },
    eventHasText: function(intygId, attr, attrVal, text) {
        var el = element(by.id('certificate-' + intygId)).element(by.attr(attr, attrVal));
        return el.getText().then(function(txt) {
            return txt === text;
        });
    },
    showEvents: function(intygId) {
        element(by.id('showEventsBtn-' + intygId)).click();
    }

});

module.exports = new MinaintygStartPageBase();
