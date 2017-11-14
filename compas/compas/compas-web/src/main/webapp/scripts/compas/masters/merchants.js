/**
 * merchant Angular Module
 */
'use strict';
angular.module('app.merchant', []).controller("merchantsCtrl", ["$scope", "$filter", "merchantSvc", "$merchantValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, merchantSvc, $merchantValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.header="";
    $scope.loadMerchantData = function () {
        $scope.merchants = [], $scope.searchKeywords = "", $scope.filteredMerchants = [], $scope.row = "", $scope.merchantEditMode = false;
        merchantSvc.GetMerchants().success(function (response) {
            return $scope.merchants = response, $scope.searchKeywords = "", $scope.filteredMerchants = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMerchants = $scope.filteredMerchants.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredMerchants = $filter("filter")($scope.merchants, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMerchants = $filter("orderBy")($scope.merchants, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMerchants = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadMerchantData();

    $scope.editMerchant = function (merchant) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify  merchant.");
            return false;
        }
        $scope.header="Edit Merchant";
        $scope.merchantEditMode = true;
        $scope.merchantId = merchant.merchantId;
        $scope.merchantName = merchant.merchantName;
        $scope.merchantCode = merchant.merchantCode;
        $scope.active = merchant.active;
        $scope.isDisabled=true;
    };

    $scope.addMerchant = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new merchant.");
            return false;
        }
        $scope.header="Create Merchant";
        $scope.merchantEditMode = true;
        $scope.merchantId = 0;
        $scope.merchantName = "";
        $scope.merchantCode = "";
        $scope.active =false;
        $scope.isDisabled=false;
    };

    $scope.calcelMerchant = function () {
        $scope.merchantEditMode = false;
        $scope.merchantId = 0;
        $scope.merchantName = "";
        $scope.merchantCode = "";
        $scope.isDisabled=false;
    }

    $scope.updMerchant = function () {
        var merchant = {};
        if (!$merchantValid($scope.merchantCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Merchant's Code. Please try again.")
            return false;
        }
        if (!$merchantValid($scope.merchantName)) {
            logger.logWarning("Opss! You may have skipped specifying the Merchant's Name. Please try again.")
            return false;
        }
        if ($scope.merchantName.length > 50) {
            logger.logWarning("Opss!merchant Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$merchantValid($scope.merchantId))
            merchant.merchantId = 0;
        else
            merchant.merchantId = $scope.merchantId;
        merchant.merchantCode = $scope.merchantCode;
        merchant.merchantName = $scope.merchantName;
        merchant.active = $scope.active;
        merchant.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        console.log(merchant);
        merchantSvc.UpdMerchant(merchant).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Merchant information was saved succesfully")
                $scope.merchantId = 0;
                $scope.merchantName = "";
                $scope.merchantCode="";
                $scope.merchantEditMode = false;
                $scope.isDisabled=false;
                $scope.loadMerchantData();
            }
            else if (response.respCode == 201) {
                logger.logWarning("Opss! The Merchant name specified already exists. Please try again")
            }
            else {
                logger.logWarning("Opss! Something went wrong while updating the Merchant. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('merchantSvc', function ($http) {
    var compasMerchantSvc = {};
    compasMerchantSvc.GetMerchants = function () {
        return $http({
            url: '/compas/rest/merchant/gtMerchants/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasMerchantSvc.UpdMerchant = function (merchant) {
        return $http({
            url: '/compas/rest/merchant/updMerchant',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data:merchant
        });
    };
    return compasMerchantSvc;
}).factory('$merchantValid', function () {
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