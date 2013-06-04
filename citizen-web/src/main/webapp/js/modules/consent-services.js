'use strict';

/* Services */

/*
 * Service to set/revoke consent for a user.
 * 
 */
angular.module('services.consent', []);
angular.module('services.consent').factory('consentService', [ '$http', function($http) {

    function _giveConsent(callback) {
        $http.post('/api/certificates/consent/give', {}).success(function(data) {
            callback(data);
        }).error(function(data, status, headers, config) {
            console.log("error " + status);
        });
    }

    function _revokeConsent(callback) {
        $http.post('/api/certificates/consent/revoke', {}).success(function(data) {
            callback(data);
        }).error(function(data, status, headers, config) {
            console.log("error " + status);
        });
    }

    // Return public API for service
    return {
        giveConsent : _giveConsent,
        revokeConsent : _revokeConsent
    }
} ]);
