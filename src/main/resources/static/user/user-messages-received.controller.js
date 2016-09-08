router.controller('userMessagesReceivedController', function($scope, $state, $cookies, MessageService){
	
	$scope.pageCounter = 1;
	$scope.checkbox = [];
	$scope.selectedReceived = {};
	
	$scope.selectAll = function(){
		if($scope.checked){
			var i;
			for (i = 0; i < $scope.messages.length; i++){
				$scope.messages[i].checked = true;
			}
			
		}else{
			var i;
			for (i = 0; i < $scope.messages.length; i++){
				$scope.messages[i].checked = false;
			}
		}
			
		
	}
	
	$scope.deleteMsg = function(){
		var i;
		for (i = 0; i < $scope.messages.length; i++){
			if($scope.messages[i].checked){
				$scope.messages.splice(i,1);
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