router.controller('userMessagesReceivedController', function($scope, $state, $cookies, MessageService){
	
	$scope.pageCounter = 1;
	$scope.checkbox = [];
	
	
	$scope.selectAll = function(){
		if($scope.checked){
			var i;
			for (i = 0; i < $scope.messagesReceived.length; i++){
				$scope.messagesReceived[i].checked = true;
			}
			
		}else{
			var i;
			for (i = 0; i < $scope.messagesReceived.length; i++){
				$scope.messagesReceived[i].checked = false;
			}
		}
			
		
	}
	
	$scope.deleteMsg = function(){
		console.log($scope.messagesReceived);
		var i;
		for (i = 0; i < $scope.messagesReceived.length; i++){
			if($scope.messagesReceived[i].checked){
				console.log('deleting ');
				console.log($scope.messagesReceived[i]);
				MessageService.deleteMessage($scope.token, $scope.user.userId, $scope.messagesReceived[i].messageId, 'RECEIVED')
								.then(function(response){
									console.log(response);
								}, function(response){
									console.log(response);
								});
				$scope.messagesReceived.splice(i,1);
				i = i - 1;
			}
		}
	}
	
	$scope.nextPage = function (){
		$scope.pageCounter++;
		var to = $scope.pageCounter * 10;
		var from = to - 9;
		
	}
	
	$scope.previousPage = function(){
		$scope.pageCounter--;
		if($scope.pageCounter >= 1){
			var to = $scope.pageCounter * 10;
			var from = to - 9;
		}
	}
	
	$scope.setReply = function(){
		console.log($scope.selectedReceived);
		$scope.message.subject = "Re: " + $scope.selectedReceived.subject;
		$scope.message.username = $scope.selectedReceived.username;
	}
	
});