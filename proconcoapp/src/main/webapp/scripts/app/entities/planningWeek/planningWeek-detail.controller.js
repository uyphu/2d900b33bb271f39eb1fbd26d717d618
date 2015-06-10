'use strict';

angular.module('proconcoappApp')
    .controller('PlanningWeekDetailController', function ($scope, $stateParams, PlanningWeek, PlanningWeekId) {
        $scope.planningWeek = {};
        $scope.load = function (id) {
            PlanningWeek.get({id: id}, function(result) {
              $scope.planningWeek = result;
            });
        };
        $scope.load($stateParams.id);
    });
