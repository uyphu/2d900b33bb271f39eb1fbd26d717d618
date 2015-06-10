'use strict';

angular.module('proconcoappApp')
    .factory('ReportId', function ($resource, DateUtils) {
        return $resource('api/reportIds/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
