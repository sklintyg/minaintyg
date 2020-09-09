/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('minaintyg').directive('miHeader',
    ['MIConfig', 'MIUser', 'common.ObjectHelper', '$uibModal', '$state', 'sessionCheckService',
      function(MIConfig, MIUser, objectHelper, $uibModal, $state, sessionCheckService) {
        'use strict';

        return {
          restrict: 'E',
          templateUrl: '/app/components/miHeader/miHeader.directive.html',
          link: function($scope) {
            if (!objectHelper.isEmpty(MIUser.fullName)) {
              $scope.authUserDescription = MIUser.fullName;
            } else {
              $scope.authUserDescription = MIUser.personId;
            }

            if ($scope.authUserDescription) {
              sessionCheckService.startPolling();
            }

            $scope.userHasSekretessmarkering = MIUser.sekretessmarkering;

            $scope.applicationLogoutUrl = MIConfig.applicationLogoutUrl;

            $scope.showSekretessInfoMessage = function() {
              $uibModal.open({
                templateUrl: '/app/components/miHeader/miHeaderSekretess.dialog.html',
                keyboard: false,
                controller: function($scope, $uibModalInstance) {

                  $scope.onKeydown = function(e) {
                    if (e.keyCode === 27) {
                      $uibModalInstance.close();
                      e.preventDefault();
                      $scope.$emit('dialogOpen', false);
                    }
                  };

                  $scope.dialog = {
                    focus: true
                  };
                  $scope.$emit('dialogOpen', true);

                  $scope.gotoAbout = function() {
                    $uibModalInstance.close();
                    $scope.$emit('dialogOpen', false);
                    $state.go('omminaintyg.juridik');
                  };
                  $scope.close = function() {
                    $uibModalInstance.close();
                    $scope.$emit('dialogOpen', false);
                  };
                }
              });
            };
          }
        };
      }]);
