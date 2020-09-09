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

angular.module('minaintyg').controller('minaintyg.ListCtrl',
    ['$location', '$log', '$rootScope', '$scope', '$filter', 'common.IntygListService', 'common.dialogService',
      'common.messageService', 'common.moduleService',
      function($location, $log, $rootScope, $scope, $filter, IntygListService, dialogService,
          messageService, moduleService) {
        'use strict';

        $scope.activeCertificates = [];
        $scope.doneLoading = false;
        $scope.dialog = {
          acceptprogressdone: true,
          focus: false
        };
        $scope.isCollapsedRevoke = true;
        $scope.messageService = messageService;
        $scope.moduleService = moduleService;
        $scope.pageTitle = 'Inkorgen';
        $scope.errorMessage = null;

        var archiveDialog = {};

        $scope.sendSelected = function(item) {
          $log.debug('send ' + item.id);
          IntygListService.selectedCertificate = item;
          $rootScope.keepInboxTab = true;
          var path = '/' + item.type.toLowerCase() + '/' + item.typeVersion + '/view/' + item.id;
          $location.path(path);
          $log.debug('item path str: ' + path);

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
              $scope.refreshActiveCertificates();
              archiveDialog.close();
              $scope.dialog.acceptprogressdone = true;
            } else {
              // show error view
              archiveDialog.close();
              dialogService.showDialog($scope, {
                dialogId: 'archive-error-dialog',
                titleId: 'error.generictechproblem.title',
                bodyText: messageService.getProperty('error.modal.couldnotarchivecert', {intygsId:item.id}),
                button1text: 'error.modal.btn.back-to-inkorg',
                templateUrl: '/app/partials/error-dialog.html',
                autoClose: true
              });
            }
          });
        };

        // Archive dialog
        $scope.certToArchive = {};

        $scope.openArchiveDialog = function(cert) {
          $scope.certToArchive = cert;
          archiveDialog = dialogService.showDialog($scope, {
            dialogId: 'archive-confirmation-dialog',
            titleId: 'inbox.archivemodal.header',
            bodyTextId: 'inbox.archivemodal.text',
            button1click: function() {
              $scope.archiveSelected();
            },
            button1id: 'archive-button',
            button1text: 'button.archive',
            button1icon: 'icon-box',
            autoClose: false
          });
        };

        $scope.refreshActiveCertificates = function() {
          $scope.activeCertificates = $filter('unarchived')($scope.activeCertificates);
          var currentYear = null;
          angular.forEach($scope.activeCertificates, function(cert) {

            //If next cert is on a different year - it should show a year-divider
            var certYear = $filter('date')(cert.sentDate, 'yyyy');
            cert.yearDivider = currentYear && (currentYear !== certYear) ? certYear : null;

            //Update year to compare next with
            currentYear = certYear;
          });

        };

        // Fetch list of certs initially
        IntygListService.getCertificates(function(list) {
          $scope.doneLoading = true;
          $scope.errorMessage = null;
          if (list !== null) {
            $scope.activeCertificates = list;
            $scope.refreshActiveCertificates();
          } else {
            // show error view
            $scope.errorMessage = 'error.couldnotloadcertlist';
          }
        });

        // Set focus on new page so screen readers can announce it
        $scope.pagefocus = true;
      }]);
