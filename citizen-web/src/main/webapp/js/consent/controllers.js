'use strict';

// Consent Controller
consentApp.controller('ConsentCtrl', [ '$scope', '$location', '$filter', 'consentService', '$window', function ConsentCtrl($scope, $location, $filter, consentService, $window) {

    $scope.giveConsent = function() {
        consentService.giveConsent(function(data) {
            console.log("giveConsent callback:" + data);
            // If ok, go to consent-given
            if (data.result) {
                $location.path("/samtycke-givet");
            } else {
                $location.path("/fel");
            }
        });
    };

} ]);

// Consent Controller
consentApp.controller('ConsentGivenCtrl', [ '$scope', '$location', '$filter', 'consentService', '$window', function ConsentCtrl($scope, $location, $filter, consentService, $window) {

    $scope.continueToMI = function() {
        $window.location.href = "/web/start";
    }

	$scope.pagefocus = true;
} ]);
