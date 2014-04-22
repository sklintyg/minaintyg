define([
], function () {
    'use strict';

    return ['$http', '$log', function ($http, $log) {

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
                //give calling code a chance to handle error 
                callback(null);
            });
        }

        function _archiveCertificate(item, callback) {
            $log.debug("Archiving " + item.id);

            $http.put('/api/certificates/' + item.id + "/archive").success(function(data) {
                //Kill the cache
                cachedList = null;

                callback(data, item);
            }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                //give calling code a chance to handle error 
                callback(null);
            });

        }

        function _restoreCertificate(item, callback) {
            $log.debug("restoring " + item.id);
            $http.put('/api/certificates/' + item.id + "/restore").success(function(data) {
                callback(data, item);
            }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                //give calling code a chance to handle error 
                callback(null);
            });

        }

        // Return public API for our service
        return {
            getCertificates : _getCertificates,
            archiveCertificate : _archiveCertificate,
            restoreCertificate : _restoreCertificate,
            selectedCertificate : _selectedCertificate
        }
    }];
});
