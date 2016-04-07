var registerController = router.controller('registerController', function($scope, $http) {
    $scope.user = {};
    $scope.register = function() {
        $http.post('/api/register', $scope.user).success((res) => {
            alert(res.error || "Success!");
        }).error(function(data, response) {
            alert(data.message);
        });
    };
});
