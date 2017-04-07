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

angular.module('minaintyg').controller('minaintyg.AboutCtrl',
    [ '$filter', '$location', '$log', '$scope', '$window', 'minaintyg.consentService', 'common.dialogService',
        'common.messageService',
        function($filter, $location, $log, $scope, $window, consentService, dialogService, messageService) {
            'use strict';

            // Hold left side navigation state
            $scope.visibility = {
                omminaintyg: true,
                samtycke: false,
                juridik: false
            };

            // Hold focus state for sub pages
            $scope.subpagefocus = {
                omminaintyg: false,
                samtycke: false,
                juridik: false
            };

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            $scope.navigateTo = function(section) {
                angular.forEach($scope.visibility, function(value, key) {
                    $scope.visibility[key] = (key === section) ? true : false;
                    $scope.subpagefocus[key] = (key === section) ? true : false;
                });
            };

            $scope.getMessage = function(key) {
                return messageService.getProperty(key);
            };

            // Revoke dialog

            var revokeDialog = {};

            $scope.openRevokeDialog = function() {
                $scope.dialog.focus = true;
                revokeDialog = dialogService.showDialog($scope, {
                    dialogId: 'revoke-consent-confirmation-dialog',
                    titleId: 'about.revokemodal.header',
                    bodyTextId: 'about.revokemodal.text',
                    button1click: function() {
                        $log.debug('revoking consent..');
                        $scope.dialog.acceptprogressdone = true;
                        revokeConsent();
                    },
                    button1id: 'revoke-consent-button',
                    button1text: 'about.revokeconsent.button.label',
                    autoClose: false
                });
            };

            function revokeConsent() {
                consentService.revokeConsent(function(data) {
                    $log.debug('revokeConsent callback:' + data);
                    $scope.dialog.acceptprogressdone = false;
                    if (data !== null && data.result) {
                        $window.location.href = '/web/start';
                    } else {
                        $location.path('/fel/couldnotrevokeconsent');
                    }
                });
            }

            $scope.pagefocus = true;
        }]);
