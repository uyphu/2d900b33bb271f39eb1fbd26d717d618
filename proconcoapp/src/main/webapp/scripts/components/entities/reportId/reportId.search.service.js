'use strict';

angular.module('proconcoappApp')
    .factory('ReportIdSearch', function ($resource) {
        return $resource('api/_search/reportIds/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
