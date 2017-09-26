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
 * Created by Magnus Ekstrand 2016-05-09
 */

'use strict';

var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var MinaintygConsentPage = MinaintygBasePage._extend({

    init: function init() {
        init._super.call(this);
        this.at = element(by.id('consentTerms'));
        this.giveConsent = element(by.id('giveConsentButton'));
        this.confirmConsent = element(by.id('giveConsentCheckbox'));
    },
    get: function () {
        return browser.get('web/start');
    },
    isAt: function isAt() {
        specHelper.waitForAngularTestability();
        return isAt._super.call(this);
    },
    clickConfirmConsent: function() {
        this.confirmConsent.click();
    },
    clickGiveConsent: function() {
        this.giveConsent.click();
        // Wait for page reload
        browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /.*inkorg/.test(url);
            });
        }, 10000);
        specHelper.waitForAngularTestability();
    }

});

module.exports = new MinaintygConsentPage();
