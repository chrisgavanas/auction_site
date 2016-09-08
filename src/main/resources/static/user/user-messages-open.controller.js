router.controller('userMessagesOpenController', function($scope, $cookies,  MessageService){
	$scope.setReply = function(){
		console.log($scope.selectedReceived);
		$scope.message.subject = "Re: " + $scope.selectedReceived.subject;
		$scope.message.username = $scope.selectedReceived.username;
	}
});