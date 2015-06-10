'use strict';

angular.module('proconcoappApp')
    .factory('PlanningWeek', function ($resource, DateUtils) {
        return $resource('api/planningWeeks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.crtTms = DateUtils.convertLocaleDateFromServer(data.crtTms);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.crtTms = DateUtils.convertLocaleDateToServer(data.crtTms);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.crtTms = DateUtils.convertLocaleDateToServer(data.crtTms);
                    return angular.toJson(data);
                }
            }
        });
    });
