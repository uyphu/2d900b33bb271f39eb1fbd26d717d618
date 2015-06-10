'use strict';

angular.module('proconcoappApp')
    .controller('ReportDetailController', function ($scope, $stateParams, Report, ReportId) {
        $scope.report = {};
        $scope.load = function (id) {
            Report.get({id: id}, function(result) {
              $scope.report = result;
            });
        };
        $scope.load($stateParams.id);
    });
