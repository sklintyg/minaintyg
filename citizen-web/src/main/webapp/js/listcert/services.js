'use strict';

/* Services */

listCertApp.factory('listCertService', [ '$http', function($http) {
    return {

        getCertificates : function(callback) {
            $http.get('/api/certificates').success(function(data) {
                console.log(data.certificateList[0].date);
                callback(data.certificateList);
            }).error(function(data, status, headers, config) {
                console.log("error");
            });
        }
    }
} ]);

// angular.module('listcertServices', ['ngResource']).
// factory('CertificateMeta', function($resource){
// return $resource('/api/certificates/list:testId.json', {}, {
// query: {method:'GET', params:{testId:'123456'}, isArray:true}
// });
// });
