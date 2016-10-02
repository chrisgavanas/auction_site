router.controller('adminOptionsVerifiedController', function($state, $scope, $cookies, $http, AuthenticationService, AdminService){
	
	$scope.verified = {};
	$scope.usernamesAndIds = [];
	$scope.pageCounter = 1;
$scope.pageCounter = 1;
	
	$scope.totalUsers = 0;
	
	$scope.pageNumbers = 0;
	$scope.maxSize = 5;

	$scope.bigCurrentPage = 1;
	$scope.currentPage = 4;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
		
	AdminService.getVerified($scope.token, 1, 10)
				.then(function successCallback(response){
					$scope.verified = angular.copy(response.data);
					$scope.totalUsers = response.headers().totalusers;
					$scope.pageNumbers = Math.ceil($scope.totalUsers / 10);
					
					var i;
					for(i = 0; i < $scope.verified.length; i ++){
						$scope.usernamesAndIds.push( { id: $scope.verified[i].userId, text: $scope.verified[i].username } );
					}
			
				}, function errorCallback(response){
					$cookies.remove('userId');
					$cookies.remove('authToken');
					$cookies.put('signedIn', 'no');
					
					$scope.signedIn = false;
					$state.go('main.signedout');
			
				});
		
	
	
	
	
	$scope.showUser = function(username){
		$state.go('main.userpreview');
		
	};
	
	$scope.change = function (current){
		$scope.to = current * 10;
		$scope.from = $scope.to - 9;
		AdminService.getVerified($scope.token, $scope.from, $scope.to)
						.then(function successCallback(response){
							$scope.verified = angular.copy(response.data);
						}, function errorCallback(response){
							$cookies.remove('userId');
							$cookies.remove('authToken');
							$cookies.put('signedIn', 'no');
			
							$scope.signedIn = false;
							$state.go('main.signedout');

						});
	};
});