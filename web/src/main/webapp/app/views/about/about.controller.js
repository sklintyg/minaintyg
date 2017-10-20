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

angular.module('minaintyg').controller(
        'minaintyg.AboutCtrl',
        [ '$filter', '$state', '$log', '$scope', '$window', 'minaintyg.consentService', 'common.dialogService', 'common.messageService',
                function($filter, $state, $log, $scope, $window, consentService, dialogService, messageService) {
                    'use strict';


                    // Revoke dialog

                    var revokeDialog = {};

                    $scope.openRevokeDialog = function() {
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
                            button1icon: 'icon icon-lock',
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
                                revokeDialog.close();
                                dialogService.showDialog( $scope, {
                                    dialogId: 'revoke-error-dialog',
                                    titleId: 'error.generictechproblem.title',
                                    bodyTextId: 'error.modal.couldnotrevokeconsent',
                                    templateUrl: '/app/partials/error-dialog.html',
                                    autoClose: true
                                });
                            }
                        });
                    }

                    //faq's are actually arrays of faq items
                    $scope.faqs = messageService.getProperty('help.faq');

                    $scope.menuItems = [];

                    $scope.menuItems.push({
                        id: 'link-about-omminaintyg',
                        link: 'omminaintyg.info',
                        label: 'Om Mina intyg'
                    });

                    $scope.menuItems.push({
                        id: 'link-help-faq',
                        link: 'omminaintyg.faq',
                        label: 'Vanliga frågor och svar'
                    });

                    $scope.menuItems.push({
                        id: 'link-about-samtycke',
                        link: 'omminaintyg.samtycke',
                        label: 'Samtycke'
                    });

                    $scope.menuItems.push({
                        id: 'link-about-juridik',
                        link: 'omminaintyg.juridik',
                        label: 'Lagring och hantering av intyg'
                    });

                    $scope.menuItems.push({
                        id: 'link-help-info',
                        link: 'omminaintyg.help-info',
                        label: 'Hjälp och support'
                    });

                } ]);
