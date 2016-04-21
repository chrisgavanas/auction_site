var registerController = router.controller('registerController', function($scope, $http, $state, AuthenticationService) {
    $scope.user = {};
    $scope.register = function(user) {
    	AuthenticationService.register(user).then(function (user){
    		$state.go("welcome");
    	}, function (response) {
    		alert(response.statusText+response.status);
    		if (response.statusText == "Username is already in use.") 
        		document.getElementById("inUsePanel1").style.display = "block";
    		if(response.statusText == "Email is already in use.")	
    			document.getElementById("inUsePanel2").style.display = "block";
        	
           
    	});
    };
    
    
    $scope.redirectLogin = function(){
		$state.go("login");
	
	};	
	$scope.redirectRegister = function(){
		$state.go("register");
	};	
});
