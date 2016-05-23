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

/**
 * Created by Magnus Ekstrand on 2016-05-09.
 * Holds paths to page files for easy inclusion and intellisense support in specs.
 */
'use strict';

var minaintygBase = require('./minaintyg.base.page.js');
var welcomePage = require('./welcome.page.js');
var startPage = require('./start.page.js');
var consentPage = require('./consent.page.js');
var aboutPage = require('./about.page.js');
var errorPage = require('./error.page.js');

var intygPath = './intyg/'; // should point to intyg folder
//var luae_fs = require(intygPath + 'luae_fs.intyg.page.js')

module.exports = {
    'minaintygBase': minaintygBase,
    'welcomePage': welcomePage,
    'startPage': startPage,
    'consentPage': consentPage,
    'aboutPage': aboutPage,
    'errorPage': errorPage
//    'intyg': {
//        'luae_fs': luae_fs
//    }
};