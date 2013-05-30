'use strict';
/**
 * Service used to functions regarding certificates, such as getCertificate, Send Certificate etc.
 * Can be used by the modules as a common component.
 *
 * 
 *
 */
angular.module('services.certService', []);
angular.module('services.certService').factory('certService', [ '$http', '$rootScope', function(http, rootScope) {

    var _selectedCertificate = null;

    function _getCertificate(id, callback) {
        http.get(rootScope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + id).success(function(data) {
            console.log("got certificate data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            console.log("error " + status);
            callback(null);
        });
    }

    //TODO:send certificate function
    
    // Return public API for our service
    return {
        getCertificate : _getCertificate
    }
} ]);