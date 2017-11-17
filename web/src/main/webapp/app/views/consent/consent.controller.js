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

angular.module('minaintyg').controller('minaintyg.ConsentCtrl',
    [ '$cookies', '$filter', '$state', '$scope', '$window', 'minaintyg.consentService', 'common.dialogService',
        function($cookies, $filter, $state, $scope, $window, consentService, dialogService) {
            'use strict';

            $scope.consentConfirmed = false;

            $scope.giveConsent = function() {
                consentService.giveConsent(function(data) {
                    // If consent saved OK, proceed to main app and set cookie to indicate that we'e just
                    // given consent, rather than normal login flow
                    if (data !== null && data.result) {
                        $cookies.put('RedirectFromConsent','1');
                        $window.location.href = '/web/start';
                    } else {
                        dialogService.showDialog( $scope, {
                            dialogId: 'consent-error-dialog',
                            titleId: 'error.generictechproblem.title',
                            bodyTextId: 'error.modal.couldnotgiveconsent',
                            templateUrl: '/app/partials/error-dialog.html',
                            autoClose: true
                        });
                    }
                });
            };
        }]);
