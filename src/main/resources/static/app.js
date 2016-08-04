var router = angular.module('router', ['ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'checklist-model']);


router.config(function($stateProvider, $urlRouterProvider) {
    
    //$urlRouterProvider.otherwise('/welcome');
    
    $stateProvider
        
    .state('main', {
    	abstract: true,
    	views: { 'navBar': {
    							templateUrl: './components/nav-bar.html',
    							controller: 'navBarController'
    			},
    			'': {
    				templateUrl: './main/main.html'
    			}
    						
    	}
    })
    	.state('main.welcome', {
    		url: '/welcome',
    		controller: 'welcomeController',
			templateUrl: 'main/welcome.html',
    		
    		
    	})
    	
        .state('main.register', {
        	url: '/register',
        	controller: 'registerController',
        	templateUrl: 'register/register.html'
        })
        
        .state('main.verification',{
        	url: '/pendingMessage',
        	templateUrl: 'messeges/verification-pending.html'
        })
        
        .state('main.profile', {
        	url: '/profile',
        	controller: 'profileController',
        	templateUrl: 'user/profile.html'

        })
        .state('main.profile.userInfo',{
        	url: '/userInfo',
        	views: { 'menuview': {
        		controller: 'userInfoController',
        	    templateUrl: '/user/user-info.html'
        		}
        	}
        })
       
        .state('main.item', {
    		url: '/item?id',
    		controller: 'itemController',
    		templateUrl: 'auctions/auction-item.html'
    	})
    	
        .state('main.profile.userAuctions',{
        	url: '/userAuctions',
        	views: { 'menuview': {
        		controller: 'userAuctionsController',
        	    templateUrl: '/user/user-auctions.html'
        		}
        	}
        })
        
        .state('main.profile.userBids',{
        	url: '/userBids',
        	views: { 'menuview': {
        		controller: 'userBidsController',
        	   templateUrl: '/user/user-bids.html'
        		}
        	}
        })
        
    
    	
        .state('main.admin', {
    			url: '/adminOptions',
    			controller: 'adminOptionsController',
    			templateUrl: 'admin/admin-options.html'
    	})
    
    
        .state('main.newAuction', {
        	url: '/newAuction',
        	controller: 'newAuctionController',
        	templateUrl: 'auctions/new-auction.html'
        })
        
        .state('main.verificationPreview', {
        	url: '/verify?id',
        	controller: 'verificationPreviewController',
        	templateUrl: 'admin/verification-preview.html'
        })	
       
        
});