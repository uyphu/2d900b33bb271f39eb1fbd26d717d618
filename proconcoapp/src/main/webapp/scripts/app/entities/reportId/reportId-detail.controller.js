'use strict';

angular.module('proconcoappApp')
    .controller('ReportIdDetailController', function ($scope, $stateParams, ReportId, User, Report) {
        $scope.reportId = {};
        $scope.load = function (id) {
            ReportId.get({id: id}, function(result) {
              $scope.reportId = result;
            });
        };
        $scope.load($stateParams.id);
    });
