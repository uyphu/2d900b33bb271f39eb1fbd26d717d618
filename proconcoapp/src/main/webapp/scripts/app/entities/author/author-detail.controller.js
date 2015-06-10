'use strict';

angular.module('proconcoappApp')
    .controller('AuthorDetailController', function ($scope, $stateParams, Author, Book) {
        $scope.author = {};
        $scope.load = function (id) {
            Author.get({id: id}, function(result) {
              $scope.author = result;
            });
        };
        $scope.load($stateParams.id);
    });
