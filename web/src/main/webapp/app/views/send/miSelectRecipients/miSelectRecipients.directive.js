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

angular.module('minaintyg').directive('miSelectRecipients', function() {
  'use strict';

  return {
    restrict: 'E',
    scope: {
      recipients: '=',
      defaultRecipient: '@',
      onSend: '&'
    },
    templateUrl: '/app/views/send/miSelectRecipients/miSelectRecipients.directive.html',
    controller: function($scope, MIUser) {

      $scope.userHasSekretessmarkering = MIUser.sekretessmarkering;

      //Private controller functions  ---------------------------------
      function _select(index) {
        if (index > -1) {
          $scope.selected.push($scope.recipients.splice(index, 1)[0]);
        }

      }

      function _deselect(index) {
        if (index > -1) {
          $scope.recipients.push($scope.selected.splice(index, 1)[0]);
        }

      }

      function _deselectAll() {
        //Move all selected back
        while ($scope.selected.length > 0) {
          _deselect(0);
        }
      }

      function _getIndexById(id, list) {
        if (id && list) {
          for (var i = 0; i < list.length; i++) {
            if (id === list[i].id) {
              return i;
            }
          }
        }
        return -1;
      }

      //Expose functions to template ---------------------------------

      $scope.select = function(event, recipient) {
        if (event) {
          event.preventDefault();
        }
        var index = _getIndexById(recipient.id, $scope.recipients);
        _select(index);
      };

      $scope.deselect = function(event, index) {
        if (event) {
          event.preventDefault();
        }
        _deselect(index);
      };

      $scope.deselectAll = function() {
        _deselectAll();
      };

      //Initialize ---------------------------------

      $scope.selected = [];

      //Handle default recipient selection (if not already sent)
      var preselectionIndex = _getIndexById($scope.defaultRecipient, $scope.recipients);
      if (preselectionIndex > -1 && !$scope.recipients[preselectionIndex].sentTimestamp) {
        _select(preselectionIndex);
      }

    }
  };
});
