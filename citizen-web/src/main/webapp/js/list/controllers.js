'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', 'listCertService', function ListCertCtrl($scope, $filter, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    listCertService.getCertificates(function(list) {
        // filter and just keep those with right status
        $scope.certificates = $filter('bycertstatus')(list, {
            'UNHANDLED' : true,
            'DELETED' : false
        });
        $scope.doneLoading = true;
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', '$filter', 'listCertService', function ListCertCtrl($scope, $filter, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;
    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    $scope.restoreCert = function(certId) {
        console.log("Restore requested for cert:" + certId);
    }
    
    listCertService.getCertificates(function(list) {
        // filter and just keep those with right status
        $scope.certificates = $filter('bycertstatus')(list, {
            'DELETED' : true
        });
        $scope.doneLoading = true;
    });
} ]);
