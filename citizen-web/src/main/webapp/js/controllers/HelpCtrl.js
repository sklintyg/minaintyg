angular.module('minaintyg').controller('minaintyg.HelpCtrl',['$scope','common.messageService',
    function($scope, messageService) {
        'use strict';

        $scope.pagefocus = true;

        // Hold left side navigation state
        $scope.visibility = {
            helpdescription: true,
            helpfaq: false
        };

        // Hold focus state for sub pages
        $scope.subpagefocus = {
            helpdescription: false,
            helpfaq: false
        };

        $scope.navigateTo = function(section) {
            angular.forEach($scope.visibility, function(value, key) {
                $scope.visibility[key] = (key === section);
                $scope.subpagefocus[key] = (key === section);
            });
        };

        $scope.faqs = messageService.getProperty('help.faq');
    }]);
