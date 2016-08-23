router.controller('userMessagesAllController', function($scope, $state){
	$scope.messages = [];
	
	
	$scope.messages = [
	                 {from:"manoulesi", subject:"ta ntoui", date:"date"},
	                 {from:"tonytonyp", subject:"ksexasthka re", date:"nteito"},
	                 {from:"chris-to", subject:"ponaei to kefali mou", date:"xtes"}
	                 ];
	
	$scope.selectAll = function(){
		if($scope.checked){
			$scope.checkboxes = true;
			
		}else
			$scope.checkboxes = false;
		
	}
	
});