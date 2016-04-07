var loginController = router.controller('loginController', function($scope, $http) {
    $scope.user = {};
    $scope.login = function() {
        $http.post('/api/login', $scope.user).success((res) => {
            alert(res.error || "Success!");
        }).error(console.log.bind(console));
    };
});
