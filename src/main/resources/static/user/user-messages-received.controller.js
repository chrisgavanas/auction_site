router.controller('userMessagesReceivedController', function($scope, $state, $cookies, MessageService){
	
	$scope.pageCounter = 1;
	$scope.checkbox = [];
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
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
		
		var i;
		$scope.toDelete = [];
		for (i = 0; i < $scope.messagesReceived.length; i++){
			if($scope.messagesReceived[i].checked){
				$scope.toDelete.push($scope.messagesReceived[i].messageId);
				
				$scope.messagesReceived.splice(i,1);
				i = i - 1;
			
			}
		}
		
		MessageService.deleteMessage($scope.token, $scope.user.userId, $scope.toDelete, 'RECEIVED')
			.then(function(response){
				
			}, function(response){
				console.log(response);
			});
		
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
		$scope.message.subject = "Re: " + $scope.selectedReceived.subject;
		$scope.message.username = $scope.selectedReceived.username;
	}
	
});