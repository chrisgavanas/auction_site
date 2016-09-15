router.controller('userInfoController', function($state, $scope, $cookies, $http, AuthenticationService){
	$scope.paswords = {};
	console.log($scope.user);
		
	

							
								
								
	$scope.show = function(field){
		console.log(field+"Form");
		document.getElementById(field+"Form").style.display = "block";
		document.getElementById(field+"But").style.display = "none";
		document.getElementById(field+"Field").style.display = "none";
	};
	
	$scope.applyChanges = function(user){
		AuthenticationService.updateUser(user, $scope.token).then(function (response){
			console.log(response);
			$state.go($state.current, {}, {reload: true});
		}, function(response){
			console.log(response);
			
		});
		
		
	};
	
	$scope.changePassword = function(passwords){
		AuthenticationService.changePassword(passwords, $scope.user.userId, $scope.token)
								.then(function(response){
									$state.go($state.current);
								}, function(response){
									if(response.data.message == "Password is invalid")
										document.getElementById("invalidPass").style.display = "block";
									else if(response.data.message == "New password must differ with the old password.")
										document.getElementById("differentPass").style.display = "block";
									console.log(response);
			
								});
	};
	
	$scope.adminOptions = function(){
		$state.go('main.admin');
	};
	
	// other functions //
	
	
	
});