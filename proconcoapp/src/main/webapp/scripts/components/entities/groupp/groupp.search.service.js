'use strict';

angular.module('proconcoappApp')
    .factory('GrouppSearch', function ($resource) {
        return $resource('api/_search/groupps/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
