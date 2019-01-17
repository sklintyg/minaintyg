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
 * Created by BESA on 2015-11-25.
 */
/*globals protractor */
'use strict';

var restUtil = require('./../util/rest.util.js');
var textHelper =  require('./textHelper.js')

module.exports = {

    createIntyg: function(createJson) {
        debug(createJson);
        return restUtil.createIntyg(createJson);
    },
    deleteIntyg: function(id) {
        return restUtil.deleteIntyg(id);
    },
    deleteAllIntygForCitizen: function(userId) {
        return restUtil.deleteAllIntyg(userId || '191212121212');
    },
    createApprovedReceivers: function(intygsId, createJson) {
        debug(createJson);
        return restUtil.createApprovedReceivers(intygsId, createJson);
    },
    deleteApprovedReceivers: function(intygsId) {
        return restUtil.deleteApprovedReceivers(intygsId);
    },
    get: function(url, loggedIn) {
        return restUtil.get(url, loggedIn);
    },
    getTextResource: function(name) {
        var deferred = protractor.promise.defer();
        // Load and cache expected dynamictext-values for this intygstype.
        restUtil.getResource('classpath:texts/' + name).then(
            function (response) {
                textHelper.parseTextXml(response.body).then(
                    function (response) {
                        deferred.fulfill(response);
                    }, function (err) {
                        deferred.reject('Error during text parse: ' + JSON.stringify(err));
                    });
            }, function (err) {
                deferred.reject('Error while fetching resource: ' + JSON.stringify(err));
            });

        return deferred.promise;
    }

};
