var router = angular.module('router', ['ui.router', 'ngResource', 'ngCookies', 'ngRoute']);

router.config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/welcome');
    
    $stateProvider
        
    	.state('welcome', {
    		url: '/welcome',
    		controller: 'welcomeController',
    		templateUrl: 'views/welcome.html'
    	})
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