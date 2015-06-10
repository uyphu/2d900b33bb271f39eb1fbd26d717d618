'use strict';

angular.module('proconcoappApp')
    .controller('PlanningWeekIdController', function ($scope, PlanningWeekId, User, PlanningWeek, PlanningWeekIdSearch, ParseLinks) {
        $scope.planningWeekIds = [];
        $scope.users = User.query();
        $scope.planningweeks = PlanningWeek.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            PlanningWeekId.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.planningWeekIds.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.planningWeekIds = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            PlanningWeekId.get({id: id}, function(result) {
                $scope.planningWeekId = result;
                $('#savePlanningWeekIdModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.planningWeekId.id != null) {
                PlanningWeekId.update($scope.planningWeekId,
                    function () {
                        $scope.refresh();
                    });
            } else {
                PlanningWeekId.save($scope.planningWeekId,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            PlanningWeekId.get({id: id}, function(result) {
                $scope.planningWeekId = result;
                $('#deletePlanningWeekIdConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PlanningWeekId.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePlanningWeekIdConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PlanningWeekIdSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.planningWeekIds = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#savePlanningWeekIdModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.planningWeekId = {week: null, year: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
