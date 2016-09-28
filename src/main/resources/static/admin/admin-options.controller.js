router.controller('adminOptionsController', function($timeout,$window, $state, $scope, $cookies, $http, AuthenticationService, AdminService){
	$scope.xmldata = null;
	$scope.buttonClicked = false;

	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	$scope.verified = function(){
		$state.go('main.verified');
	};
	
	$scope.unverified = function(){
		$state.go('main.unverified');
	};
	
	$scope.prepareXML = function(){
		$scope.buttonClicked = true;
		$scope.wait = true;
		var promise = AdminService.exportToXML($scope.token);

								promise.then(function(response){
									$timeout(function () {
										console.log(response);
										$scope.xmldata = response.data;
										blob = new Blob([$scope.xmldata], { type: 'text/plain' }),
										url = $window.URL || $window.webkitURL;
										$scope.fileUrl = url.createObjectURL(blob);
									});
							    
								}, function(response){
									console.log(response);
								}, function (evt) {
									console.log(evt);
					                $scope.progress = Math.min(100, parseInt(100.0 * 
	                                         evt.loaded / evt.total));
								});
	};
});