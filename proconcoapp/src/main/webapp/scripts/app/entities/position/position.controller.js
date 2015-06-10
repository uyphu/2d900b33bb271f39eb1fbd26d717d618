'use strict';

angular.module('proconcoappApp')
    .controller('PositionController', function ($scope, Position, PositionSearch, ParseLinks) {
        $scope.positions = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Position.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.positions.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.positions = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
                $('#savePositionModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.position.id != null) {
                Position.update($scope.position,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Position.save($scope.position,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
                $('#deletePositionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Position.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePositionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PositionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.positions = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#savePositionModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.position = {postName: null, delFlag: null, crtUid: null, crtTms: null, updUid: null, updTms: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
