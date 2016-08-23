router.controller('userMessagesAllController', function($scope, $state){
	$scope.messages = [];
	$scope.pageCounter = 1;
	$scope.checkbox = [];
	$scope.messages = [
	                 {from:"manoulesi", subject:"ta ntoui", date:"date", checked:false},
	                 {from:"tonytonyp", subject:"ksexasthka re", date:"nteito", checked: false},
	                 {from:"chris-to", subject:"ponaei to kefali mou", date:"xtes", checked: false}
	                 ];
	
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
});