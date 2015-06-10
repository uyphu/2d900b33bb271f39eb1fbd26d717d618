'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('position', {
                parent: 'entity',
                url: '/position',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.position.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/position/positions.html',
                        controller: 'PositionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('position');
                        return $translate.refresh();
                    }]
                }
            })
            .state('positionDetail', {
                parent: 'entity',
                url: '/position/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.position.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/position/position-detail.html',
                        controller: 'PositionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('position');
                        return $translate.refresh();
                    }]
                }
            });
    });
