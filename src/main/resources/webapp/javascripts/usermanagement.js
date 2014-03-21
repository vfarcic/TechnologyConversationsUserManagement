angular.module('userManagementModule', ['ui.bootstrap'])
    // TODO Display message on validation error
    .controller('usersListCtrl', function($scope, $http) {
        $http.get('/usermanagementapi/users/all.json').then(function(response) {
            $scope.users = response.data;
        }, function(response) {
            // TODO Output to modal
        });
        $scope.updateUser = function(userName) {
            $http.get('/usermanagementapi/users/user/' + userName + '.json').then(function(response) {
                $scope.user = response.data;
                console.log($scope.user);
            }, function(response) {
                // TODO Output to modal
            });
        };
        $scope.getCssClass = function(ngModelController) {
            return {
                'has-error': ngModelController.$invalid,
                'has-success': ngModelController.$valid && ngModelController.$dirty
            };
        };
        $scope.onlyLettersAndSpace = function() {
            return /^[a-zA-Z ]+$/;
        }
    });
