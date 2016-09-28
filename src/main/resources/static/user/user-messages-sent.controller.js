router.controller('userMessagesSentController', function($scope, $state, MessageService){
	$scope.pageCounter = 1;
	$scope.checkbox = [];
	$scope.selectedSent = {};
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	$scope.selectAll = function(){
		if($scope.checked){
			var i;
			for (i = 0; i < $scope.messagesSent.length; i++){
				$scope.messagesSent[i].checked = true;
			}
			
		}else{
			var i;
			for (i = 0; i < $scope.messagesSent.length; i++){
				$scope.messagesSent[i].checked = false;
			}
		}
			
		
	}
	
$scope.deleteMsg = function(){
		
		var i;
		$scope.toDelete = [];
		for (i = 0; i < $scope.messagesSent.length; i++){
			if($scope.messagesSent[i].checked){
				$scope.toDelete.push($scope.messagesSent[i].messageId);
				
				$scope.messagesSent.splice(i,1);
				i = i - 1;
				console.log($scope.messagesSent.length);
			}
		}
		
		MessageService.deleteMessage($scope.token, $scope.user.userId, $scope.toDelete, 'SENT')
			.then(function(response){
				console.log(response);
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
	
	
	
	
	
});