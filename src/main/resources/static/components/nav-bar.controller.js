router.controller('navBarController', function($interval, $state, $scope, $rootScope, $cookies, $http, AuthenticationService, AuctionItemService, MessageService){
	$scope.searchData = {};
	$scope.donotmatch = false;
	$scope.newM = false;
	$scope.signedIn = {};
	$scope.categories = {};
	$scope.categories.category = {};
	$scope.unseenCounter = 0;
	$scope.signedIn = false;
	$scope.user = {};
	$scope.token = null;
	$scope.contact = null;
	$scope.text = null;
	$scope.bytes = [];
	var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
	 
	
	if($cookies.get('signedIn') == 'yes'){
		$scope.signedIn = true;
		$scope.token = $cookies.get('authToken');
		$scope.user.userId = $cookies.get('userId');
	}else{
		$scope.signedIn = false;
	}

	var getMessages = function(){
		MessageService.getMessagesByType($scope.token, $scope.user.userId, "RECEIVED")
						.then (function(response){
								$scope.unseenCounter  = 0;
								$scope.messagesReceived = response.data;
							
								for(var i = 0; i < $scope.messagesReceived.length; i++){
				
									if($scope.messagesReceived[i].seen == false){
										$scope.unseenCounter ++;
				
									}
									
									
								}
			
						}, function(response){
							console.log(response);
						});

		MessageService.getMessagesByType($scope.token, $scope.user.userId, "SENT")
					.then (function(response){
						$scope.messagesSent = response.data;

					}, function(response){
					});
	}
	if($scope.signedIn == true){
		AuthenticationService.getUser($scope.user.userId, $scope.token)
								.then(function(response){
								$scope.user = response.data;

								$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date($scope.user.dateOfBirth));
								$scope.user.dateOfBirth = new Date($scope.user.dateOfBirth);
										
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date($scope.user.registrationDate));
								$scope.user.registrationDate = new Date($scope.user.registrationDate);
								if($scope.user.gender == "F")
									$scope.gender = "Female";
								else
									$scope.gender = "Male";
								
								if($scope.user.isAdmin == true)
									$scope.user.type = "Administrator";
								else
									$scope.user.type = "User";
							}, function errorCallback(response){
								
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								
								$scope.signedIn = false;
								$state.go('main.signedout');
							});
		getMessages();
		AuctionItemService.recommend($scope.token, $scope.user.userId)
							.then(function(response){
								$scope.recommendations = response.data;
						
								for (i = 0; i < $scope.recommendations.length; i++){
								
									if($scope.recommendations[i].images.length > 0){
										AuctionItemService.getImage($scope.recommendations[i].images[0])
		                    					.then(function(response){
		                    						$scope.recommendations[i].displayImage = 'data:image/jpg;base64,' + response.data;
		                    		
		                    					}, function(response){
		                    						console.log(response);
		                    					});
					
										
						
									}else{
										$scope.recommendations[i].displayImage = './images/item.png';
									}
			
								}
							}, function(response){
								console.log(response);
							});
	}


	$rootScope.$on('$stateChangeSuccess', function() {
		if($scope.signedIn == true){
			AuthenticationService.getUser($scope.user.userId, $scope.token)
									.then(function(response){
									$scope.user = response.data;

									$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date($scope.user.dateOfBirth));
									$scope.user.dateOfBirth = new Date($scope.user.dateOfBirth);
											
									$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date($scope.user.registrationDate));
									$scope.user.registrationDate = new Date($scope.user.registrationDate);
									if($scope.user.gender == "F")
										$scope.gender = "Female";
									else
										$scope.gender = "Male";
									
									if($scope.user.isAdmin == true)
										$scope.user.type = "Administrator";
									else
										$scope.user.type = "User";
								}, function errorCallback(response){
									
									$cookies.remove('userId');
									$cookies.remove('authToken');
									$cookies.put('signedIn', 'no');
									
									$scope.signedIn = false;
									$state.go('main.signedout');
								});
			getMessages();
			
			AuctionItemService.recommend($scope.token, $scope.user.userId)
			.then(function(response){
				$scope.recommendations = response.data;
		
			}, function(response){
				console.log(response);
			});
		}else{
			$cookies.remove('userId');
			$cookies.remove('authToken');
			$cookies.put('signedIn', 'no');
			
			$scope.signedIn = false;
		}
			      
	});
	
			
		    

		
	
		
		
		
		
		

	AuctionItemService.getCategories($scope.token)
							.then(function(response){
									$scope.categories.array = angular.copy(response.data);
									$scope.categories.array.push({categoryId: 'ALL', description: 'All Categories'});
									$scope.categories.category = $scope.categories.array[$scope.categories.array.length - 1];
									
							}, function(response){
								console.log(response);
							});
	
	$scope.redirectRegister = function(){
		$state.go("main.register");
	}
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		$scope.signedIn = false;
		$scope.user = {};
		$scope.loginform.$submitted = false;
		$scope.token = null;
		$scope.recommendations = [];
		$state.go('main.welcome');
		
	}
	
	$scope.myProfile = function(){
		$state.go("main.profile.userInfo");
	}
	$scope.auctions = function(){
		$state.go("main.profile.userAuctions");
	}
	$scope.bids = function(){
		$state.go("main.profile.userBids");
	}
	$scope.messages = function(){
		$state.go("main.profile.userMessages.received");
	}

	$scope.userToLogin = {};
    $scope.login = function() {
    	if(EMAIL_REGEXP.test($scope.field)){
    		$scope.userToLogin.email = $scope.field;
    		$scope.userToLogin.username = null;
    		
    	}else{
    		$scope.userToLogin.email = null;
    		$scope.userToLogin.username = $scope.field;
    	}
    	
    	AuthenticationService.login($scope.userToLogin)
    							.then(function (response){
    								
    								$scope.userToLogin = {};
    								$scope.loginform.$submitted = false;

    								$scope.token = response.authToken;
    								$('#myModal').modal('hide');
    								AuthenticationService.getUser(response.useId, response.authToken)
    									.then(function(response){
    										
    										$scope.signedIn = true;
    										$scope.user = response.data;
    										if($scope.user.isAdmin == true){
    											
    	    									$state.go('main.admin');
    										}else{
    											$state.go('main.welcome');
    										}
    										
    								}, function errorCallback(response){
    										
    										$cookies.remove('userId');
    										$cookies.remove('authToken');
    										$cookies.put('signedIn', 'no');
    										$scope.signedIn = false;
    										$scope.user = {};
    									});
    								
    								
    							
    							}, function (response) {
    								
    								if (response.status == 401 || response.status == 403) {
    									$scope.donotmatch = true;
               
    								}
    							});
    	
    	

    };
	
	$scope.search = function(){
		
		
		
	
	
		$state.go('main.search', {input: $scope.searchData.text, catId: $scope.categories.category.categoryId});
	}
	
	$scope.gotomessages = function(){
		$state.go('main.profile.userMessages.received');
	}
	
});