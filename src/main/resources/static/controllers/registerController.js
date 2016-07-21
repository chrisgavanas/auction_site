var registerController = router.controller('registerController', function($scope, $rootScope, $http, $state, AuthenticationService) {
	$scope.checked = false;
	$scope.user = {};
    $scope.user.address = {};
  
    $scope.register = function(user) {
    	user.registrationDate = new Date();
    	if($scope.checked){
    		AuthenticationService.register(user).then(function (user){
    			$state.go("main.verification");
    		}, function (response) {
    			alert(response.data.message);
    			if (response.data.message === "Username is already in use.") 
    				document.getElementById("inUsePanel1").style.display = "block";
    			if(response.data.message === "Email is already in use.")	
    				document.getElementById("inUsePanel2").style.display = "block";
        	
           
    		});
    	}else{
    		document.getElementById("accepterror").style.display = "block";
    	}	
    };
    
    
    
	
});
