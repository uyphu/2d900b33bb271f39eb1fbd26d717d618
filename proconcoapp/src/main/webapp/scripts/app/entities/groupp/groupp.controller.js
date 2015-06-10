'use strict';

angular.module('proconcoappApp')
    .controller('GrouppController', function ($scope, Groupp, GrouppSearch, ParseLinks) {
        $scope.groupps = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Groupp.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.groupps.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.groupps = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Groupp.get({id: id}, function(result) {
                $scope.groupp = result;
                $('#saveGrouppModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.groupp.id != null) {
                Groupp.update($scope.groupp,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Groupp.save($scope.groupp,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Groupp.get({id: id}, function(result) {
                $scope.groupp = result;
                $('#deleteGrouppConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Groupp.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteGrouppConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            GrouppSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.groupps = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveGrouppModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.groupp = {grpName: null, delFlag: null, crtUid: null, crtTms: null, updUid: null, updTms: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
