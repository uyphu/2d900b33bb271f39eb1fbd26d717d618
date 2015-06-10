'use strict';

angular.module('proconcoappApp')
    .controller('PlanningWeekController', function ($scope, PlanningWeek, PlanningWeekId, PlanningWeekSearch, ParseLinks) {
        $scope.planningWeeks = [];
        $scope.planningweekids = PlanningWeekId.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            PlanningWeek.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.planningWeeks.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.planningWeeks = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            PlanningWeek.get({id: id}, function(result) {
                $scope.planningWeek = result;
                $('#savePlanningWeekModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.planningWeek.id != null) {
                PlanningWeek.update($scope.planningWeek,
                    function () {
                        $scope.refresh();
                    });
            } else {
                PlanningWeek.save($scope.planningWeek,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            PlanningWeek.get({id: id}, function(result) {
                $scope.planningWeek = result;
                $('#deletePlanningWeekConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PlanningWeek.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePlanningWeekConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PlanningWeekSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.planningWeeks = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#savePlanningWeekModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.planningWeek = {monday: null, tuesday: null, wednesday: null, thursday: null, friday: null, saturday: null, sunday: null, delFlag: null, crtUid: null, crtTms: null, updUid: null, updTms: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
