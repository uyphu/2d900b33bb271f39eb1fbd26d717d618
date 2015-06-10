'use strict';

angular.module('proconcoappApp')
    .factory('Position', function ($resource, DateUtils) {
        return $resource('api/positions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.crtTms = DateUtils.convertLocaleDateFromServer(data.crtTms);
                    data.updTms = DateUtils.convertLocaleDateFromServer(data.updTms);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.crtTms = DateUtils.convertLocaleDateToServer(data.crtTms);
                    data.updTms = DateUtils.convertLocaleDateToServer(data.updTms);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.crtTms = DateUtils.convertLocaleDateToServer(data.crtTms);
                    data.updTms = DateUtils.convertLocaleDateToServer(data.updTms);
                    return angular.toJson(data);
                }
            }
        });
    });
