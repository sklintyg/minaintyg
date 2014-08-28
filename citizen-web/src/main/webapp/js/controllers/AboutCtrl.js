angular.module('minaintyg').controller('minaintyg.AboutCtrl',
    [ '$filter', '$location', '$log', '$scope', '$window', 'minaintyg.consentService', 'minaintyg.dialogService',
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
