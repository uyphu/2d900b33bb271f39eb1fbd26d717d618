'use strict';

angular.module('proconcoappApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
