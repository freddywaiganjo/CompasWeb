/**
 * organization Angular Module
 */
'use strict';
angular.module('app.organization', []).controller("organizationsCtrl", ["$scope", "$filter", "organizationSvc", "$organizationValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, organizationSvc, $organizationValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.loadOrganizationData = function () {
        $scope.organizations = [], $scope.searchKeywords = "", $scope.filteredOrganizations = [], $scope.row = "", $scope.organizationEditMode = false;
        organizationSvc.GetOrganizations().success(function (response) {
            return $scope.organizations = response, $scope.searchKeywords = "", $scope.filteredOrganizations = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageOrganizations = $scope.filteredOrganizations.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredOrganizations = $filter("filter")($scope.organizations, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredOrganizations = $filter("orderBy")($scope.organizations, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageOrganizations = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadOrganizationData();

    $scope.editOrganization = function (organization) {
    	 if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
             logger.log("Oh snap! You are not allowed to edit organization.");
             return false;
         }
        $scope.organizationEditMode = true;
        $scope.organizationId = organization.organizationId;
        $scope.organizationName = organization.organizationName;
    };

    $scope.addOrganization = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new organization.");
            return false;
        }
        $scope.organizationEditMode = true;
        $scope.organizationId = 0;
        $scope.organizationName = "";
    };

    $scope.calcelOrganization = function () {
        $scope.organizationEditMode = false;
        $scope.organizationId = 0;
        $scope.organizationName = "";
    }

    $scope.updOrganization = function () {
        var organization = [];
        if (!$organizationValid($scope.organizationName)) {
            logger.logWarning("Opss! You may have skipped specifying the Organization's Name. Please try again.")
            return false;
        }
        if ($scope.organizationName.length > 50) {
            logger.logWarning("Opss!Organization Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$organizationValid($scope.organizationId))
            organization.organizationId = 0;
        else
            organization.organizationId = $scope.organizationId;
        organization.organizationName = $scope.organizationName;
        organization.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        organizationSvc.UpdOrganization(organization).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Organization information was saved succesfully")
                $scope.organizationId = 0;
                $scope.organizationName = "";
                $scope.organizationEditMode = false;
                $scope.loadOrganizationData();
            }
            else if (response.respCode == 201) {
                logger.logWarning("Opss! The Organization name specified already exists. Please try again")
            }
            else {
                logger.logWarning("Opss! Something went wrong while updating the Organization. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('organizationSvc', function ($http) {
    var compasOrganizationSvc = {};
    compasOrganizationSvc.GetOrganizations = function () {
        return $http({
            url: '/compas/rest/organization/gtOrganizations',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasOrganizationSvc.UpdOrganization = function (organization) {
        return $http({
            url: '/compas/rest/organization/updOrganization',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: {
                'organizationId': organization.organizationId,
                'organizationName': organization.organizationName,
                'createdBy': organization.createdBy
            }
        });
    };
    return compasOrganizationSvc;
}).factory('$organizationValid', function () {
    return function (valData) {
        if (angular.isUndefined(valData))
            return false;
        else {
            if (valData == null)
                return false;
            else {
                if (valData.toString().trim() == "")
                    return false;
                else
                    return true;
            }
        }
        return false;
    };
});