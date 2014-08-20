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

        function _getRecipients(type, onSuccess, onError) {
            $log.debug('_getRecipients type: ' + type);
            var restPath = '/api/certificates/' + type + '/recipients';
            $http.get(restPath).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function (data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
        }

        // Return public API for our service
        return {
            sendCertificate : _sendCertificate,
            getRecipients: _getRecipients
        }
    }];
});
