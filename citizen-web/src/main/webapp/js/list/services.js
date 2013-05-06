'use strict';

/* Services */

/*
 * This service gets a list of all certificates. It also caches the list to
 * avoid having to fetch the list every time a controller requests it.
 */
listCertApp.factory('listCertService', [ '$http', function($http) {

    // cached certificates response
    var cachedList = null;

    function _getCertificates(callback) {
        if (cachedList != null) {
            console.log("returning cached response");
            callback(cachedList);
            return;
        }
        $http.get('/api/certificates').success(function(data) {
            console.log("populating cache");
            cachedList = data.certificateList;
            callback(cachedList);
        }).error(function(data, status, headers, config) {
            console.log("error");
        });
    }
    // Return public API for our service
    return {
        getCertificates : _getCertificates
    }
} ]);
