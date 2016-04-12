var registerController = router.controller('registerController', function($scope, $http, $state) {
    $scope.user = {};
    $scope.register = function() {
        $scope.user.registrationDate = new Date();

        $http.post('/api/user', $scope.user).success((res) => {
            alert(res.error || "Success!");
        }).error(function(data, response) {
            alert(data.message);
        });
    };
    
    
    $scope.redirectLogin = function(){
		$state.go("login");
	
	};	
	$scope.redirectRegister = function(){
		$state.go("register");
	};	
});
