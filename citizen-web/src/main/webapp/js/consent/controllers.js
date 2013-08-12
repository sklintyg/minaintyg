'use strict';

// Consent Controller
consentApp.controller('ConsentCtrl', [ '$scope', '$location', '$filter', 'consentService', '$window','$cookies', function ConsentCtrl($scope, $location, $filter, consentService, $window, $cookies) {

    $scope.giveConsent = function() {
        consentService.giveConsent(function(data) {
            // If consent saved OK, proceed to main app and set cookie to indicate that we'e just 
            // given consent, rather than normal login flow
            if (data !=null && data.result) {
                $cookies['RedirectFromConsent'] = "1";
                $window.location.href = "/web/start";
            } else {
                $location.path("/fel/couldnotgiveconsent");
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
