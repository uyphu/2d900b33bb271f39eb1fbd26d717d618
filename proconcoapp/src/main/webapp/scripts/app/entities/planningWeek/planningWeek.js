'use strict';

angular.module('proconcoappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('planningWeek', {
                parent: 'entity',
                url: '/planningWeek',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.planningWeek.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planningWeek/planningWeeks.html',
                        controller: 'PlanningWeekController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planningWeek');
                        return $translate.refresh();
                    }]
                }
            })
            .state('planningWeekDetail', {
                parent: 'entity',
                url: '/planningWeek/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'proconcoappApp.planningWeek.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planningWeek/planningWeek-detail.html',
                        controller: 'PlanningWeekDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planningWeek');
                        return $translate.refresh();
                    }]
                }
            });
    });
