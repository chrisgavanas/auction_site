router.controller('verificationPreviewController', function($state, $scope, $cookies, $http, $stateParams, AuthenticationService, AdminService){
	$scope.userId = $stateParams.id;

	$scope.user1 = {}
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	
	AuthenticationService.getUser($scope.userId, $scope.token)
						.then(function successCallback(response){
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
		
							if(response.status == 404)
								$state.go('main.notfound');
		
						});
	
	$scope.verify = function(){
		AdminService.verify($scope.token, $scope.userId)
					.then(function successCallback(response){
						$state.go('main.admin');
					}, function errorCallback(response){
				
					});
	};
	
})