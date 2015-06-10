'use strict';

angular.module('proconcoappApp')
    .factory('PlanningWeekIdSearch', function ($resource) {
        return $resource('api/_search/planningWeekIds/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
