/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
 * Created by marced
 */

'use strict';
var specHelper = require('./../helpers/specHelper.js')
var MinaintygBasePage = require('./minaintyg.base.page.js');

var anpassaLisjpPage = MinaintygBasePage._extend({

  init: function init() {
    init._super.call(this);
    this.at = element(by.id('customizeCertificateHeader'));
    this.showsummaryBtn = element(by.id('goto-step-2'));
    this.showSelectionBtn = element(by.id('goto-step-1'));
    this.showDownloadBtn = element(by.id('goto-step-3'));

  },
  isAt: function isAt() {
    specHelper.waitForAngularTestability();
    return isAt._super.call(this);
  },

  clickShowSummary: function() {
    this.showsummaryBtn.click();
  },

  clickShowSelection: function() {
    this.showSelectionBtn.click();
  }
});

module.exports = new anpassaLisjpPage();
