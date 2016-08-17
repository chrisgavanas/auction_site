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
    			console.log(response);
    			if (response.data.message === "Username is already in use.") 
    				document.getElementById("inUsePanel1").style.display = "block";
    			if(response.data.message === "Email is already in use.")	
    				document.getElementById("inUsePanel2").style.display = "block";
        	
           
    		});
    	}else{
    		document.getElementById("accepterror").style.display = "block";
    	}	
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
