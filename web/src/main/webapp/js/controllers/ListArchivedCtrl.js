angular.module('minaintyg').controller('minaintyg.ListArchivedCtrl',
    [ '$location', '$log', '$scope', 'common.dialogService', 'minaintyg.listCertService',
        function($location, $log, $scope, dialogService, listCertService) {
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
                restoreDialog = dialogService.showDialog({
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

                        listCertService.restoreCertificate($scope.certificates[i], statusCallback);
                    }
                }
            };

            listCertService.getCertificates(function(list) {
                // filtering of deleted certs is done in view template
                $scope.certificates = list;
                $scope.doneLoading = true;
            });

            $scope.pagefocus = true;
        }]);
