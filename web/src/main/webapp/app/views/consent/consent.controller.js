angular.module('minaintyg').controller('minaintyg.ConsentCtrl',
    [ '$cookies', '$filter', '$location', '$scope', '$window', 'minaintyg.consentService',
        function($cookies, $filter, $location, $scope, $window, consentService) {
            'use strict';

            $scope.giveConsent = function() {
                consentService.giveConsent(function(data) {
                    // If consent saved OK, proceed to main app and set cookie to indicate that we'e just
                    // given consent, rather than normal login flow
                    if (data !== null && data.result) {
                        $cookies.RedirectFromConsent = '1';
                        $window.location.href = '/web/start';
                    } else {
                        $location.path('/fel/couldnotgiveconsent');
                    }
                });
            };
        }]);
