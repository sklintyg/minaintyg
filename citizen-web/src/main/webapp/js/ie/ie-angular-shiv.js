/* global window, document */
// Modified version of ie-angular-shiv from angular-ui-utils project, that also supports directives without camelCase
// names. e.g our "message" directive
(function(window, document) {
    'use strict';

    var tags = [ 'ngInclude', 'ngPluralize', 'ngView', 'ngSwitch', 'uiCurrency', 'uiCodemirror', 'uiDate', 'uiEvent',
        'uiKeypress', 'uiKeyup', 'uiKeydown', 'uiMask', 'uiMapInfoWindow', 'uiMapMarker', 'uiMapPolyline',
        'uiMapPolygon', 'uiMapRectangle', 'uiMapCircle', 'uiMapGroundOverlay', 'uiModal', 'uiReset',
        'uiScrollfix', 'uiSelect2', 'uiShow', 'uiHide', 'uiToggle', 'uiSortable', 'uiTinymce'
    ];

    window.myCustomTags = window.myCustomTags || []; // externally defined by developer using angular-ui directives
    tags.push.apply(tags, window.myCustomTags);

    var toCustomElements = function(str) {
        var result = [];
        var dashed = str.replace(/([A-Z])/g, function($1) {
            return ' ' + $1.toLowerCase();
        });
        var tokens = dashed.split(' ');
        var ns = tokens[0];
        var dirname = tokens.slice(1).join('-');

        // handle directives without ns pattern e.g 'message'
        if (dirname === '') {
            result.push(ns);
            return result;
        } else {
            result.push(ns + ':' + dirname);
            result.push(ns + '-' + dirname);
            result.push('x-' + ns + '-' + dirname);
            result.push('data-' + ns + '-' + dirname);
            return result;

        }
    };

    for (var i = 0, tlen = tags.length; i < tlen; i++) {
        var customElements = toCustomElements(tags[i]);
        for (var j = 0, clen = customElements.length; j < clen; j++) {
            var customElement = customElements[j];
            document.createElement(customElement);
        }
    }

})(window, document);
