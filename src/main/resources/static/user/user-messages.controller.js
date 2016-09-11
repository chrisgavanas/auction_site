router.controller('userMessagesController', function($scope, $cookies,  $state, MessageService){
	$scope.sent = false;
	$scope.message = {};
	$scope.$state = $state;
	$scope.notexists = false;
	$scope.unseenCounter = 0;
	var token = $cookies.get('authToken');
	$scope.token = token;
	
	
	$scope.selectedReceived = [];
	$state.go('main.profile.userMessages.received');
	
	
	
	
	
	
	
	$scope.sendMessage = function(message){
		console.log("k");
		message.date = new Date();
		message.from = $scope.user.username;
		console.log(message);
		MessageService.sendMessage(token, $scope.user.userId, message)
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
			MessageService.markMessageAsSeen(token, $scope.user.userId, message.messageId)
						.then(function(response){
							console.log(response);
						}, function(response){
							console.log(response);
						});
		
		$state.go('main.profile.userMessages.open');
	}
	
});