var loginController = router.controller('loginController', function($scope, $http) {
    $scope.user = {};
    $scope.login = function() {
        $http.post('/api/login', $scope.user).success((res) => {
            alert(res.error || "Success!");
        }).error(function(data, response) {
            if (response == 400) {
                $scope.user.username = ""
                alert(data.message);
            }
        });
    };
});
