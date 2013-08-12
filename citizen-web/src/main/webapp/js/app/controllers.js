'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', '$location', '$window', '$log', 'listCertService', 'messageService', '$cookies', '$rootScope', 
    function ListCertCtrl($scope, $filter, $location, $window, $log, listCertService, messageService, $cookies, $rootScope) {
        $scope.certificates = [];
        $scope.doneLoading = false;
        $scope.messageService = messageService;
        $scope.pageTitle = "Inkorgen";
        $scope.isCollapsed = true;
        
  
        $scope.sendSelected = function (item) {
            $log.debug("send " + item.id);
            listCertService.selectedCertificate = item;
            var path = "/m/" + item.type.toLowerCase() + "/intyg/" + item.id
            $window.location.href = path;
        }

        $scope.archiveSelected = function (item) {
            $log.debug("archive " + item.id);
            listCertService.archiveCertificate(item, function (fromServer, oldItem) {
                $log.debug("statusUpdate callback:" + fromServer);
                if (fromServer != null) {
                    // Better way to update the object?
                    oldItem.archived = fromServer.archived;
                    oldItem.status = fromServer.status;
                    oldItem.selected = false;
                } else {
                    // show error view
                    $location.path("/fel/couldnotarchivecert");
                }
            });
        }

        // Archive dialog
        $scope.archiveOpen = false;
        $scope.dialogfocus = false;
        $scope.certToArchive = {};
        $scope.dialogOpts = {
            backdropFade: true,
            dialogFade: true
        };

        $scope.openArchiveDialog = function (cert) {
            $scope.certToArchive = cert;
            $scope.archiveOpen = true;
            $scope.dialogfocus = true;
        }

        $scope.closeArchiveDialog = function (confirm) {
            if (confirm) {
                $scope.archiveSelected($scope.certToArchive);
            }
            $scope.archiveOpen = false;
            $scope.dialogfocus = false;
        }

        //FK dialog
        var fromConsentPage =  $cookies['RedirectFromConsent'];
        $scope.fkMessageOpen = fromConsentPage && ($rootScope.MI_CONFIG.LOGIN_METHOD === "FK");
        $scope.closeFKDialog = function (backtoFK) {
            if (backtoFK) {
                $window.location.href = "/web/logga-ut";
            }
            //no matter outcome of dialog, it should be a one time popup..
            //remove flag indicating forwarded from consent page..
            delete $cookies['RedirectFromConsent']
            //...and close dialog
            $scope.fkMessageOpen = false;
        }
        
        
        // fetch list of certs initially
        listCertService.getCertificates(function (list) {
            $scope.doneLoading = true;
            if (list != null) {
                $scope.certificates = list;
            } else {
                // show error view
                $location.path("/fel/couldnotloadcertlist");
            }

        });

        // Set focus on new page so screen readers can announce it
        $scope.pagefocus = true;
    } ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', '$location', '$log', 'listCertService', function ListCertCtrl($scope, $location, $log, listCertService) {
    $scope.certificates = [];
    $scope.doneLoading = false;

    // Restore dialog
    $scope.restoreOpen = false;
    $scope.dialogfocus = false;
    $scope.certToRestore = {};
    $scope.dialogOpts = {
        backdropFade: true,
        dialogFade: true
    };

    $scope.openRestoreDialog = function (cert) {
        $scope.certToRestore = cert;
        $scope.restoreOpen = true;
        $scope.dialogfocus = true;
    }

    $scope.closeRestoreDialog = function (confirm) {
        if (confirm) {
            $scope.restoreCert($scope.certToRestore)
        }
        $scope.restoreOpen = false;
        $scope.dialogfocus = false;
    }

    $scope.restoreCert = function (certId) {
        $log.debug("Restore requested for cert:" + certId);
        for (var i = 0; i < $scope.certificates.length; i++) {
            if ($scope.certificates[i].id == certId) {

                listCertService.restoreCertificate($scope.certificates[i], function (fromServer, oldItem) {
                    $log.debug("(restore) statusUpdate callback:" + fromServer);
                    if (fromServer != null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                    } else {
                        // show error view
                        $location.path("/fel/couldnotrestorecert");
                    }

                });

            }

        }
    }

    listCertService.getCertificates(function (list) {
        // filtering of deleted certs is done in view template
        $scope.certificates = list;
        $scope.doneLoading = true;
    });

    $scope.pagefocus = true;
} ]);

// Consent Controller
listCertApp.controller('AboutCtrl', [ '$scope', '$location', '$filter', '$log', 'consentService', 'messageService', '$window',
    function ConsentCtrl($scope, $location, $filter, $log, consentService, messageService, $window) {

        // Hold left side navigation state
        $scope.visibility = {
            "omminaintyg": true,
            "samtycke": false,
            "juridik": false
        }

        // Hold focus state for sub pages
        $scope.subpagefocus = {
            "omminaintyg": false,
            "samtycke": false,
            "juridik": false
        }

        $scope.dialogfocus = false;

        $scope.navigateTo = function (section) {
            angular.forEach($scope.visibility, function (value, key) {
                $scope.visibility[key] = (key == section) ? true : false;
                $scope.subpagefocus[key] = (key == section) ? true : false;
            });
        }

        $scope.getMessage = function (key) {
            return messageService.getProperty(key);
        }

        $scope.opts = {
            backdropFade: true,
            dialogFade: true
        };

        $scope.openConfirmDialog = function () {
            $scope.shouldBeOpen = true;
            $scope.dialogfocus = true;
        };

        $scope.closeConfirmDialog = function (confirm) {
            $scope.shouldBeOpen = false;
            $log.debug("closeConfirmDialog " + confirm);
            if (confirm) {
                $log.debug("revoking consent..");
                revokeConsent();
            }
        };

        function revokeConsent() {
            consentService.revokeConsent(function (data) {
                $log.debug("revokeConsent callback:" + data);
                if (data != null && data.result) {
                    $window.location.href = "/web/start";
                } else {
                    $location.path("/fel/couldnotrevokeconsent");
                }

            });
        }
        ;

        $scope.pagefocus = true;
    } ]);

listCertApp.controller('HelpCtrl', [ '$scope', '$location', 'listCertService', function ListCertCtrl($scope, $location, listCertService) {
    $scope.pagefocus = true;
} ]);
