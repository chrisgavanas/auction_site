router.directive('autoActive', ['$location', '$state', function ($location, $state) {
        return {
            restrict: 'A',
            scope: false,
            link: function (scope, element) {
                function setActive() {
                    var path = $location.path();
                    if (path) {
                        angular.forEach(element.find('a'), function (a) {
                        	
                            if (a.href.match('#' + path + '(?=\\?|$)')) {
                            
                            	
                                angular.element(a).addClass('active');
                            } else {
                            	
                                angular.element(a).removeClass('active');
                            }
                        });
                    }
                }

                setActive();

                scope.$on('$locationChangeSuccess', setActive);
            }
        }}]);