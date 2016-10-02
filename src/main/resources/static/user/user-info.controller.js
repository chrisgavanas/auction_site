router.controller('userInfoController', function($state, $scope, $cookies, $http, AuthenticationService){
	$scope.paswords = {};
	$scope.passwordChanged = false;
	$scope.invalidPass = false;
	$scope.different = false;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	

							
								
								
	$scope.show = function(field){
		
		document.getElementById(field+"Form").style.display = "block";
		document.getElementById(field+"But").style.display = "none";
		document.getElementById(field+"Field").style.display = "none";
	};
	
	$scope.applyChanges = function(user){
		AuthenticationService.updateUser(user, $scope.token).then(function (response){
			$state.go($state.current, {}, {reload: true});
		}, function(response){
			console.log(response);
			
		});
		
		
	};
	
	$scope.changePassword = function(passwords){
		AuthenticationService.changePassword(passwords, $scope.user.userId, $scope.token)
								.then(function(response){
									$scope.passwordChanged = true;
									$scope.invalidPass = false;
									$scope.different = false;
								}, function(response){
									$scope.passwordChanged = false;
									$scope.invalidPass = false;
									$scope.different = false;
								
									if(response.data.message == "Password is invalid.")
										$scope.invalidPass = true;
									else if(response.data.message == "New password must differ with the old password.")
										$scope.different = true;
									
			
								});
	};
	
	$scope.adminOptions = function(){
		$state.go('main.admin');
	};
	
	 var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
	    var mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})");

	    $scope.passwordStrength = {
	    	"position": "relative",
	        "width": "0%",
	        "height": "8px",
	        
	        "border-radius": "3px"
	        
	    };

	    $scope.strength = null;
	    $scope.analyze = function(value) {
	        if(strongRegex.test(value)) {
	            $scope.passwordStrength["background-color"] = "green";
	            $scope.passwordStrength["width"] = "100%";
	            $scope.strength = "Strong";
	        } else if(mediumRegex.test(value)) {
	            $scope.passwordStrength["background-color"] = "orange";
	            $scope.passwordStrength["width"] = "66%";
	            $scope.strength = "Medium";
	        } else {
	            $scope.passwordStrength["background-color"] = "red";
	            $scope.strength = "Weak";
	            $scope.passwordStrength["width"] = "33%";
	        }
	    };

	
	
	
});