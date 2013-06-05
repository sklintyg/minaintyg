'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', '$location', '$window', 'listCertService', function ListCertCtrl($scope, $filter, $location, $window, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    $scope.initialDisplaySize = 9;
    $scope.currentDisplaySize = 9;

    $scope.sendSelected = function(item) {
        console.log("send " + item.id);
        listCertService.selectedCertificate = item;
        var path = "/" + item.type.toLowerCase() + "/intyg/" + item.id
        $window.location.href = path;
    }

    $scope.archiveSelected = function(item) {
        console.log("archive " + item.id);
        listCertService.archiveCertificate(item, function(fromServer, oldItem) {
            console.log("statusUpdate callback:" + fromServer);
            // Better way to update the object?
            oldItem.archived = fromServer.archived;
            oldItem.status = fromServer.status;
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

// Consent Controller
listCertApp.controller('AboutCtrl', [ '$scope', '$location', '$filter', 'consentService', '$window', function ConsentCtrl($scope, $location, $filter, consentService, $window) {

    // Hold left side navigation state
    $scope.visibility = {
        "omminaintyg" : true,
        "samtycke" : false,
        "juridik" : false
    }

    $scope.navigateTo = function(section) {
        angular.forEach($scope.visibility, function(value, key) {
            $scope.visibility[key] = (key == section) ? true : false;
        });
    }

    $scope.opts = {
        backdropFade : true,
        dialogFade : true
    };
    
    $scope.openConfirmDialog = function() {
        $scope.shouldBeOpen = true;
    };
    
    $scope.closeConfirmDialog = function(confirm) {
        $scope.shouldBeOpen = false;
        console.log("closeConfirmDialog " + confirm);
        if (confirm) {
            console.log("revoking consent..");
            revokeConsent();
        }
    };

    function revokeConsent() {
        consentService.revokeConsent(function(data) {
            console.log("revokeConsent callback:" + data);
            if (data.result) {
                $window.location.href = "/web/start";
            } else {
                $location.path("/fel");
            }

        });
    };

} ]);
