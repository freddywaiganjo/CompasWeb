/**
 * product Angular Module
 */
'use strict';
angular.module('app.product', []).controller("productsCtrl", ["$scope", "$filter", "productSvc","merchantSvc", "$productValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, productSvc,merchantSvc, $productValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.header="";
    $scope.loadProductData = function () {
        $scope.products = [], $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "", $scope.productEditMode = false;
        productSvc.GetProducts().success(function (response) {
            return $scope.products = response, $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageProducts = $scope.filteredProducts.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredProducts = $filter("filter")($scope.products, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredProducts = $filter("orderBy")($scope.products, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageProducts = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadProductData();
    $scope.loadMerchantData = function () {
        $scope.merchants = []
        merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
            return $scope.merchants = response
            })
    }
    $scope.loadMerchantData()
    $scope.editProduct = function (product) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify  product.");
            return false;
        }
        $scope.header="Edit Product";
        $scope.productEditMode = true;
        $scope.productId = product.productId;
        $scope.productCode = product.productCode;
        $scope.productName = product.productName;
        $scope.merchantSelect = product.merchantId;
        $scope.active = product.active;
        $scope.isExisting = true;
    };

    $scope.addProduct = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new product.");
            return false;
        }
        $scope.header="Create Product";
        $scope.productEditMode = true;
        $scope.productId = 0;
        $scope.productCode="";
        $scope.productName = "";
        $scope.merchantSelect="";
        $scope.active =false;
        $scope.isExisting = false;
    };

    $scope.calcelProduct = function () {
        $scope.productEditMode = false;
        $scope.productId = 0;
        $scope.productCode="";
        $scope.merchantSelect="";
        $scope.productName = "";
        $scope.isExisting = false;
    }

    $scope.updProduct = function () {
        var product = {};
        if (!$productValid($scope.productCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Product's Code. Please try again.")
            return false;
        }
        if (!$productValid($scope.productName)) {
            logger.logWarning("Opss! You may have skipped specifying the Product's Name. Please try again.")
            return false;
        }
        if ($scope.productName.length > 50) {
            logger.logWarning("Opss!product Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$productValid($scope.merchantSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Merchant. Please try again.")
            return false;
        }
        if (!$productValid($scope.productId))
            product.productId = 0;
        else
            product.productId = $scope.productId;
        product.productCode = $scope.productCode;
        product.productName = $scope.productName;
        product.merchantId =  $scope.merchantSelect;
        product.active = $scope.active;
        product.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        console.log(product);
        productSvc.UpdProduct(product).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Product information was saved succesfully")
                $scope.productId = 0;
                $scope.productName = "";
                $scope.productCode="";
                $scope.merchantSelect="";
                $scope.productEditMode = false;
                $scope.isExisting = false;
                $scope.loadProductData();
            }
            else if (response.respCode == 201) {
                logger.logWarning("Opss! The Product name specified already exists. Please try again")
            }
            else {
                logger.logWarning("Opss! Something went wrong while updating the Product. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('productSvc', function ($http) {
    var compasProductSvc = {};
    compasProductSvc.GetProducts = function () {
        return $http({
            url: '/compas/rest/product/gtProducts/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasProductSvc.UpdProduct = function (product) {
        return $http({
            url: '/compas/rest/product/updProduct',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data:product
        });
    };
    return compasProductSvc;
}).factory('$productValid', function () {
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