var profileController = router.controller('profileController', function($scope, $state, $http,$cookies, $route){
	$scope.user = {};
	$scope.signedIn = {};
	if($cookies.get('signedIn') === 'yes'){
		$scope.user.userId = $cookies.get('userId');
		$scope.signedIn = true;
		$http.get('/api/user/'+ $scope.user.userId).then(function successCallback(response){
			$scope.user = response.data;
			if($scope.user.gender == "F")
				$scope.user.gender = "Female";
			else
				$scope.user.gender = "Male";
			
			if($scope.user.isAdmin == true)
				$scope.user.type = "Administrator";
			else
				$scope.user.type = "User";
			$scope.user.dateOfBirth = $.datepicker.formatDate("M d, yy", new Date(response.data.dateOfBirth));
			console.log(response.data);

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
	}
	
	$scope.show = function(){
		document.getElementById("usernameForm").style.display = "block";
		document.getElementById("usernameCont").style.backgroundColor = "#edf1f4";
		document.getElementById("usernameForm").style.backgroundColor = "#edf1f4";
		document.getElementById("usernameBut").style.display = "none";
	}
});