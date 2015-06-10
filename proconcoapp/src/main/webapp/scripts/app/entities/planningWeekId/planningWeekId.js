'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('planningWeekId', {
                parent: 'entity',
                url: '/planningWeekId',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.planningWeekId.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planningWeekId/planningWeekIds.html',
                        controller: 'PlanningWeekIdController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planningWeekId');
                        return $translate.refresh();
                    }]
                }
            })
            .state('planningWeekIdDetail', {
                parent: 'entity',
                url: '/planningWeekId/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.planningWeekId.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planningWeekId/planningWeekId-detail.html',
                        controller: 'PlanningWeekIdDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planningWeekId');
                        return $translate.refresh();
                    }]
                }
            });
    });
