'use strict';

/* Directives */
listCertApp.directive("message", function () {
    return {
        restrict: "E",
        scope: {},
        replace: true,
        template: "<span>{{resultValue}}</span>",
        compile:function (element, attr, transclusionFunc) {
            return function ($scope, element, attr) {
                var lang = "sv";
                var value = messages[lang][attr.key];
                if(typeof value === "undefined") {
                    value = "Saknat värde '" + attr.key + "' i ordlistan '" + lang +"'.";
                }
                $scope.resultValue = value;
                element.addClass("text-error");
            };
        }
    }
});