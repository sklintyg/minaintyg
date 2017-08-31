/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('minaintyg').controller('minaintyg.SendCtrl',
    [ '$filter', '$location', '$state', '$stateParams', '$scope', '$uibModal', 'minaintyg.SendService', 'common.IntygService',
        function($filter, $location, $state, $stateParams, $scope, $uibModal, sendService, intygService) {
            'use strict';

            var dialogInstance;

            $scope.vm = {
                id: $stateParams.certificateId,
                cert : null,
                meta: null,
                statuses: null,
                type: $stateParams.type,
                defaultRecipient: $stateParams.defaultRecipient,
                recipients: [],
                sendingInProgress: false,
                initializing: true
            };

            function _decorateWithSentStatus(statuses, recipient) {
                // check if selected recipient exists with SENT status in cert history
                var sentToThisRecipientStatus = null;
                if (angular.isArray(statuses)) {
                    angular.forEach(statuses, function(status) {
                        if (status.type === 'SENT' && status.target === recipient.id) {
                            sentToThisRecipientStatus = status;
                        }
                    });
                }

                if (sentToThisRecipientStatus) {
                    recipient.sent = true;
                    recipient.sentTimestamp = sentToThisRecipientStatus.timestamp;
                } else {
                    recipient.sent = false;
                    recipient.sentTimestamp = undefined;
                }
            }

            intygService.getCertificate($scope.vm.type, $scope.vm.id, function(result) {
                if (result !== null) {
                    $scope.vm.cert = result.utlatande;
                    $scope.vm.meta = result.meta;
                    $scope.vm.statuses = result.meta.statuses;

                    //Also need the list of recipients
                    sendService.getRecipients($scope.vm.type).then(function(result) {
                        if (result && result.data) {
                            angular.forEach(result.data, function(recipient) {
                                _decorateWithSentStatus($scope.vm.statuses, recipient);
                                $scope.vm.recipients.push(recipient);
                            });
                        }
                    }, function() {
                        $state.go('fel', {errorCode: 'generic'});
                    }).finally(function() { // jshint ignore:line
                        $scope.vm.initializing = false;
                    });


                } else {
                    // show error view
                    $state.go('fel', {errorCode: 'certnotfound'});
                }
            }, function() {
                $state.go('fel', {errorCode: 'certnotfound'});
            });




            $scope.doSend = function(selectedRecipients) {
                var dialogVm = {
                    sending: true,
                    recipients: selectedRecipients,
                    someFailed: false
                };

                dialogInstance = $uibModal.open({
                    templateUrl: '/app/views/send/sender.dialog.html',
                    backdrop: 'static',
                    keyboard: false,
                    windowClass: 'mi-sending-dialog-window-class',
                    controller: function($scope, $uibModalInstance, vm, onBackToCertificate) {
                        $scope.vm = vm;
                        $scope.onBackToCertificate = onBackToCertificate;


                    },
                    resolve: {
                        onBackToCertificate: function() {
                            return $scope.backToViewCertificate;
                        },
                        vm: function() {
                            return dialogVm;
                        }
                    }
                });

                sendService.sendCertificate($scope.vm.id, selectedRecipients, function(results) {

                    if (results) {
                        //Update status for each recipient
                        angular.forEach(dialogVm.recipients, function(recipient) {
                            //set a default status
                            recipient.status = {
                                sent: false
                            };
                            angular.forEach(results, function(result) {
                                if (result.recipientId === recipient.id) {
                                    recipient.status = result;
                                    if (!recipient.status.sent) {
                                        dialogVm.someFailed = true;
                                    }
                                }
                            });
                        });

                        //reveal results
                        dialogVm.sending = false;

                    } else {
                        $state.go('fel', {errorCode: 'generic'});
                    }

                });

            };



            $scope.backToViewCertificate = function () {
                $location.path($scope.vm.type + '/view/' + $scope.vm.id).search({});
            };
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $scope.vm.type + '/' + $scope.vm.id + '/pdf';


            // Cleanup ---------------
            $scope.$on('$destroy', function() {
                if (dialogInstance) {
                    dialogInstance.close();
                    dialogInstance = undefined;
                }
            });



        }]);
