'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('report', {
                parent: 'entity',
                url: '/report',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.report.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/report/reports.html',
                        controller: 'ReportController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('report');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reportDetail', {
                parent: 'entity',
                url: '/report/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.report.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/report/report-detail.html',
                        controller: 'ReportDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('report');
                        return $translate.refresh();
                    }]
                }
            });
    });
