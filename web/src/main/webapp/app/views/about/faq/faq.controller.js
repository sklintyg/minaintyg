/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('minaintyg').controller('AboutFaqPageCtrl', [ '$scope', 'common.messageService', '$window', 'smoothScroll',
    function($scope, messageService, $window, smoothScroll) {
    'use strict';

    function getQuestions(prefix, idPrefix) {
        var questions = [];
        var numberOfQuestions = 1;

        while (hasQuestion(prefix, numberOfQuestions)) {
            questions.push({
                id: 'faq-' + idPrefix + '-' + numberOfQuestions,
                title: prefix + numberOfQuestions + '.title',
                closed: true,
                body: prefix + numberOfQuestions + '.body'
            });

            numberOfQuestions++;
        }

        return questions;
    }

    function hasQuestion(prefix, index) {
        var key = prefix + index + '.title';

        return messageService.propertyExists(key);
    }

    var faq = [];

    faq.push({
        title: 'Intyg',
        icon: 'icon-doc-text',
        questions: getQuestions('faq.intyg.', 'intyg')
    });

    faq.push({
        title: 'Säkerhet',
        icon: 'icon-shield',
        questions: getQuestions('faq.sakerhet.', 'sakerhet')
    });

    $scope.faq = faq;

    $scope.openAll = function() {
        toggleQuestions(false);
    };

    $scope.closeAll = function() {
        toggleQuestions(true);
    };

    $scope.toggleQuestion = function(question) {
        question.closed = !question.closed;

        if (!question.closed) {
            var elementToScrollTo = $('#' + question.id);

            var windowElement = $($window);
            var windowHeight = windowElement.height() / 2;
            var scrollTop = windowElement.scrollTop();
            var elementPosition = elementToScrollTo.offset().top;

            if (elementPosition - scrollTop > windowHeight) {
                var offset = 100;
                var options = {
                    duration: 500,
                    easing: 'easeInOutQuart',
                    offset: offset
                };

                //scroll to this questions panel heading, centered vertically
                smoothScroll(elementToScrollTo[0], options);
            }
        }
    };

    function toggleQuestions(closed) {
        angular.forEach(faq, function(category) {
            angular.forEach(category.questions, function(question) {
                question.closed = closed;
            });
        });
    }

} ]);
