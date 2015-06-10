'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reportId', {
                parent: 'entity',
                url: '/reportId',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.reportId.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reportId/reportIds.html',
                        controller: 'ReportIdController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reportId');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reportIdDetail', {
                parent: 'entity',
                url: '/reportId/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.reportId.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reportId/reportId-detail.html',
                        controller: 'ReportIdDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reportId');
                        return $translate.refresh();
                    }]
                }
            });
    });
