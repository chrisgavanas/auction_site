var router = angular.module('router', ['ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'checklist-model']);


router.config(function($stateProvider, $urlRouterProvider) {
    
    //$urlRouterProvider.otherwise('/welcome');
    
    $stateProvider
        
    	.state('welcome', {
    		url: '/welcome',
    		controller: 'welcomeController',
    		templateUrl: 'views/welcome.html'
    	})
    	
        .state('register', {
        	url: '/register',
        	controller: 'registerController',
        	templateUrl: 'views/register.html'
        })
        .state('profile', {
        	url: '/profile',
        	controller: 'profileController',
        	templateUrl: 'views/profile2.html'

        })
        .state('newAuction', {
        	url: '/newAuction',
        	controller: 'newAuctionController',
        	templateUrl: 'views/newAuction.html'
        })
        .state('itemPreview',{
        	url: '/newAuction/itemPreview',
        	controller: 'itemPreviewController',
        	templateUrl: 'views/itemPreview.html'
        })
       
        
});