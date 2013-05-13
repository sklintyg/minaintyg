'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', '$location', 'listCertService', function ListCertCtrl($scope, $filter, $location, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    $scope.initialDisplaySize = 9;
    $scope.currentDisplaySize = 9;

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

        console.log("archive " + items[0]);
        listCertService.archiveCertificate(items[0], function(fromServer, oldItem) {
            console.log("statusUpdate callback:" + fromServer);
            // Better way to update the object?
            oldItem.archived = fromServer.archived;
            oldItem.status = fromServer.status;
            oldItem.statusTranslated = fromServer.statusTranslated;
            oldItem.selected = false;

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
    $scope.initialDisplaySize = 9;
    $scope.currentDisplaySize = 9;

    $scope.restoreCert = function(certId) {
        console.log("Restore requested for cert:" + certId);
        for ( var i = 0; i < $scope.certificates.length; i++) {
            if ($scope.certificates[i].id == certId) {

                listCertService.restoreCertificate($scope.certificates[i], function(fromServer, oldItem) {
                    console.log("(restore) statusUpdate callback:" + fromServer);
                    // Better way to update the object?
                    oldItem.archived = fromServer.archived;
                    oldItem.status = fromServer.status;
                    oldItem.selected = false;

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
listCertApp.controller('SendCertCtrl', [ '$scope', '$location', '$filter', 'listCertService', function ListCertCtrl($scope, $location, $filter, listCertService) {
    $scope.certificates = [];

    $scope.certToSend = listCertService.selectedCertificate;
    // changes to certToSend is propagated to other controllers via the service
    // scope
    $scope.sendConfirmed = function() {

        console.log("sending " + $scope.certToSend.id);
        listCertService.sendCertificate($scope.certToSend, function(fromServer, oldItem) {
            console.log("(send) statusUpdate callback:" + fromServer);
            // Better way to update the object?
            oldItem.status = fromServer.status;
            oldItem.statusTranslated = fromServer.statusTranslated;
            oldItem.selected = false;
            $location.path("/lista");

        });

    }

} ]);
