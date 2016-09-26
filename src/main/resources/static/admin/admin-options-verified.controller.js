router.controller('adminOptionsVerifiedController', function($state, $scope, $cookies, $http, AuthenticationService, AdminService){
	
	$scope.verified = {};
	
	$scope.usernamesAndIds = [];
	
	$scope.pageCounter = 1;
	
		
	AdminService.getVerified($scope.token, 1, 10)
				.then(function successCallback(response){
					$scope.verified = angular.copy(response.data);
			
					var i;
					for(i = 0; i < $scope.verified.length; i ++){
						$scope.usernamesAndIds.push( { id: $scope.unverified[i].userId, text: $scope.unverified[i].username } );
					}
			
				}, function errorCallback(response){
					
			
				});
		
	

	$scope.getVerified = function(form, to){
		AdminService.getVerified($scope.token, form, to)
							.then(function(response){
								console.log(respone);
							}, function(response){
								console.log(response);
							});
	};
	
	
	$scope.nextPage = function(){
		$scope.pageCounter++;
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		
		AdminService.getVerified($scope.token, from, to)
					.then(function successCallback(response){
						$scope.verified = angular.copy(response.data);
					}, function errorCallback(response){
			
			
					});
		
	
		
	};
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
			
			AdminService.getVerified($scope.token, from, to)
						.then(function successCallback(response){
							$scope.verified = angular.copy(response.data);
						}, function errorCallback(response){
								
						});
		}
		
	};
	
	
	
	$scope.showUser = function(username){
		$state.go('main.userpreview');
		
	};
});