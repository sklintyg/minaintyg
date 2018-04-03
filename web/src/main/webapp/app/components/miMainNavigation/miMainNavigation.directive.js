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

angular.module('minaintyg').directive('miMainNavigation', function($rootScope, $state) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            linkPrefix: '@',
            defaultActive: '@'
        },
        controller: function($scope) {
            //The about section has substates, but this component is only interested in mathing against root states.
            function _getRootStateName(stateName) {
                return stateName.split('.')[0];
            }

            $scope.navClass = function(page) {
                if (angular.isString($scope.defaultActive)) {
                    if (page === $scope.defaultActive) {
                        return 'active';
                    }
                }
                var currentRootState = $rootScope.keepInboxTab ? 'inkorg' : _getRootStateName($state.current.name) || 'inkorg';

                return (page === currentRootState) ? 'active' : '';
            };
            $scope.showMenu = function(){
                return $state.current.name !== 'index';
            };
        },
        templateUrl: '/app/components/miMainNavigation/miMainNavigation.directive.html'
    };
});
