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

angular.module('minaintyg').factory('minaintyg.SendService', function($http, $log) {
    'use strict';

    function _sendCertificate(certId, recipients, callback) {
        var recipientIds = [];

        angular.forEach(recipients, function(recipient) {
            recipientIds.push(recipient.id);
        });

        $http.put('/api/certificates/' + certId + '/send' , recipientIds).success(function(data) {
            callback(data);
        }).error(function(data, status) {
            $log.error('error ' + status);
            //give calling code a chance to handle error
            callback(null);
        });
    }

    function _getRecipients(type) {
        $log.debug('_getRecipients type: ' + type);
        var restPath = '/api/certificates/' + type + '/recipients';
        var ret = $http.get(restPath);
        angular.forEach(ret, function(utlatandeRecipient) {
           $log.debug('Recipient: ' + utlatandeRecipient.name);
        });
        return ret;
    }

    // Return public API for our service
    return {
        sendCertificate: _sendCertificate,
        getRecipients: _getRecipients
    };
});
