define([
], function () {
    'use strict';

    return ['$http', '$log', function ($http, $log) {

        function _sendCertificate(certId, recipientId, callback) {
            $http.put('/moduleapi/certificate/' + certId + '/send/' + recipientId, {}).success(function(data) {
                $log.debug("Sending certificate:" + certId + "to target: " + recipientId);
                callback(data);
            }).error(function(data, status, headers, config) {
                $log.error("error " + status);
                //give calling code a chance to handle error 
                callback(null);
            });
        }

        // Return public API for our service
        return {
            sendCertificate : _sendCertificate,
        }
    }];
});
