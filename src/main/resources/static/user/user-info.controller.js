router.controller('userInfoController', function($state, $scope, $cookies, $http, AuthenticationService){
	$scope.paswords = {};
	$scope.user = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
		
	AuthenticationService.getUser($scope.user.userId, token)
							.then(function(response){
								$scope.user = angular.copy(response.data);

								$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.dateOfBirth));
								$scope.user.dateOfBirth = new Date(response.data.dateOfBirth);
			
								$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
								$scope.user.registrationDate = new Date(response.data.registrationDate);
								prepareData(response);
							}, function errorCallback(response){
								$cookies.remove('userId');
								$cookies.remove('authToken');
								$cookies.put('signedIn', 'no');
								$state.go('main.welcome');
							});
	// scope functions //
	$scope.show = function(field){
		console.log(field+"Form");
		document.getElementById(field+"Form").style.display = "block";
		document.getElementById(field+"But").style.display = "none";
		document.getElementById(field+"Field").style.display = "none";
	};
	
	$scope.applyChanges = function(user){
		AuthenticationService.updateUser(user).then(function (response){
			$state.go($state.current, {}, {reload: true});
		}, function(response){
			
		});
		
		
	};
	
	$scope.changePassword = function(passwords){
		AuthenticationService.changePassword(passwords, $scope.user.userId, token)
								.then(function(response){
									$state.go($state.current);
								}, function(response){
									if(response.data.message == "Password is invalid")
										document.getElementById("invalidPass").style.display = "block";
									else if(response.data.message == "New password must differ with the old password.")
										document.getElementById("differentPass").style.display = "block";
									
			
								});
	};
	
	$scope.adminOptions = function(){
		$state.go('main.admin');
	};
	
	// other functions //
	var prepareData = function(response){
		if($scope.user.gender == "F")
			$scope.gender = "Female";
		else
			$scope.gender = "Male";
		
		if($scope.user.isAdmin == true)
			$scope.user.type = "Administrator";
		else
			$scope.user.type = "User";
	}
});