router.controller('userMessagesController', function($scope, $cookies,  $state, MessageService){
	$scope.sent = false;
	$scope.message = {};
	$scope.user = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	
	$scope.messagesReceived = [];
	$scope.messagesSent= [];
	$scope.selectedReceived = [];
	MessageService.getMessagesByType(token, $scope.user.userId, "RECEIVED")
					.then (function(response){
						console.log(response);
						$scope.messagesReceived = response.data;
						
					}, function(response){
						console.log(response);
					});
	MessageService.getMessagesByType(token, $scope.user.userId, "SENT")
					.then (function(response){
							console.log(response);
							$scope.messagesSent = response.data;
		
					}, function(response){
						console.log(response);
					});
	
	$scope.sendMessage = function(message){
		console.log("k");
		message.date = new Date();
		console.log(message);
		MessageService.sendMessage(token, $scope.user.userId, message)
						.then(function (response){
							console.log(response);
							$scope.sent = true;
							$scope.messages = [];
						}, function(response){
							console.log(response);
						})
		
	};
	
	
	$scope.open = function(message){
		console.log(message);
		$scope.selectedReceived = message;
		$state.go('main.profile.userMessages.open');
	}
	
});