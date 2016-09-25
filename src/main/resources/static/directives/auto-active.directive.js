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
                            if(a.href == "unsafe:https://localhost:8080/#/profile/userAuctions" && path == '/profile/userAuctions/active'){
                            	
                            	angular.element(a).addClass('active');
                            }
                            if(a.href == "unsafe:https://localhost:8080/#/profile/userAuctions" && path == '/profile/userAuctions/pending'){
                            	
                            	angular.element(a).addClass('active');
                            }
                            if(a.href == "unsafe:https://localhost:8080/#/profile/userAuctions" && path == '/profile/userAuctions/sold'){
                            	angular.element(a).addClass('active');
                            }
                            
                            if(a.href == "unsafe:https://localhost:8080/#/profile/messages" && path == '/profile/messages/received'){
                            	angular.element(a).addClass('active');
                            }
                            if(a.href == "unsafe:https://localhost:8080/#/profile/messages" && path == '/profile/messages/sent'){
                            	angular.element(a).addClass('active');
                            }
                        });
                    }
                }

                setActive();

                scope.$on('$locationChangeSuccess', setActive);
            }
        }}]);