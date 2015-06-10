'use strict';

angular.module('proconcoappApp')
    .controller('PlanningWeekIdDetailController', function ($scope, $stateParams, PlanningWeekId, User, PlanningWeek) {
        $scope.planningWeekId = {};
        $scope.load = function (id) {
            PlanningWeekId.get({id: id}, function(result) {
              $scope.planningWeekId = result;
            });
        };
        $scope.load($stateParams.id);
    });
