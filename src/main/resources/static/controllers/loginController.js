var loginController = router.controller('loginController', function($scope, $http, $state, AuthenticationService) {
    $scope.user = {};
    $scope.login = function(user) {
    	AuthenticationService.login(user).then(function (user){
    		$state.go("welcomeUser");
    	}, function (response) {
    		if (response.status == 400){
        		if($scope.user.username === "" | $scope.user.username === undefined)
        			document.getElementById("usernamePanel").style.display = "block";
        		if($scope.user.password === "" | $scope.user.password === undefined)
        			document.getElementById("passwordPanel").style.display = "block";
        		
        	}
            if (response.status == 401) {
                alert(JSON.stringify($scope.user.username));
               
            }
    	});
    };
    
 
    $scope.redirectLogin = function(){
		$state.go("login");
	
	};
	$scope.redirectRegister = function(){
		$state.go("register");
	};	
});
