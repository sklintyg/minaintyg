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

angular.module('minaintyg').controller('minaintyg.ListArchivedCtrl',
    [ '$location', '$log', '$scope', 'common.dialogService', 'common.IntygListService', 'common.moduleService',
        function($location, $log, $scope, dialogService, IntygListService, moduleService) {
            'use strict';

            $scope.archivedCertificates = [];
            $scope.doneLoading = false;
            $scope.moduleService = moduleService;
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
                var item = $scope.certToRestore;
                $log.debug('Restore requested for cert:' + item.id);
                $scope.dialog.acceptprogressdone = false;

                IntygListService.restoreCertificate(item, function(fromServer, oldItem) {
                    $log.debug('(restore) statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        oldItem.archived = fromServer.archived;
                        oldItem.selected = false;
                        restoreDialog.close();
                        $scope.dialog.acceptprogressdone = true;
                    } else {
                        // show error view
                        $location.path('/fel/couldnotrestorecert');
                    }
                });
            };

            // fetch list of archived certs initially
            IntygListService.getArchivedCertificates(function(list) {
                $scope.doneLoading = true;
                if (list !== null) {
                    $scope.archivedCertificates = list;
                } else {
                    // show error view
                    $location.path('/fel/couldnotloadcertlist');
                }
            });

            $scope.pagefocus = true;
        }]);
