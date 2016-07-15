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
        .state('profile.userInfo',{
        	url: '/userInfo',
        	views: { 'menuview': {
        		controller: 'userInfoController',
        	   templateUrl: '/views/userInfo.html'
        		}
        	}
        })
        .state('profile.userAuctions',{
        	url: '/userAuctions',
        	views: { 'menuview': {
        		controller: 'userAuctionsController',
        	   templateUrl: '/views/userAuctions.html'
        		}
        	}
        })
        
        .state('profile.userBids',{
        	url: '/userBids',
        	views: { 'menuview': {
        		controller: 'userBidsController',
        	   templateUrl: '/views/userBids.html'
        		}
        	}
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