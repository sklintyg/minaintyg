angular.module('minaintyg').controller('minaintyg.ListCtrl',
    [ '$cookies', '$location', '$log', '$rootScope', '$scope', '$window', 'minaintyg.listCertService',
        'common.dialogService', 'common.messageService',
        function($cookies, $location, $log, $rootScope, $scope, $window, listCertService, dialogService,
            messageService) {
            'use strict';

            $scope.certificates = [];
            $scope.doneLoading = false;
            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };
            $scope.showCookieInfo = false;
            $scope.toggleCookieInfo = function() {
                $scope.showCookieInfo = !$scope.showCookieInfo;
            };

            var archiveDialog = {};

            $scope.messageService = messageService;
            $scope.pageTitle = 'Inkorgen';
            $scope.isCollapsed = true;

            $scope.sendSelected = function(item) {
                $log.debug('send ' + item.id);
                listCertService.selectedCertificate = item;
                $rootScope.keepInboxTab = true;
                $location.path('/' + item.type.toLowerCase() + '/view/' + item.id);
            };

            $scope.archiveSelected = function() {
                var item = $scope.certToArchive;
                $log.debug('archive ' + item.id);
                $scope.dialog.acceptprogressdone = false;
                listCertService.archiveCertificate(item, function(fromServer, oldItem) {
                    $log.debug('statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                        archiveDialog.close();
                        $scope.dialog.acceptprogressdone = true;
                    } else {
                        // show error view
                        $location.path('/fel/couldnotarchivecert');
                    }
                });
            };

            // Archive dialog
            $scope.certToArchive = {};

            $scope.openArchiveDialog = function(cert) {
                $scope.certToArchive = cert;
                $scope.dialog.focus = true;
                archiveDialog = dialogService.showDialog({
                    dialogId: 'archive-confirmation-dialog',
                    titleId: 'inbox.archivemodal.header',
                    bodyTextId: 'inbox.archivemodal.text',
                    button1click: function() {
                        $log.debug('archive');
                        $scope.archiveSelected();
                    },
                    button1id: 'archive-button',
                    button1text: 'button.archive',
                    autoClose: false
                });
            };

            // FK dialog
            var fromConsentPage = $cookies.RedirectFromConsent;
            if (fromConsentPage && ($rootScope.MI_CONFIG.LOGIN_METHOD === 'FK')) {
                dialogService.showDialog({
                    dialogId: 'fk-login-consent-dialog',
                    titleId: 'fkdialog.head',
                    bodyTextId: 'fkdialog.text',
                    button1click: function() {
                        $scope.closeFKDialog(true);
                    },
                    button2click: function() {
                        $scope.closeFKDialog(false);
                    },
                    button1id: 'close-fkdialog-logout-button',
                    button2id: 'close-fkdialog-continue-button',
                    button1text: 'fkdialog.button.returntofk',
                    button2text: 'fkdialog.button.continueuse',
                    autoClose: false
                });
            }

            $scope.closeFKDialog = function(backtoFK) {
                if (backtoFK) {
                    $window.location.href = '/web/logga-ut-fk';
                }
                // no matter outcome of dialog, it should be a one time popup..
                // remove flag indicating forwarded from consent page..
                delete $cookies.RedirectFromConsent;
                // ...and close dialog
            };

            // fetch list of certs initially
            listCertService.getCertificates(function(list) {
                $scope.doneLoading = true;
                if (list !== null) {
                    $scope.certificates = list;
                } else {
                    // show error view
                    $location.path('/fel/couldnotloadcertlist');
                }
            });

            // Set focus on new page so screen readers can announce it
            $scope.pagefocus = true;
        }]);
