router.controller('userMessagesOpenController', function($state, $scope, $cookies,  MessageService){
	
	
	$scope.setReply = function(){
		console.log($scope.selectedReceived);
		$scope.message.subject = "Re: " + $scope.selectedReceived.subject;
		$scope.message.to = $scope.selectedReceived.from;
	}
	
	$scope.back = function(){
		$state.go('main.profile.userMessages.received');
	}
});