router.controller('navBarController', function($interval, $state, $scope, $rootScope, $cookies, $http, AuthenticationService, AuctionItemService, MessageService){
	var data = new FormData();
	$scope.user = {};
	$scope.newM = false;

	
	$scope.signedIn = {};
	$scope.categories = [];
	
	
	
	var token = $cookies.get('authToken');
	$scope.user.userId = $cookies.get('userId');
	
	$scope.donotmatch = false;
	
	if($cookies.get('signedIn') == 'yes')
		$scope.signedIn = true;
	else
		$scope.signedIn = false;
	
	var getMessages = function(){
		MessageService.getMessagesByType(token, $scope.user.userId, "RECEIVED")
						.then (function(response){
								$scope.unseenCounter  = 0;
								$scope.messagesReceived = response.data;
								for(var i = 0; i < $scope.messagesReceived.length; i++){
				
									if($scope.messagesReceived[i].seen == false){
										$scope.unseenCounter ++;
				
									}
									if($scope.unseenCounter == 0)
										$scope.newM = false;
									else
										$scope.newM = true;
								}
			
						}, function(response){
							//	console.log(response);
						});

		MessageService.getMessagesByType(token, $scope.user.userId, "SENT")
					.then (function(response){
						//	console.log(response);
						$scope.messagesSent = response.data;

					}, function(response){
						//console.log(response);
					});
	}
	getMessages();


	$rootScope.$on('$stateChangeSuccess', function() {
		getMessages();
		//if($state.current == 'main.profile.userMessages.open')
		
		
	      
	});
	
			
		    

		
	if($scope.signedIn == true){
		AuthenticationService.getUser($scope.user.userId, token)
								.then(function(response){
								$scope.user = response.data;
							}, function errorCallback(response){
								console.log(response);
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								
								$scope.signedIn = false;
							});
		
		
		
		
		
	}
	AuctionItemService.getCategories(token)
							.then(function(response){
									$scope.categories = angular.copy(response.data);
									$scope.defaultCat = {};
									$scope.defaultCat = {categoryId: "0", description: "All Categories"};
									$scope.categories.push($scope.defaultCat);
									$scope.selected = "0";
									
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
		
		$state.go('main.welcome');
		$scope.signedIn = false;
		$scope.user = [];
		$scope.loginform.$submitted = false;
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
    	console.log($scope.userToLogin);
    	AuthenticationService.login($scope.userToLogin)
    							.then(function (response){
    								
    								console.log(response);
    								$scope.userToLogin = {};
    								$scope.loginform.$submitted = false;

									
    								$('#myModal').modal('hide');
    								AuthenticationService.getUser(response.useId, response.authToken)
    									.then(function(response){
    										$scope.signedIn = true;
    										$scope.user = response.data;
    										if($scope.user.isAdmin == true)
    	    									$state.go('main.admin');
    										else
    											$state.go('main.welcome');
    										
    								}, function errorCallback(response){
    										console.log(response);
    										$cookies.remove('userId');
    										$cookies.remove('authToken');
    										$cookies.put('signedIn', 'no');
    										$scope.signedIn = false;
    										
    									});
    							
    								
    								
    							}, function (response) {
    								console.log(response);
    								if (response.status == 401 || response.status == 403) {
    									$scope.donotmatch = true;
               
    								}
            
    							});
    	
    	

    };
	
	$scope.search = function(){
		console.log($scope.selected);
	}
	
	
	
});