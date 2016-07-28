router.controller('userpreviewController', function($state, $scope, $cookies, $http, $stateParams){
	$scope.userId = $stateParams.id;
	console.log($scope.userId);
	$scope.user = {}
	var token = $cookies.get('authToken');
	
	$http.get('/api/user/'+ $scope.userId, {headers: {'authToken': token}}).then(function successCallback(response){
		$scope.user = angular.copy(response.data);
		if($scope.user.gender == "F")
			$scope.gender = "Female";
		else
			$scope.gender = "Male";
		
		if($scope.user.isAdmin == true)
			$scope.user.type = "Administrator";
		else
			$scope.user.type = "User";
		
		$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.dateOfBirth));
		$scope.user.dateOfBirth = new Date(response.data.dateOfBirth);
		
		$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
		$scope.user.registrationDate = new Date(response.data.registrationDate);
	}, function errorCallback(response){
		alert("error");
		
		
	});
	
	$scope.verify = function(){
		$http.post('/api/user/'+$scope.userId+'/verify-user', {}, {headers: {'authToken': token}}).then(function successCallback(response){
			$state.go('main.admin');
		}), function errorCallback(response){
			alert('error sti verify');
		}
	};
	
})