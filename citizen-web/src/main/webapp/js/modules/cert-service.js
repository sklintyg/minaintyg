'use strict';
/**
 * Service used to functions regarding certificates, such as getCertificate,
 * Send Certificate etc. Can be used by the modules as a common component.
 * 
 * 
 * 
 */
angular.module('services.certService', []);
angular.module('services.certService').factory('certService', [ '$http', '$rootScope','$log', function(http, rootScope, $log) {

    var _selectedCertificate = null;

    function _getCertificate(id, callback) {
        http.get(rootScope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + id).success(function(data) {
            $log.debug("got certificate data for id " + id);
            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    function _sendCertificate(id, target, callback) {
        $log.debug("send certificate " + id + " to " + target);
        http.put(rootScope.MODULE_CONFIG.MI_COMMON_API_CONTEXT_PATH + id + "/send/" + target).success(function(data) {

            callback(data);
        }).error(function(data, status, headers, config) {
            $log.debug("error " + status);
            callback(null);
        });
    }

    // Return public API for our service
    return {
        getCertificate : _getCertificate,
        sendCertificate : _sendCertificate
    }
} ]);