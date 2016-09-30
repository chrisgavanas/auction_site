router.controller('userAuctionsStartController', function ($scope, $http, $state,$stateParams, $cookies, AuthenticationService, AuctionItemService) {
	$scope.data = {};
	$scope.invalid1 = false;
	$scope.invalid2 = false;
	var auctionItemId = $stateParams.id;
	
	if($scope.signedIn == false)
		$state.go('main.signedout');
	
	
	AuctionItemService.getAuctionItemById($scope.token, auctionItemId)
		.then(function(response){
			$scope.item = response.data;
			if($scope.item.userId != $scope.user.userId)
				$state.go('main.forbidden');
			
		}, function(response){
			if(response.status == 404)
				$state.go('main.notfound');
		
		});
	$scope.start = function(){
		
		
		 
		
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
									if(response.status == 404)
										$state.go('main.notfound');
								});
		}
	};
	
	
	 $(function () {
         $('#datetimepicker1').datetimepicker();
        
     });
	 
	 
	 
	
});

