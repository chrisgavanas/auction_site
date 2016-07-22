var router = angular.module('router', ['ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'checklist-model']);


router.config(function($stateProvider, $urlRouterProvider) {
    
    //$urlRouterProvider.otherwise('/welcome');
    
    $stateProvider
        
    .state('main', {
    	abstract: true,
    	views: { 'navBar': {
    							templateUrl: './views/navBar.html',
    							controller: 'navBarController'
    			},
    			'': {
    				templateUrl: './views/main.html'
    			}
    						
    	}
    })
    	.state('main.welcome', {
    		url: '/welcome',
    		controller: 'welcomeController',
			templateUrl: 'views/welcome.html',
    		
    		
    	})
    	
        .state('main.register', {
        	url: '/register',
        	controller: 'registerController',
        	templateUrl: 'views/register.html'
        })
        
        .state('main.verification',{
        	url: '/pendingMessage',
        	templateUrl: 'views/verificationPending.html'
        })
        
        .state('main.profile', {
        	url: '/profile',
        	controller: 'profileController',
        	templateUrl: 'views/profile2.html'

        })
        .state('main.profile.userInfo',{
        	url: '/userInfo',
        	views: { 'menuview': {
        		controller: 'userInfoController',
        	   templateUrl: '/views/userInfo.html'
        		}
        	}
        })
       
        .state('main.profile.userAuctions',{
        	url: '/userAuctions',
        	views: { 'menuview': {
        		controller: 'userAuctionsController',
        	   templateUrl: '/views/userAuctions.html'
        		}
        	}
        })
        
        .state('main.profile.userBids',{
        	url: '/userBids',
        	views: { 'menuview': {
        		controller: 'userBidsController',
        	   templateUrl: '/views/userBids.html'
        		}
        	}
        })
        
        .state('main.admin', {
    			url: '/adminOptions',
    			controller: 'adminOptionsController',
    			templateUrl: 'views/adminOptions.html'
    	})
    
    	.state('main.admin.viewUser', {
    			url: '/userview',
    			views:{ 'userview':{
    				templateUrl: 'views/userPreview.html'
    			}
    			}
    	})
        .state('main.newAuction', {
        	url: '/newAuction',
        	controller: 'newAuctionController',
        	templateUrl: 'views/newAuction.html'
        })
        .state('main.itemPreview',{
        	url: '/newAuction/itemPreview',
        	controller: 'itemPreviewController',
        	templateUrl: 'views/itemPreview.html'
        })
       
        
});