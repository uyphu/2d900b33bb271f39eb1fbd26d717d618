'use strict';

angular.module('proconcoappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


