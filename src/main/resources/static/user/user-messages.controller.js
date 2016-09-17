router.controller('userMessagesController', function($scope, $cookies,  $state, MessageService){
	$scope.sent = false;
	$scope.message = {};
	$scope.$state = $state;
	$scope.notexists = false;
	$scope.unseen = $scope.$parent.unseenCounter;
	
	
	
	$scope.selectedReceived = [];
	$state.go('main.profile.userMessages.received');
	
	console.log($scope.$parent.messagesReceived);
	if($scope.$parent.contact != null){
		$scope.message.to = $scope.contact;
		$('#modalCompose').modal('show');
		$scope.$parent.contact = null;
	}
	
	
	
	
	$scope.sendMessage = function(message){
		console.log("k");
		message.date = new Date();
		message.from = $scope.user.username;
		console.log(message);
		MessageService.sendMessage($scope.token, $scope.user.userId, message)
						.then(function (response){
							console.log(response);
							$scope.sent = true;
							$scope.notexists = false;
							//$scope.messagesSent.splice(0, 0,message);
							
						}, function(response){
							if(response.status == 404)
								$scope.notexists = true;
						})
		
	};
	
	
	$scope.open = function(message){
		console.log(message);
		
		$scope.selectedReceived = message;
		if(!message.seen)
			MessageService.markMessageAsSeen($scope.token, $scope.user.userId, message.messageId)
						.then(function(response){
							console.log(response);
						}, function(response){
							console.log(response);
						});
		
		$state.go('main.profile.userMessages.open');
	}
	
});