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

angular.module('minaintyg').directive('miBreadcrumb', [
    '$rootScope', '$location', '$state', '$stateParams', '$window', 'minaintyg.BreadcrumbConfig',
    function($rootScope, $location, $state, $stateParams, $window, config) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                enableLinks: '=?'
            },
            controller: function($scope, $window) {

                var breadcrumbList = null;
                var backState = null;

                function buildStepsFromStateBreadcrumb() {
                    if($state.current.data){
                        if($state.current.data.breadcrumb){

                            breadcrumbList = $state.current.data.breadcrumb;
                            backState = $state.current.data.backState;

                            // Take icon from the top level breadcrumb
                            if(breadcrumbList.length > 0){
                                $scope.stateIconName = config[breadcrumbList[0]].icon;

                                // Build steps array used to generate list items
                                var steps = [];
                                angular.forEach(breadcrumbList, function(crumbName){

                                    var step = {stateName: crumbName, stateConfig: config[crumbName]};

                                    // If no link function is present use the statename
                                    if(!step.stateConfig.link) {
                                        step.stateConfig.link = step.stateName;
                                    }

                                    if(!$scope.enableLinks) {
                                        step.stateConfig.link = null;
                                    }

                                    this.push(step);
                                }, steps);

                                $scope.steps = steps;
                            }
                        }
                    }
                }

                if(!angular.isDefined($scope.enableLinks)){
                    $scope.enableLinks = true;
                }

                $scope.$watch(function() {
                   return $state.current.data;
                }, function(current, old){
                    buildStepsFromStateBreadcrumb();
                }, true);

                buildStepsFromStateBreadcrumb();

                $scope.back = function(){

                    // Use backstate override if specified, otherwise use next to last breadcrumb as fallback
                    if(angular.isDefined(backState)){

                        // If special state history-back is requested, we back using the browser, otherwise we just trust the specified requested state
                        if(backState === 'history-back'){
                            $window.history.back();
                        } else {
                            $state.go(backState);
                        }
                    } else if(breadcrumbList && breadcrumbList.length > 1){
                        $state.go(breadcrumbList[breadcrumbList.length - 2]);
                    }
                };

                $scope.link = function(step){

                    var goStateParams = { stateName: step.stateName };

                    // Override params if a link function is defined
                    if(typeof config[step.stateName].link === 'function'){
                        goStateParams = config[step.stateName].link($stateParams);
                    }

                    // If no custom statename is passed, use the name of the state itself
                    $state.go(goStateParams.stateName || step.stateName, goStateParams.stateParams);
                };
            },
            templateUrl: '/app/components/miBreadcrumb/miBreadcrumb.directive.html'
        };
    }]);