'use strict';

angular.module('proconcoappApp')
    .controller('ReportController', function ($scope, Report, ReportId, ReportSearch, ParseLinks) {
        $scope.reports = [];
        $scope.reportids = ReportId.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Report.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.reports.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.reports = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Report.get({id: id}, function(result) {
                $scope.report = result;
                $('#saveReportModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.report.id != null) {
                Report.update($scope.report,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Report.save($scope.report,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Report.get({id: id}, function(result) {
                $scope.report = result;
                $('#deleteReportConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Report.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteReportConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ReportSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.reports = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveReportModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.report = {amt: null, marketRpt: null, monday: null, tuesday: null, wednesday: null, thursday: null, friday: null, saturday: null, sunday: null, delFlag: null, crtUid: null, crtTms: null, updUid: null, updTms: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
