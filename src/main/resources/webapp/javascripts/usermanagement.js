angular.module('userManagementModule', ['ui.bootstrap'])
angular.module('userManagementModule', ['ui.bootstrap'])
    // TODO Display message on validation error
    .controller('usersListCtrl', function($scope, $http) {
        $scope.putStoryOperation = "Create";
        $scope.getUsers = function() {
            $http.get('/usermanagementapi/users/all.json').then(function(response) {
                console.log('Got users');
                $scope.users = response.data;
            }, function(response) {
                console.log(response);
                // TODO Output to modal
            });
        };
        $scope.getUsers();
        $scope.getUser = function(userName) {
            $http.get('/usermanagementapi/users/user/' + userName + '.json').then(function(response) {
                $scope.user = response.data;
                $scope.originalUser = angular.copy($scope.user);
                $scope.putStoryOperation = "Update";
                console.log('Got user ' + userName);
            }, function(response) {
                console.log(response);
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
        };
        $scope.onlyAlphanumeric = function() {
            return /^[a-zA-Z0-9]+$/;
        };
        $scope.showUpdated = function() {
            return $scope.putStoryOperation === "Update";
        };
        $scope.needsCapitalAndDigit = function() {
            return /^.*(?=.*?[A-Z])(?=.*?[0-9]).*$/;
        };
        $scope.getButtonCssClass = function() {
            return {
                'btn-success': $scope.userForm.$valid,
                'btn-danger': $scope.userForm.$invalid
            };
        };
        $scope.canSaveUser = function() {
            return $scope.userForm.$valid && !angular.equals($scope.user, $scope.originalUser);
        };
        $scope.putUser = function() {
            if ($scope.putStoryOperation === 'Update' && $scope.user.userName != $scope.originalUser.userName) {
                $http.delete('/usermanagementapi/users/user/' + $scope.originalUser.userName + '.json').then(function(response) {
                    updateUser($scope, $http);
                }, function(response) {
                    console.log(response);
                    // TODO Output to modal
                });
            } else {
                updateUser($scope, $http);
            }
        };
        $scope.canDeleteUser = function() {
            return $scope.putStoryOperation === "Update";
        };
        $scope.deleteUser = function() {
            $http.delete('/usermanagementapi/users/user/' + $scope.originalUser.userName + '.json').then(function(response) {
                $scope.getUsers();
                $scope.newUser();
            }, function(response) {
                console.log(response);
                // TODO Output to modal
            });
        };
        $scope.newUser = function() {
            $scope.user = null;
            $scope.putStoryOperation = "Create";
        };
    });

function newUser($scope) {
}

function updateUser($scope, $http) {
    $http.put('/usermanagementapi/users/user/' + $scope.user + '.json', $scope.user).then(function(response) {
        if (response.data.status === 'NOK') {
            console.log(response);
            // TODO Output to modal
        } else {
            $scope.users.push({userName: $scope.user.userName, fullName: $scope.user.fullName});
            $scope.putStoryOperation = "Update";
        }
    }, function(response) {
        console.log(response);
        // TODO Output to modal
    });
}