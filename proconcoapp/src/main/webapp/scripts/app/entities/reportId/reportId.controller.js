'use strict';

angular.module('proconcoappApp')
    .controller('ReportIdController', function ($scope, ReportId, User, Report, ReportIdSearch, ParseLinks) {
        $scope.reportIds = [];
        $scope.users = User.query();
        $scope.reports = Report.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ReportId.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.reportIds.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.reportIds = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ReportId.get({id: id}, function(result) {
                $scope.reportId = result;
                $('#saveReportIdModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.reportId.id != null) {
                ReportId.update($scope.reportId,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ReportId.save($scope.reportId,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ReportId.get({id: id}, function(result) {
                $scope.reportId = result;
                $('#deleteReportIdConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ReportId.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteReportIdConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ReportIdSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.reportIds = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveReportIdModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.reportId = {week: null, year: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
