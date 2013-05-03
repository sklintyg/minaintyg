'use strict';

/* Services */

/*
This service gets a list of all certificates.
 */
listCertApp.factory('listCertService', [ '$http', function ($http) {
    return {
        getCertificates: function (callback) {
            $http.get('/api/certificates').success(function (data) {
                console.log(data.certificateList[0].date);
                callback(data.certificateList);
            }).error(function (data, status, headers, config) {
                    console.log("error");
                });
        }
    }
} ]);
