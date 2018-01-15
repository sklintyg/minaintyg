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
angular.module('minaintyg').directive('miFooter', [
    '$window', '$state', 'common.dialogService',
    function($window, $state, dialogService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {},
        templateUrl: '/app/components/miFooter/miFooter.directive.html',
        link: function($scope) {

            $scope.openCookieDialog = function(){
                dialogService.showDialog($scope, {
                    dialogId: 'cookie-footer-dialog',
                    titleId: 'footer.cookies.modal.title',
                    bodyTextId: 'footer.cookies.modal.body',
                    button1click: function() {},
                    button1id: 'cookie-button-close',
                    button1text: 'common.close',
                    button2visible: false,
                    autoClose: true
                });
            };
        }
    };
}]);
