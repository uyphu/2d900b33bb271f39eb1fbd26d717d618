'use strict';

angular.module('proconcoappApp')
    .factory('PositionSearch', function ($resource) {
        return $resource('api/_search/positions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
