'use strict';

angular.module('proconcoappApp')
    .controller('GrouppDetailController', function ($scope, $stateParams, Groupp) {
        $scope.groupp = {};
        $scope.load = function (id) {
            Groupp.get({id: id}, function(result) {
              $scope.groupp = result;
            });
        };
        $scope.load($stateParams.id);
    });
