router.controller('navBarController', function($interval, $state, $scope, $rootScope, $cookies, $http, AuthenticationService, AuctionItemService, MessageService){
	$scope.searchData = {};
	$scope.donotmatch = false;
	$scope.newM = false;
	$scope.signedIn = {};
	$scope.categories = {};
	$scope.categories.category = {};
	
	console.log($scope.signedIn);
	$scope.user = {};
	$scope.token = null;
	$scope.contact = null;
	$scope.text = null;
	
	
	if($cookies.get('signedIn') == 'yes'){
		$scope.signedIn = true;
		$scope.token = $cookies.get('authToken');
		$scope.user.userId = $cookies.get('userId');
	}else{
		$scope.signedIn = false;
	}
	console.log($scope.token);
	console.log($scope.user.userId);
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
							//	console.log(response);
						});

		MessageService.getMessagesByType($scope.token, $scope.user.userId, "SENT")
					.then (function(response){
						//	console.log(response);
						$scope.messagesSent = response.data;

					}, function(response){
						//console.log(response);
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
    	
    	AuthenticationService.login($scope.userToLogin)
    							.then(function (response){
    								
    								console.log(response);
    								$scope.userToLogin = {};
    								$scope.loginform.$submitted = false;

    								$scope.token = response.authToken;
    								$('#myModal').modal('hide');
    								AuthenticationService.getUser(response.useId, response.authToken)
    									.then(function(response){
    										console.log(response);
    										$scope.signedIn = true;
    										$scope.user = response.data;
    										if($scope.user.isAdmin == true){
    											console.log("mohka");
    	    									$state.go('main.admin');
    										}else{
    											$state.go('main.welcome');
    										}
    										
    								}, function errorCallback(response){
    										console.log(response);
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