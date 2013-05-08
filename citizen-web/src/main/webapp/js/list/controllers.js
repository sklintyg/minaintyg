'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', '$location', 'listCertService', function ListCertCtrl($scope, $filter, $location, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    $scope.sendSelected = function() {
        var items = $filter('filter')($scope.certificates, {
            selected : true
        });
        console.log("send " + items.length);
        listCertService.selectedCertificate = items[0];
        $location.path("/skicka-intyg");

    }

    $scope.archiveSelected = function() {
        var items = $filter('filter')($scope.certificates, {
            selected : true
        });
        listCertService.selectedCertificate = items[0];
        console.log("archive " + listCertService.selectedCertificate.id);
        listCertService.archiveCertificate(listCertService.selectedCertificate, function(updatedItem) {
            console.log("statusUpdate callback:" + updatedItem);
            // Better way to update the object?
            listCertService.selectedCertificate.status = updatedItem.status;
            listCertService.selectedCertificate.statusStyled = updatedItem.statusStyled;
            listCertService.selectedCertificate.selected = false;

        });
    }

    // fetch list of certs initially
    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
        // filtering is done i view
        $scope.doneLoading = true;
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', '$location', 'listCertService', function ListCertCtrl($scope, $location, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;
    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    $scope.restoreCert = function(certId) {
        console.log("Restore requested for cert:" + certId);
        for ( var i = 0; i < $scope.certificates.length; i++) {
            if ($scope.certificates[i].id == certId) {
                listCertService.selectedCertificate = $scope.certificates[i];
                listCertService.restoreCertificate(listCertService.selectedCertificate, function(updatedItem) {
                    console.log("(restore) statusUpdate callback:" + updatedItem);
                    // Better way to update the object?
                    listCertService.selectedCertificate.status = updatedItem.status;
                    listCertService.selectedCertificate.statusStyled = updatedItem.statusStyled;
                    listCertService.selectedCertificate.selected = false;

                });

            }

        }
    }

    listCertService.getCertificates(function(list) {
        // filtering of deleted certs is done in view template
        $scope.certificates = list;
        $scope.doneLoading = true;
    });
} ]);

// Send Certification Controller
listCertApp.controller('SendCertCtrl', [ '$scope', '$filter', 'listCertService', function ListCertCtrl($scope, $filter, listCertService) {
    $scope.certificates = [];

    $scope.certToSend = listCertService.selectedCertificate;

} ]);
