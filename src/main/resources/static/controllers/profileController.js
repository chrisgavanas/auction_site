var profileController = router.controller('profileController', function($scope, $state, $http,$cookies, $route, AuthenticationService){
	$scope.user = {};
	
	$scope.signedIn = {};
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId).then(function successCallback(response){
			$scope.user = angular.copy(response.data);
			
			if($scope.user.gender == "F")
				$scope.gender = "Female";
			else
				$scope.gender = "Male";
			
			if($scope.user.isAdmin == true)
				$scope.user.type = "Administrator";
			else
				$scope.user.type = "User";
			
			$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.dateOfBirth));
			$scope.user.dateOfBirth = new Date(response.data.dateOfBirth);

		}, function errorCallback(response){
			alert("error");
			
			
		});
	
		$http.get('api/auctionitem/user/'+ $scope.user.userId).then(function successCallback(response){
			$scope.hasAuctions = false;
			if(response.data.length != 0)
				$scope.hasAuctions = true;
			
		}, function errorCallback(response){
			alert("error");
		});
		
	}else
		$scope.signedIn = false;
	
	
	$scope.redirectLogin = function(){
		 $state.go("login");
		
	};	
	$scope.redirectRegister = function(){
		$state.go("register");
	};	
	
	$scope.logout = function(){
		$cookies.remove('userId');
		$cookies.remove('authToken');
		$cookies.put('signedIn', 'no');
		$state.go("welcome"); 
	};
	
	$scope.show = function(field){
	
		document.getElementById(field+"Form").style.display = "block";
		document.getElementById(field+"Cont").style.backgroundColor = "#edf1f4";
		document.getElementById(field+"But").style.display = "none";
		document.getElementById(field+"Field").style.display = "none";
	};
	
	$scope.applyChanges = function(user){
		AuthenticationService.updateUser(user).then(function (response){
			console.log(response);
			
			
		}, function(response){
			console.log(response);
		});
		$state.go($state.current, {}, {reload: true});
		
	};
	
	$scope.newAuction = function(){
		$state.go("newAuction");
	};
});