angular.module('minaintyg').factory('minaintyg.consentService',
    function($http, $log) {
        'use strict';

        function _giveConsent(callback) {
            $http.post('/api/certificates/consent/give', {}).success(function(data) {
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // give calling code a chance to handle error
                callback(null);
            });
        }

        function _revokeConsent(callback) {
            $http.post('/api/certificates/consent/revoke', {}).success(function(data) {
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // give calling code a chance to handle error
                callback(null);
            });
        }

        // Return public API for service
        return {
            giveConsent: _giveConsent,
            revokeConsent: _revokeConsent
        };
    });
