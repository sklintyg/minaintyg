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

angular.module('minaintyg').directive('latestEvents', ['common.messageService',
    function(messageService) {
        'use strict';

        function _getEventText(msgProperty, params) {
            var text = messageService.getProperty(msgProperty, params);
            return text;
        }

        return {
            restrict: 'E',
            replace: true,
            scope: {
                header: '=',
                cert: '='
            },
            templateUrl: '/app/views/list/latestEvents.directive.html',
            link: function(scope) {

                scope.certId = scope.cert.id;
                scope.isCollapsedArchive = true;
                scope.statuses = scope.cert.statuses;

                // Compile event status message text
                scope.getEventText = function(status) {
                    var msgProperty = '';
                    var params = [];

                    var type = status.type;
                    var sender = scope.cert.careunitName;
                    var receiver = messageService.getProperty('certificates.target.' + status.target.toLowerCase());
                    var timestamp = status.timestamp;

                    if (timestamp) {
                        timestamp = moment(timestamp).format('YYYY-MM-DD HH:mm');
                    }

                    if (type.toUpperCase() === 'CANCELLED') {
                        msgProperty = 'certificates.status.cancelled';
                        params.push(timestamp);
                    }
                    else if (type.toUpperCase() === 'RECEIVED') {
                        msgProperty = 'certificates.status.received';
                        params.push.apply(params, [sender, receiver, timestamp]);
                    }
                    else if (type.toUpperCase() === 'SENT') {
                        msgProperty = 'certificates.status.sent';
                        params.push.apply(params, [sender, receiver, timestamp]);
                    }

                    var text = _getEventText(msgProperty, params);
                    return text.length === 0 ? _getEventText('certificates.status.noevents', []) : text;
                };

            }

        };

    }]
);
