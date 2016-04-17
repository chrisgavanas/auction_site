var loginController = router.controller('loginController', function($scope, $http, $state) {
    $scope.user = {};
    $scope.login = function() {
        $http.post('/api/login', $scope.user).success((res) => {
            alert(res.error || "Success!");
        }).error(function(data, response) {
        	if (response == 400){
        		if($scope.user.username === "" | $scope.user.username === undefined)
        			document.getElementById("usernamePanel").style.display = "block";
        		if($scope.user.password === "" | $scope.user.password === undefined)
        			document.getElementById("passwordPanel").style.display = "block";
        	}
            if (response == 401) {
               // $scope.user.username = ""
                alert(JSON.stringify($scope.user.username));
            }
        });
    };
    
    
    $scope.redirectLogin = function(){
		$state.go("login");
	
	}
	$scope.redirectRegister = function(){
		$state.go("register");
	}
});
