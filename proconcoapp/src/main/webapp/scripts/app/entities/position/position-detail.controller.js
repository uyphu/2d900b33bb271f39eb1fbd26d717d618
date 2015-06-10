'use strict';

angular.module('proconcoappApp')
    .controller('PositionDetailController', function ($scope, $stateParams, Position) {
        $scope.position = {};
        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
              $scope.position = result;
            });
        };
        $scope.load($stateParams.id);
    });
