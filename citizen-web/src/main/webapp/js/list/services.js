'use strict';

/* Services */

/*
 * This service gets a list of all certificates. It also caches the list to
 * avoid having to fetch the list every time a controller requests it.
 */
angular.module('services.listCertService', []);
angular.module('services.listCertService').factory('listCertService', [ '$http', '$log', function($http, $log) {


    // cached certificates response
    var cachedList = null;

    var _selectedCertificate = null;

    function _getCertificates(callback) {
        if (cachedList != null) {
            $log.debug("returning cached response");
            callback(cachedList);
            return;
        }
        $http.get('/api/certificates').success(function(data) {
            $log.debug("populating cache");
            cachedList = data;
            callback(cachedList);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
        });
    }

    function _archiveCertificate(item, callback) {
        $log.debug("Archiving " + item.id);
        $http.put('/api/certificates/' + item.id + "/archive").success(function(data) {
            callback(data, item);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
        });

    }

    function _restoreCertificate(item, callback) {
        $log.debug("restoring " + item.id);
        $http.put('/api/certificates/' + item.id + "/restore").success(function(data) {
            callback(data, item);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
        });

    }
    
    function _sendCertificate(item, callback) {
        $log.debug("service: sending " + item.id);
        $http.put('/api/certificates/' + item.id + "/send").success(function(data) {
            callback(data, item);
        }).error(function(data, status, headers, config) {
            $log.error("error " + status);
        });

    }

    // Return public API for our service
    return {
        getCertificates : _getCertificates,
        archiveCertificate : _archiveCertificate,
        restoreCertificate : _restoreCertificate,
        sendCertificate : _sendCertificate,
        selectedCertificate : _selectedCertificate
    }
} ]);
