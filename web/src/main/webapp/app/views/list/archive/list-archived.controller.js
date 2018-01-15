/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
    [ '$state', '$log', '$scope', 'common.IntygListService', 'common.moduleService', 'common.messageService',
        'common.dialogService',
        function($state, $log, $scope, IntygListService, moduleService, messageService, dialogService) {
            'use strict';

            $scope.archivedCertificates = [];
            $scope.doneLoading = false;
            $scope.errorMessage = null;
            $scope.moduleService = moduleService;
            $scope.messageService = messageService;

            $scope.restoreCert = function(item) {
                $log.debug('Restore requested for cert:' + item.id);
                IntygListService.restoreCertificate(item, function(fromServer, oldItem) {
                    $log.debug('(restore) statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        oldItem.archived = fromServer.archived;
                        oldItem.selected = false;
                    } else {
                        // show error view
                        dialogService.showDialog( $scope, {
                            dialogId: 'restore-error-dialog',
                            titleId: 'error.generictechproblem.title',
                            bodyTextId: 'error.modal.couldnotrestorecert',
                            button1text: 'error.modal.btn.back-to-archive-cert',
                            templateUrl: '/app/partials/error-dialog.html',
                            autoClose: true
                        });
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
                    $scope.errorMessage = 'error.couldnotloadarchivedlist';
                }
            });

            $scope.pagefocus = true;

            $scope.getTypeOrReplacedText = function(item) {
                if (item.replacedBy) {
                    return 'ERSATT INTYG';
                } else {
                    return moduleService.getModuleName(item.type);
                }
            };
        }]);
