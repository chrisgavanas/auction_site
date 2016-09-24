router.controller('userAuctionsStartController', function ($scope, $http, $state,$stateParams, $cookies, AuthenticationService, AuctionItemService) {
	$scope.data = {};
	$scope.invalid1 = false;
	$scope.invalid2 = false;
		
	$scope.start = function(){
		
		
		 
		var auctionItemId = $stateParams.id;
		$scope.data.endDate = $('#datetimepicker1').data('DateTimePicker').date()._d;
		if($scope.data.endDate < new Date())
			$scope.invalid1 = true;
		else{
			$scope.invalid1 = false;
			AuctionItemService.startAuction($scope.token, auctionItemId, $scope.data)
								.then(function(response){
									$scope.invalid2 = false;
									$state.go('main.profile.userAuctions');
								}, function(response){
									if(response.status == 409)
										$scope.invalid2 = true;
								});
		}
	};
	
	
	 $(function () {
         $('#datetimepicker1').datetimepicker();
        
     });
	 
	 
	 
	
});

