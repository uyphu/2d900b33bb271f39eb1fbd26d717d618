'use strict';

angular.module('proconcoappApp')
    .controller('AuthorController', function ($scope, Author, Book, AuthorSearch, ParseLinks) {
        $scope.authors = [];
        $scope.books = Book.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Author.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.authors.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.authors = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Author.get({id: id}, function(result) {
                $scope.author = result;
                $('#saveAuthorModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.author.id != null) {
                Author.update($scope.author,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Author.save($scope.author,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Author.get({id: id}, function(result) {
                $scope.author = result;
                $('#deleteAuthorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Author.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteAuthorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AuthorSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.authors = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveAuthorModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.author = {name: null, birthDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
