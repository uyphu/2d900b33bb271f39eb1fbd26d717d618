'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupp', {
                parent: 'entity',
                url: '/groupp',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.groupp.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupp/groupps.html',
                        controller: 'GrouppController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('groupp');
                        return $translate.refresh();
                    }]
                }
            })
            .state('grouppDetail', {
                parent: 'entity',
                url: '/groupp/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.groupp.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/groupp/groupp-detail.html',
                        controller: 'GrouppDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('groupp');
                        return $translate.refresh();
                    }]
                }
            });
    });
