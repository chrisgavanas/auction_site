var router = angular.module('router', ['ui.router', 'ngResource']);

router.config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/home');
    
    $stateProvider
        
        .state('login', {
            url: '/login',
            controller: 'loginController',
            templateUrl: 'views/login.html'
        })
        .state('register', {
        	url: '/register',
        	controller: 'registerController',
        	templateUrl: 'views/register.html'
        })
});