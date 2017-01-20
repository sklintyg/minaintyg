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

angular.module('minaintyg').controller('minaintyg.ListCtrl',
    [ '$cookies', '$location', '$log', '$rootScope', '$scope', '$window', 'common.IntygListService',
        'common.dialogService', 'common.messageService', 'common.moduleService',
        function($cookies, $location, $log, $rootScope, $scope, $window, IntygListService, dialogService,
            messageService, moduleService) {
            'use strict';

            $scope.certificates = [];
            $scope.doneLoading = false;
            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };
            $scope.isCollapsedArchive = true;
            $scope.isCollapsedRevoke = true;
            $scope.messageService = messageService;
            $scope.moduleService = moduleService;
            $scope.pageTitle = 'Inkorgen';

            var archiveDialog = {};

            $scope.sendSelected = function(item) {
                $log.debug('send ' + item.id);
                IntygListService.selectedCertificate = item;
                $rootScope.keepInboxTab = true;
                $location.path('/' + item.type.toLowerCase() + '/view/' + item.id);
                $log.info( 'item path str: /' + item.type.toLowerCase() + '/view/'  + item.id );

            };

            $scope.archiveSelected = function() {
                var item = $scope.certToArchive;
                $log.debug('archive ' + item.id);
                $scope.dialog.acceptprogressdone = false;
                IntygListService.archiveCertificate(item, function(fromServer, oldItem) {
                    $log.debug('statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        oldItem.archived = fromServer.archived;
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
                archiveDialog = dialogService.showDialog( $scope, {
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
            var fromConsentPage = $cookies.get('RedirectFromConsent');
            if (fromConsentPage && ($rootScope.MI_CONFIG.LOGIN_METHOD === 'FK')) {
                dialogService.showDialog($scope, {
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
                $cookies.remove('RedirectFromConsent');
                // ...and close dialog
            };

            // Compile event status message text
            $scope.getEventText = function(type, sender, receiver, timestamp) {
                var text = '';

                if (receiver) {
                    receiver = messageService.getProperty('certificates.target.' + receiver.toLowerCase());
                }
                if (timestamp) {
                    timestamp = moment(timestamp).format("YYYY-MM-DD HH:mm")
                }

                console.log(timestamp);
                if (type === 'CANCELLED') {
                    text = messageService.getProperty('certificates.status.cancelled')
                        .replace('//TIMESTAMP//', timestamp);
                }
                else if (type === 'RECEIVED') {
                    text = messageService.getProperty('certificates.status.received')
                        .replace('//SENDER//', sender)
                        .replace('//RECEIVER//', receiver)
                        .replace('//TIMESTAMP//', timestamp);
                }
                else if (type === 'SENT') {
                    text = messageService.getProperty('certificates.status.sent')
                        .replace('//SENDER//', sender)
                        .replace('//RECEIVER//', receiver)
                        .replace('//TIMESTAMP//', timestamp);
                }

                return text.length === 0 ? messageService.getProperty('certificates.status.noevent') : text;
            }

            // Fetch list of certs initially
            IntygListService.getCertificates(function(list) {
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
