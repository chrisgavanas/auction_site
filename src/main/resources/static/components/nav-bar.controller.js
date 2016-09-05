router.controller('navBarController', function($state, $scope, $rootScope, $cookies, $http, AuthenticationService, AuctionItemService){
	var data = new FormData();
	$scope.user = {};
	$scope.signedIn = {};
	$scope.categories = [];
	var token = $cookies.get('authToken');
	$scope.user.userId = $cookies.get('userId');
	
	if($cookies.get('signedIn') == 'yes')
		$scope.signedIn = true;
	else
		$scope.signedIn = false;
	
	
	if($scope.signedIn == true)
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
		if($state.current.name != 'main.welcome'){
			$state.go('main.welcome', {}, {reload: true});
		}else
			$state.go($state.current, {}, {reload: true});	
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
		$state.go("main.profile.userMessages");
	}
	var modal = document.getElementById('myModal');

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal
	
/*	$scope.openModal = function() {
		
	    modal.style.display = "block";
	}

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
	    modal.style.display = "none";
	}

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
	    if (event.target == modal) {
	        modal.style.display = "none";
	    }
	}
	*/
	$scope.user = {};
    $scope.login = function(user) {
    	console.log("mohka");
    	AuthenticationService.login(user, token)
    							.then(function (response){
    								$scope.user = response;
    								console.log($scope.user);
    								$('#myModal').modal('hide');
    								AuthenticationService.getUser($scope.user.useId, $scope.user.authToken)
    									.then(function(response){
    										$scope.user = response.data;
    										$scope.signedIn = true;
    								}, function errorCallback(response){
    										console.log(response);
    										$cookies.remove('userId');
    										$cookies.remove('authToken');
    										$cookies.put('signedIn', 'no');
    										$scope.signedIn = false;
    										
    									});
    							
    								if($scope.user.isAdmin == true)
    									$state.go('main.admin');
    								//else
    								//	$state.go('main.welcome', {}, {reload: true});
    							}, function (response) {
    								if (response.status == 400){
    									if($scope.user.username === "" | $scope.user.username === undefined)
    										document.getElementById("usernamePanel").style.display = "block";
    									if($scope.user.password === "" | $scope.user.password === undefined)
    										document.getElementById("passwordPanel").style.display = "block";
        		
    								}
    								if (response.status == 401 || response.status == 403) {
    									document.getElementById("donotmatchPanel").style.display = "block";
               
    								}
            
    							});
    	
    	

    };
	
	$scope.search = function(){
		console.log($scope.selected);
	}
	
});