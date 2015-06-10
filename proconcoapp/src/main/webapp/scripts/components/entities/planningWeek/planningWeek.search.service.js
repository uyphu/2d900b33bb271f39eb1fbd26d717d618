'use strict';

angular.module('proconcoappApp')
    .factory('PlanningWeekSearch', function ($resource) {
        return $resource('api/_search/planningWeeks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
