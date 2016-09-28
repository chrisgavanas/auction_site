var router = angular.module('router', ['ui.bootstrap', 'ngFileUpload','timer', 'ngMap', 'ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'ngMessages','checklist-model']);


router.config(function($stateProvider, $urlRouterProvider, $compileProvider) {
    
    $urlRouterProvider.otherwise('/welcome');
	$compileProvider.aHrefSanitizationWhitelist(/^\s*(|blob|):/);
	
    $stateProvider
        
    .state('main', {
    	
    	views: { '': {
    							templateUrl: './components/nav-bar.html',
    							controller: 'navBarController'
    			}
    		//	'': {
    		//		templateUrl: './main/main.html'
    		//	}
    						
    	}
    })
    	.state('main.welcome', {
    		url: '/welcome',
    		views: { 'mainview': {
    			
    			controller: 'welcomeController',
    			templateUrl: 'main/welcome.html',
    		}}
    		
    	})
    	
       .state('main.register', {
        	url: '/register',
        	views: { 'mainview': {
        		controller: 'registerController',
        		templateUrl: 'register/register.html'
        	}}
        })
        
        .state('main.verification',{
        	url: '/pendingMessage',
        	views: { 'mainview': {
        		templateUrl: 'messages/verification-pending.html'
        	}}
        })
        
        .state('main.signedout',{
        	url: '/not-signed-in',
        	views: { 'mainview': {
        		templateUrl: 'messages/not-logged-in.html'
        	}}
        	
        })
        
        .state('main.notfound',{
        	url: '/notfound',
        	views: { 'mainview': {
        		templateUrl: 'messages/not-found.html'
        	}}
        	
        })
        
        .state('main.forbidden',{
        	url: '/forbidden',
        	views: { 'mainview': {
        		templateUrl: 'messages/forbidden.html'
        	}}
        	
        })
        
        .state('main.biddingexamples',{
        	url: '/bidding-examples',
        	views: { 'mainview': {
        		templateUrl: 'messages/bidding-examples.html'
        	}}
        	
        })
        .state('main.seller',{
        	url: '/member?id',
        	views: { 'mainview': {
        		controller: 'sellerProfileController',
        		templateUrl: 'seller/seller-profile.html'
        	}}
        })
        .state('main.search', {
        	url: '/search?catId&input&country&from&to&sellerId',
        	views: { 'mainview': {
        		controller: 'searchController',
        		templateUrl: 'search/search.html'
        	}},
        	 params: {myParam: null}

        })
        .state('main.profile', {
        	url: '/profile',
        	views: { 'mainview': {
        		controller: 'profileController',
        		templateUrl: 'user/profile.html'
        	}}

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
       
        .state('main.profile.userMessages.received',{
        	url: '/received',
        	views: { 'messageview': {
        		controller: 'userMessagesReceivedController',
        	    templateUrl: '/user/user-messages-received.html'
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
        
         .state('main.profile.userMessages.open',{
        	
        	views: { 'messageview': {
        		controller: 'userMessagesOpenController',
        	    templateUrl: '/user/user-messages-open.html'
        		}
        	}
        })
        .state('main.item', {
    		url: '/item?id',
    		views: { 'mainview': {
    			controller: 'itemController',
    			templateUrl: 'auctions/auction-item.html'
    		}}
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
        
        .state('main.profile.userAuctions.active.offers', {
    		url: '/offers',
    		views: {'offersview@':{
    			controller: 'offersController',
    			templateUrl: 'auctions/auction-item-offers.html'
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
    			views: { 'mainview': {
    				controller: 'adminOptionsController',
    				templateUrl: 'admin/admin-options.html'
    			}}
    	})
    
    	.state('main.verified', {
    			url: '/verifiedUsers',
    			views: { 'mainview': {
    				controller: 'adminOptionsVerifiedController',
    				templateUrl: 'admin/admin-options-verified.html'
    			}}
    	})
    
    	.state('main.unverified', {
    			url: '/unverifiedUsers',
    			views: { 'mainview': {
    				controller: 'adminOptionsUnverifiedController',
    				templateUrl: 'admin/admin-options-unverified.html'
    			}}
    	})
    	
        .state('main.newAuction', {
        	url: '/newAuction',
        	views: { 'mainview': {
        		controller: 'newAuctionController',
        		templateUrl: 'auctions/new-auction.html'
        	}}
        })
        
         .state('main.editAuction', {
        	url: '/editAuction?id',
        	views: { 'mainview': {
        	controller: 'editAuctionController',
        	templateUrl: 'auctions/edit-auction.html'
        	}}
        })
        
        .state('main.verificationPreview', {
        	url: '/verify?id',
        	views: { 'mainview': {
        	controller: 'verificationPreviewController',
        	templateUrl: 'admin/verification-preview.html'
        	}}
        })	
        
        .state('main.startAuction', {
        	url: '/startAuction?id',
        	views: {
        		'mainview':{ controller: 'userAuctionsStartController',
                	templateUrl: '/user/user-auctions-start.html'},
                	
                'preview@main.startAuction': { templateUrl: '/auctions/auction-item.html',
                					controller: 'itemController'}
                }
        	
        	
        	
        })
        
       
    
    	
        
        
});

router.run(['$rootScope', '$uibModalStack', function($rootScope, $uibModalStack){
	$rootScope.$on('$stateChangeStart', 
			function(event, toState, toParams, fromState, fromParams){ 
					console.log('a');
					$uibModalStack.dismissAll();
			})                                                                                                                                                                  
    
}]); 


