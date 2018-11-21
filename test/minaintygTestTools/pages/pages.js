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

var aboutPage = require('./about.page.js');
var accessdeniedPage = require('./accessdenied.page.js');
var archivedPage = require('./archived.page.js');
var errorPage = require('./error.page.js');
var inboxPage = require('./inbox.page.js');
var mvkloginPage = require('./mvklogin.page.js');
var mvklogoutPage = require('./mvklogout.page.js');
var notfoundPage = require('./notfound.page.js');
var sendPage = require('./send.page.js');
var viewPage = require('./view.page.js');
var welcomePage = require('./welcome.page.js');
var anpassaLisjpPage = require('./anpassa.lisjp.page.js');
var anpassaFk7263Page = require('./anpassa.fk7263.page.js');

module.exports = {
    'aboutPage': aboutPage,
    'accessdeniedPage': accessdeniedPage,
    'archivedPage': archivedPage,
    'errorPage': errorPage,
    'inboxPage': inboxPage,
    'mvkloginPage': mvkloginPage,
    'mvklogoutPage': mvklogoutPage,
    'notfoundPage': notfoundPage,
    'sendPage': sendPage,
    'viewPage': viewPage,
    'welcomePage': welcomePage,
    'anpassaLisjpPage': anpassaLisjpPage,
    'anpassaFk7263Page': anpassaFk7263Page
};
