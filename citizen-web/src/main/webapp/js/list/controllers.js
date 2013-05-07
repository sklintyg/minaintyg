'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
        // filtering is done i view
        $scope.doneLoading = true;
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;
    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    $scope.restoreCert = function(certId) {
        console.log("Restore requested for cert:" + certId);
        for (var i = 0; i < $scope.certificates.length; i++) {
            if ($scope.certificates[i].id == certId) {
                // TODO: do this in callback after successful status update in
                // backend
                $scope.certificates[i].status = "RESTORED";
                console.log("Restoring " + $scope.certificates[i]);
            }

        }
    }

    listCertService.getCertificates(function(list) {
        // filtering of deleted certs is done in view template
        $scope.certificates = list;
        $scope.doneLoading = true;
    });
} ]);
