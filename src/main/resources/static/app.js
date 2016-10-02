var router = angular.module('router', ['ui.bootstrap', 'ngFileUpload','timer', 'ngMap', 'ui.router', 'ngResource', 'ngCookies', 'ngRoute', 'ngMessages','checklist-model']);


router.config(function($stateProvider, $urlRouterProvider, $compileProvider) {
    
    $urlRouterProvider.otherwise('/welcome');
	$compileProvider.aHrefSanitizationWhitelist(/^\s*(|blob|):/);
	
    $stateProvider
        
    .state('main', {
    	
    	views: { '': {
    							templateUrl: './components/nav-bar.html',
    							controller: 'navBarController'
    			},
    			
    	
    						
    	},data : { pageTitle: 'main' }
    })
    	.state('main.welcome', {
    		url: '/welcome',
    		views: { 'mainview': {
    			
    			controller: 'welcomeController',
    			templateUrl: 'main/welcome.html',
    		}},
    		data : { pageTitle: "" }
    		
    	})
    	
       .state('main.register', {
        	url: '/register',
        	views: { 'mainview': {
        		controller: 'registerController',
        		templateUrl: 'register/register.html'
        	}},
        	data : { pageTitle: 'Register' }
        })
        
        .state('main.verification',{
        	url: '/pendingMessage',
        	views: { 'mainview': {
        		templateUrl: 'messages/verification-pending.html'
        	}}
        	,data : { pageTitle: 'Verification' }
        })
        
        .state('main.signedout',{
        	url: '/not-signed-in',
        	views: { 'mainview': {
        		templateUrl: 'messages/not-logged-in.html'
        	}},
        data : { pageTitle: 'Signed out' }
        	
        })
        
        .state('main.notfound',{
        	url: '/notfound',
        	views: { 'mainview': {
        		templateUrl: 'messages/not-found.html'
        	}},
        	data : { pageTitle: 'Not found' }
        	
        })
        
        .state('main.forbidden',{
        	url: '/forbidden',
        	views: { 'mainview': {
        		templateUrl: 'messages/forbidden.html'
        	}},
        	data : { pageTitle: 'Forbidden' }
        	
        })
        
        .state('main.biddingexamples',{
        	url: '/bidding-examples',
        	views: { 'mainview': {
        		templateUrl: 'messages/bidding-examples.html'
        	}},data : { pageTitle: 'Examples' }
        	
        })
        .state('main.seller',{
        	url: '/member?id',
        	views: { 'mainview': {
        		controller: 'sellerProfileController',
        		templateUrl: 'seller/seller-profile.html'
        	}}
        ,data : { pageTitle: 'Profile' }
        })
        .state('main.search', {
        	url: '/search?catId&input&country&from&to&sellerId',
        	views: { 'mainview': {
        		controller: 'searchController',
        		templateUrl: 'search/search.html'
        	}},
        	 params: {myParam: null},
        	 data : { pageTitle: 'Search' }

        })
        .state('main.profile', {
        	url: '/profile',
        	views: { 'mainview': {
        		controller: 'profileController',
        		templateUrl: 'user/profile.html'
        	}},
        	data : { pageTitle: 'My Profile' }

        })
        .state('main.profile.userInfo',{
        	url: '/userInfo',
        	
        	views: { 'menuview': {
        		controller: 'userInfoController',
        	    templateUrl: '/user/user-info.html'
        		}
        	},
        	data : { pageTitle: 'Profile Info' }
        })
       
        .state('main.profile.userMessages',{
        	url: '/messages',
        	views: { 'menuview': {
        		controller: 'userMessagesController',
        	    templateUrl: '/user/user-messages.html'
        		}
        	},
        data : { pageTitle: 'My Mesages' }
        })
       
        .state('main.profile.userMessages.received',{
        	url: '/received',
        	views: { 'messageview': {
        		controller: 'userMessagesReceivedController',
        	    templateUrl: '/user/user-messages-received.html'
        		}
        	},
        	data : { pageTitle: 'Received Messages' }
        })
        .state('main.profile.userMessages.sent',{
        	url: '/sent',
        	views: { 'messageview': {
        		controller: 'userMessagesSentController',
        	    templateUrl: '/user/user-messages-sent.html'
        		}
        	},
        	data : { pageTitle: 'Sent Messages' }
        })
        
         .state('main.profile.userMessages.open',{
        	views: { 'messageview': {
        		controller: 'userMessagesOpenController',
        	    templateUrl: '/user/user-messages-open.html'
        		}
        	},
        	data : { pageTitle: 'Open Message'}
        })
        .state('main.item', {
    		url: '/item?id',
    		views: { 'mainview': {
    			controller: 'itemController',
    			templateUrl: 'auctions/auction-item.html'
    		}},
    		data : { pageTitle: 'Item' }
    	})
    	
    	
    	
        .state('main.profile.userAuctions',{
        	url: '/userAuctions',
        	views: { 'menuview': {
        		controller: 'userAuctionsController',
        	    templateUrl: '/user/user-auctions.html'
        		}
        	},
        	data : { pageTitle: 'My Auctions' }
        })
        
        .state('main.profile.userAuctions.active',{
        	title: 'Auctions',
        	url: '/active',
        	views: { 'auctionview': {
        		controller: 'userAuctionsActiveController',
        	    templateUrl: '/user/user-auctions-active.html'
        		}
        	},
        	data : { pageTitle: 'Active Auctions' }
        })
        
        .state('main.profile.userAuctions.active.offers', {
    		url: '/offers',
    		views: {'offersview@':{
    			controller: 'offersController',
    			templateUrl: 'auctions/auction-item-offers.html'
    			}
    		},
        data : { pageTitle: 'Active Auctions | Offers' }
    	})
    	
        .state('main.profile.userAuctions.pending',{
        	url: '/pending',
        	views: { 'auctionview': {
        		controller: 'userAuctionsPendingController',
        	    templateUrl: '/user/user-auctions-pending.html'
        		}
        	},data : { pageTitle: 'Inactive Auctions' }
        })
        
        .state('main.profile.userAuctions.sold',{
        	url: '/sold',
        	views: { 'auctionview': {
        		controller: 'userAuctionsSoldController',
        	    templateUrl: '/user/user-auctions-sold.html'
        		}
        	},data : { pageTitle: 'Sold Auctions' }
        })
        
        
    
    	
        .state('main.admin', {
    			url: '/adminOptions',
    			views: { 'mainview': {
    				controller: 'adminOptionsController',
    				templateUrl: 'admin/admin-options.html'
    			}},data : { pageTitle: 'Admin' }
    	})
    
    	.state('main.verified', {
    			url: '/verifiedUsers',
    			views: { 'mainview': {
    				controller: 'adminOptionsVerifiedController',
    				templateUrl: 'admin/admin-options-verified.html'
    			}},data : { pageTitle: 'Verified' }
    	})
    
    	.state('main.unverified', {
    			url: '/unverifiedUsers',
    			views: { 'mainview': {
    				controller: 'adminOptionsUnverifiedController',
    				templateUrl: 'admin/admin-options-unverified.html'
    			}},data : { pageTitle: 'Unverified' }
    	})
    	
        .state('main.newAuction', {
        	url: '/newAuction',
        	views: { 'mainview': {
        		controller: 'newAuctionController',
        		templateUrl: 'auctions/new-auction.html'
        	}},data : { pageTitle: 'New Auction' }
        })
        
         .state('main.editAuction', {
        	url: '/editAuction?id',
        	views: { 'mainview': {
        	controller: 'editAuctionController',
        	templateUrl: 'auctions/edit-auction.html'
        	}},data : { pageTitle: 'Edit Auction' }
        })
        
        .state('main.verificationPreview', {
        	url: '/verify?id',
        	views: { 'mainview': {
        	controller: 'verificationPreviewController',
        	templateUrl: 'admin/verification-preview.html'
        	}},data : { pageTitle: 'Preview' }
        })	
        
        .state('main.startAuction', {
        	url: '/startAuction?id',
        	views: {
        		'mainview':{ controller: 'userAuctionsStartController',
                	templateUrl: '/user/user-auctions-start.html'},
                	
                'preview@main.startAuction': { templateUrl: '/auctions/auction-item.html',
                					controller: 'itemController'}
                },data : { pageTitle: 'Start Auction' }
        	
        	
        	
        })
        
       
    
    	
        
        
});

router.run([
             '$log', '$rootScope', '$window', '$state', '$location',
             function($log, $rootScope, $window, $state, $location) {

                 $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
                	 $('.modal-backdrop').remove();
                	 $('body').removeClass('modal-open');
                	 document.getElementsByTagName("body")[0].style = 'padding-right: 0';
                     if (toState.data.pageTitle ) {
                         document.title = toState.data.pageTitle + ' | Auction';
                        
                     }else
                    	 document.title = 'Auction';
                 });

                

                
             }
]);

