router.controller('userMessagesOpenController', function($state, $scope, $cookies,  MessageService, AuthenticationService){
	
	$scope.rate = false;
	$scope.cannotrateagain = false;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	$scope.setReply = function(){
	
		$scope.message.subject = "Re: " + $scope.selectedReceived.subject;
		$scope.message.to = $scope.selectedReceived.from;
	}
	
	$scope.back = function(){
		$state.go('main.profile.userMessages.received');
	}
	
	$scope.ratePositive = function(){
		
		AuthenticationService.voteSeller($scope.token, $scope.user.userId, 'UP',  $scope.selectedReceived.voteLinkDto)
							.then(function(response){
								$scope.rate = true;
								
							}, function(response){
								if(response.status == 409){
									$scope.cannotrateagain = true;
									$scope.rate = false;
								}
							});
	};
	
	$scope.rateNegative = function(){
		
		AuthenticationService.voteSeller($scope.token, $scope.user.userId,  'DOWN', $scope.selectedReceived.voteLinkDto)
							.then(function(response){
								$scope.rate = true;
								
							}, function(response){
								if(response.status == 409){
									$scope.cannotrateagain = true;
									$scope.rate = false;
								}
							});
	};
});