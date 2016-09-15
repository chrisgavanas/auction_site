router.controller('verificationPreviewController', function($state, $scope, $cookies, $http, $stateParams){
	$scope.userId = $stateParams.id;

	$scope.user1 = {}
	
	
	$http.get('/api/user/'+ $scope.userId, {headers: {'authToken': $scope.token}}).then(function successCallback(response){
		$scope.user1 = angular.copy(response.data);
		if($scope.user1.gender == "F")
			$scope.gender = "Female";
		else
			$scope.gender = "Male";
		
		if($scope.user1.isAdmin == true)
			$scope.user1.type = "Administrator";
		else
			$scope.user1.type = "User";
		
		$scope.dateOfBirthConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.dateOfBirth));
		$scope.user1.dateOfBirth = new Date(response.data.dateOfBirth);
		
		$scope.registrationDateConverted = $.datepicker.formatDate("M d, yy", new Date(response.data.registrationDate));
		$scope.user1.registrationDate = new Date(response.data.registrationDate);
	}, function errorCallback(response){
		
		
		
	});
	
	$scope.verify = function(){
		$http.post('/api/user/'+$scope.userId+'/verify-user', {}, {headers: {'authToken': $scope.token}}).then(function successCallback(response){
			$state.go('main.admin');
		}), function errorCallback(response){
			alert('error sti verify');
		}
	};
	
})