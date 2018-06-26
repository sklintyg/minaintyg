/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global JSON */

angular.module('minaintyg').factory('sessionCheckService',
    ['$http', '$log', '$interval', '$window', function($http, $log, $interval, $window) {
        'use strict';

        var pollPromise;

        //one every minute
        var msPollingInterval = 60 * 1000;

        /*
         * stop regular polling
         */
        function _stopPolling() {
            if (pollPromise) {
                $interval.cancel(pollPromise);
            }
        }

        /*
         * get session status from server
         */
        function _getSessionInfo() {
            $log.debug('_getSessionInfo requesting session info =>');
            $http.get('/session/session-auth-check/ping').then(function(response) {
                $log.debug('<= _getSessionInfo success');
                if (response.data) {
                    $log.debug('session status  = ' + JSON.stringify(response.data));
                    if (response.data.authenticated === false) {
                        $log.debug('No longer authenticated - redirecting to loggedout');
                        _stopPolling();
                        $window.location.href = '/error.jsp?reason=denied';
                    }
                } else {
                    $log.debug('_getSessionInfo returned unexpected data:' + response.data);
                }

                //Schedule new polling
                _stopPolling();
                pollPromise = $interval(_getSessionInfo, msPollingInterval);
            }, function(response) {
                $log.error('_getSessionInfo error ' + response.status);

                _stopPolling();
                //Schedule a new check
                pollPromise = $interval(_getSessionInfo, msPollingInterval);
            });
        }

        /*
         * start regular polling of stats from server
         */
        function _startPolling() {
            $log.debug('sessionCheckService -> Start polling');
            _getSessionInfo();
        }

        // Return public API for the service
        return {
            startPolling: _startPolling,
            stopPolling: _stopPolling
        };
    }]);
