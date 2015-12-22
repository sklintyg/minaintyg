/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('minaintyg').controller('minaintyg.ListArchivedCtrl',
    [ '$location', '$log', '$scope', 'common.dialogService', 'common.IntygListService',
        function($location, $log, $scope, dialogService, IntygListService) {
            'use strict';

            $scope.certificates = [];
            $scope.doneLoading = false;

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            // Restore dialog
            var restoreDialog = {};
            $scope.certToRestore = {};

            $scope.openRestoreDialog = function(cert) {
                $scope.certToRestore = cert;
                $scope.dialog.focus = true;
                restoreDialog = dialogService.showDialog($scope, {
                    dialogId: 'restore-confirmation-dialog',
                    titleId: 'archived.restoremodal.header',
                    bodyTextId: 'archived.restoremodal.text',
                    button1click: function() {
                        $scope.restoreCert();
                    },
                    button1id: 'restore-button',
                    button1text: 'label.restore',
                    autoClose: false
                });
            };

            $scope.restoreCert = function() {
                var certId = $scope.certToRestore;
                $log.debug('Restore requested for cert:' + certId);
                $scope.dialog.acceptprogressdone = false;

                var statusCallback = function(fromServer, oldItem) {
                    $log.debug('(restore) statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                        restoreDialog.close();
                        $scope.dialog.acceptprogressdone = true;
                    } else {
                        // show error view
                        $location.path('/fel/couldnotrestorecert');
                    }
                };

                for (var i = 0; i < $scope.certificates.length; i++) {
                    if ($scope.certificates[i].id === certId) {

                        IntygListService.restoreCertificate($scope.certificates[i], statusCallback);
                    }
                }
            };

            IntygListService.getCertificates(function(list) {
                // filtering of deleted certs is done in view template
                $scope.certificates = list;
                $scope.doneLoading = true;
            });

            $scope.pagefocus = true;
        }]);
