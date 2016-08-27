var router = angular.module('router', ['ui.bootstrap', 'ngFileUpload','timer', 'ngMap', 'ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'ngMessages','checklist-model']);


router.config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/welcome');
    
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
        .state('main.seller',{
        	url: '/member?id',
        	controller: 'sellerProfileController',
        	templateUrl: 'seller/seller-profile.html'
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
       
        .state('main.profile.userMessages',{
        	url: '/messages',
        	views: { 'menuview': {
        		controller: 'userMessagesController',
        	    templateUrl: '/user/user-messages.html'
        		}
        	}
        })
        .state('main.profile.userMessages.all',{
        	url: '/all',
        	views: { 'messageview': {
        		controller: 'userMessagesAllController',
        	    templateUrl: '/user/user-messages-all.html'
        		}
        	}
        })
        .state('main.profile.userMessages.recieved',{
        	url: '/recieved',
        	views: { 'messageview': {
        		controller: 'userMessagesRecievedController',
        	    templateUrl: '/user/user-messages-recieved.html'
        		}
        	}
        })
        .state('main.profile.userMessages.sent',{
        	url: '/sent',
        	views: { 'messageview': {
        		controller: 'userMessagesSentController',
        	    templateUrl: '/user/user-messages-sent.html'
        		}
        	}
        })
        .state('main.item', {
    		url: '/item?id',
    		controller: 'itemController',
    		templateUrl: 'auctions/auction-item.html'
    	})
    	
    	.state('main.item.offers', {
    		url: '/offers',
    		views: {'offersview':{
    			controller: 'offersController',
    			templateUrl: 'auctions/auction-item-offers.html'
    			}
    		}
    	})
    	
        .state('main.profile.userAuctions',{
        	url: '/userAuctions',
        	views: { 'menuview': {
        		controller: 'userAuctionsController',
        	    templateUrl: '/user/user-auctions.html'
        		}
        	}
        })
        
        .state('main.profile.userAuctions.active',{
        	url: '/active',
        	views: { 'auctionview': {
        		controller: 'userAuctionsActiveController',
        	    templateUrl: '/user/user-auctions-active.html'
        		}
        	}
        })
        
        .state('main.profile.userAuctions.pending',{
        	url: '/pending',
        	views: { 'auctionview': {
        		controller: 'userAuctionsPendingController',
        	    templateUrl: '/user/user-auctions-pending.html'
        		}
        	}
        })
        
        .state('main.profile.userAuctions.sold',{
        	url: '/sold',
        	views: { 'auctionview': {
        		controller: 'userAuctionsSoldController',
        	    templateUrl: '/user/user-auctions-sold.html'
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
        
         .state('main.editAuction', {
        	url: '/editAuction?id',
        	controller: 'editAuctionController',
        	templateUrl: 'auctions/edit-auction.html'
        })
        
        .state('main.verificationPreview', {
        	url: '/verify?id',
        	controller: 'verificationPreviewController',
        	templateUrl: 'admin/verification-preview.html'
        })	
        
        .state('main.startAuction', {
        	url: '/startAuction?id',
        	views: {
        		'':{ controller: 'userAuctionsStartController',
                	templateUrl: '/user/user-auctions-start.html'},
                	
                'preview@main.startAuction': { templateUrl: '/auctions/auction-item.html',
                					controller: 'itemController'}
                }
        	
        	
        	
        })
        
       
    
    	
        
        
});