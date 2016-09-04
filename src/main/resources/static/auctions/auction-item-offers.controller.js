router.controller('offersController', function($state, $scope, $stateParams){
	$scope.bidds  = [{bidder:"rulabula", amount: 3, date: new Date()}, {bidder:"kwtsohlias", amount: 4, date: new Date()}];
	console.log($scope.bidds);
});