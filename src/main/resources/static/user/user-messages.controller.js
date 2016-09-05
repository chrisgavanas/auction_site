router.controller('userMessagesController', function($scope, $cookies,  MessageService){
	$scope.sent = false;
	$scope.message = {};
	$scope.user = {};
	$scope.user.userId = $cookies.get('userId');
	var token = $cookies.get('authToken');
	
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
	
	
	
});