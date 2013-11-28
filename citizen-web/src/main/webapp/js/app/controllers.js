'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter', '$location', '$window', '$log', 'listCertService', 'messageService', '$cookies', '$rootScope', '$timeout', 
    function ListCertCtrl($scope, $filter, $location, $window, $log, listCertService, messageService, $cookies, $rootScope, $timeout) {
        $scope.certificates = [];
        $scope.doneLoading = false;
        $scope.dialog = {
        		acceptprogressdone: true, 
        		focus: false
        }
        $scope.showCookieInfo = false;
        $scope.toggleCookieInfo = function() {
        	$scope.showCookieInfo = !$scope.showCookieInfo;
        }
        
        var archiveDialog = {};
        
        $scope.messageService = messageService;
        $scope.pageTitle = "Inkorgen";
        $scope.isCollapsed = true;
  
        $scope.sendSelected = function (item) {
            $log.debug("send " + item.id);
            listCertService.selectedCertificate = item;
            var path = "/m/" + item.type.toLowerCase() + "/intyg/" + item.id
            $window.location.href = path;
        }

        $scope.archiveSelected = function () {
        	var item = $scope.certToArchive;
            $log.debug("archive " + item.id);
            $scope.dialog.acceptprogressdone = false;
            listCertService.archiveCertificate(item, function (fromServer, oldItem) {
                $log.debug("statusUpdate callback:" + fromServer);
                if (fromServer != null) {
                    // Better way to update the object?
                    oldItem.archived = fromServer.archived;
                    oldItem.status = fromServer.status;
                    oldItem.selected = false;
                   	archiveDialog.close();
                    $scope.dialog.acceptprogressdone = true;
                } else {
                    // show error view
                    $location.path("/fel/couldnotarchivecert");
                }
            });
        }

        // Archive dialog
        $scope.certToArchive = {};

        $scope.openArchiveDialog = function (cert) {
            $scope.certToArchive = cert;
            $scope.dialog.focus = true;
            archiveDialog = listCertService.showDialog(
            		$scope, 
            		{
            			dialogId: "archive-confirmation-dialog",
            			titleId: "inbox.archivemodal.header", 
            			bodyTextId: "inbox.archivemodal.text",
            			button1click: function() {
            				$log.debug("archive");
            				$scope.archiveSelected();
            			},
            			button1id:"archive-button",
            			button1text: "button.archive",
            			autoClose: false
            		}
          	);
        }

        //FK dialog
        var fromConsentPage =  $cookies['RedirectFromConsent'];
        if(fromConsentPage && ($rootScope.MI_CONFIG.LOGIN_METHOD === "FK")){
            var fkDialog = listCertService.showDialog(
            		$scope, 
            		{
            			dialogId: "fk-login-consent-dialog",
            			titleId: "fkdialog.head", 
            			bodyTextId: "fkdialog.text",
            			button1click: function() {
            				$scope.closeFKDialog(true);
            			},
            			button2click: function() {
            				$scope.closeFKDialog(false);
            			},
            			button1id:"close-fkdialog-logout-button",
            			button2id:"close-fkdialog-continue-button",
            			button1text: "fkdialog.button.returntofk",
            			button2text: "fkdialog.button.continueuse",
            			autoClose: false
            		}
          	);
        }
        
        $scope.closeFKDialog = function (backtoFK) {
            if (backtoFK) {
                $window.location.href = "/web/logga-ut-fk";
            }
            //no matter outcome of dialog, it should be a one time popup..
            //remove flag indicating forwarded from consent page..
            delete $cookies['RedirectFromConsent']
            //...and close dialog
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
    
    $scope.dialog = {
    		acceptprogressdone: true,
    		focus: false
    }
    
    // Restore dialog
    var restoreDialog = {};
    $scope.certToRestore = {};
    
    $scope.openRestoreDialog = function (cert) {
      $scope.certToRestore = cert;
      $scope.dialog.focus = true;
      restoreDialog = listCertService.showDialog(
      		$scope, 
      		{
      			dialogId: "restore-confirmation-dialog",
      			titleId: "archived.restoremodal.header",
      			bodyTextId: "archived.restoremodal.text",
      			button1click: function() {
      				$scope.restoreCert();
      			},
      			button1id:"restore-button",
      			button1text: "label.restore",
      			autoClose: false
      		}
    	);
    }
    
    $scope.restoreCert = function () {
    	var certId = $scope.certToRestore;
        $log.debug("Restore requested for cert:" + certId);
        $scope.dialog.acceptprogressdone = false;
        for (var i = 0; i < $scope.certificates.length; i++) {
            if ($scope.certificates[i].id == certId) {

                listCertService.restoreCertificate($scope.certificates[i], function (fromServer, oldItem) {
                    $log.debug("(restore) statusUpdate callback:" + fromServer);
                    if (fromServer != null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                        restoreDialog.close();
                        $scope.dialog.acceptprogressdone = true;
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
listCertApp.controller('AboutCtrl', [ '$scope', '$location', '$filter', '$log', 'consentService', 'messageService', '$window', 'listCertService',
    function ConsentCtrl($scope, $location, $filter, $log, consentService, messageService, $window, listCertService) {

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

        $scope.dialog = {
        		acceptprogressdone: true,
        		focus: false
        }
        
        $scope.navigateTo = function (section) {
            angular.forEach($scope.visibility, function (value, key) {
                $scope.visibility[key] = (key == section) ? true : false;
                $scope.subpagefocus[key] = (key == section) ? true : false;
            });
        }

        $scope.getMessage = function (key) {
            return messageService.getProperty(key);
        }

        // Revoke dialog
        
        var revokeDialog = {};
        
        $scope.openRevokeDialog = function (cert) {
          $scope.dialog.focus = true;
          revokeDialog = listCertService.showDialog(
          		$scope, 
          		{
          			dialogId: "revoke-consent-confirmation-dialog",
          			titleId: "about.revokemodal.header",
          			bodyTextId: "about.revokemodal.text",
          			button1click: function() {
                  $log.debug("revoking consent..");
                  $scope.dialog.acceptprogressdone = true;
                  revokeConsent();
          			},
          			button1id:"revoke-consent-button",
          			autoClose: false
          		}
        	);
        }        
        
        function revokeConsent() {
            consentService.revokeConsent(function (data) {
                $log.debug("revokeConsent callback:" + data);
                $scope.dialog.acceptprogressdone = false;
                if (data != null && data.result) {
                	$window.location.href = "/web/start";
                } else {
                    $location.path("/fel/couldnotrevokeconsent");
                }
            });
        };

        $scope.pagefocus = true;
    } ]);

listCertApp.controller('HelpCtrl', [ '$scope', '$location', 'listCertService', function ListCertCtrl($scope, $location, listCertService) {
    $scope.pagefocus = true;
} ]);
