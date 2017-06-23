/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.bnfgrp', []).controller("bnfgrpsCtrl", ["$scope", "$filter", "bnfgrpSvc","userSvc","merchantSvc","productSvc","$bnfgrpValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,bnfgrpSvc,userSvc,merchantSvc,productSvc,$bnfgrpValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.bnfgrps = [];
	$scope.allItemsSelected = false;
	$scope.selection=[];
	$scope.header="";
	 
	
	   
	
	 $scope.loadProductData = function () {
	        $scope.products = []
	        productSvc.GetProducts($rootScope.UsrRghts.linkId).success(function (response) {
	            return $scope.products = response
	        });
	  }
	$scope.loadProductData();
	 $scope.loadAgeGroupData = function () {
	        $scope.ageGroups = []
	        bnfgrpSvc.GetAgeGroups().success(function (response) {
	            return $scope.ageGroups = response
	        });
	  }
	$scope.loadAgeGroupData();
    $scope.loadBnfgrpData = function () {
        $scope.bnfgrps = [], $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "", $scope.bnfgrpEditMode = false; $scope.previewServices=false;
        bnfgrpSvc.GetBnfgrps($rootScope.UsrRghts.linkId).success(function (response) {
            return $scope.bnfgrps = response, $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageBnfgrps = $scope.filteredBnfgrps.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredBnfgrps = $filter("filter")($scope.bnfgrps, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredBnfgrps = $filter("orderBy")($scope.bnfgrps, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageBnfgrps = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }

    $scope.loadBnfgrpData();
   
  
   
    
    $scope.editBnfgrp = function (bnfgrp) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Vulnerability Criteria.");
    		return false;
    	}
    	$scope.header="Edit Vulnerability Criteria";
        $scope.bnfgrpEditMode = true;
        $scope.isExisting = true;
        $scope.bnfGrpId = bnfgrp.bnfGrpId;
        $scope.bnfGrpCode = bnfgrp.bnfGrpCode;
        $scope.bnfGrpName = bnfgrp.bnfGrpName;
        $scope.active = bnfgrp.active;
        $scope.productSelect=bnfgrp.productId;
        $scope.houseHoldValue=bnfgrp.houseHoldValue;
        $scope.minCap=bnfgrp.minCap;
        $scope.maxCap=bnfgrp.maxCap;
        $scope.ageGroups=bnfgrp.ageGroups;
    };
  
    $scope.addBnfgrp = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create Vulnerability Criteria.");
    		return false;
    	}
    	$scope.header="Create Vulnerability Criteria";
    	 // $scope.loadBranchData();
        $scope.bnfgrpEditMode = true;
        $scope.isExisting = false;
        $scope.bnfGrpId = 0;
        $scope.bnfGrpCode = "";
        $scope.bnfGrpName = "";
        $scope.active = false;
        $scope.previewServices=false;
        $scope.isDisabled=true;
        $scope.productSelect="";
        $scope.houseHoldValue="";
        $scope.minCap="";
        $scope.maxCap="";
      
    };

    $scope.cancelBnfgrp = function () {
        $scope.bnfgrpEditMode = false;
        $scope.previewServices=false;
        $scope.active = false;
        $scope.basketSelect="";
        $scope.productSelect="";
        $scope.programmes=[];
        $scope.isDisabled=false;
        $scope.houseHoldValue="";
        $scope.minCap="";
        $scope.maxCap="";
       
    }
    $scope.selectAgeGroup = function (index) {
        // If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
    	$scope.selection=[];
        for (var i = 0; i < $scope.ageGroups.length; i++) {
        	// var idx = $scope.selection.indexOf(productId);
        
        	   if (index > -1 && $scope.ageGroups[i].isActive) {
        		   $scope.selection.push(
     	        		  {"ageGrpId": $scope.ageGroups[i].ageGrpId
     	        			  });
        		  

        	   }
        	  else if(!$scope.ageGroups[i].isActive){
        	   
        		  $scope.selection.splice(i, 1);
        	   
        	  }

            if (!$scope.ageGroups[i].isActive) {
                $scope.allItemsSelected = false;
                return;
            }
        }

        // If not the check the "allItemsSelected" checkbox
        $scope.allItemsSelected = true;
    };

    // This executes when checkbox in table header is checked
    $scope.selectAllAgeGroups = function () {
    	// Loop through all the entities and set their isChecked property
    	$scope.selection=[];
    	for (var i = 0; i < $scope.ageGroups.length; i++) {
    		$scope.ageGroups[i].isActive = $scope.allItemsSelected;
    		if ($scope.ageGroups[i].isActive) {
    			$scope.selection.push(
    					{"ageGrpId": $scope.ageGroups[i].ageGrpId
    					});


    		}
    		else if(!$scope.ageGroups[i].isActive){

    			$scope.selection.splice(i, 1);

    		}

    	}
    };

    $scope.updBnfgrp= function () {
        var bnfgrp = {};

        if (!$bnfgrpValid($scope.bnfGrpCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Vulnerability Criteria's Code. Please try again.")
            return false;
        }
        if (!$bnfgrpValid($scope.bnfGrpName)) {
            logger.logWarning("Opss! You may have skipped specifying the Vulnerability Criteria's Description. Please try again.")
            return false;
        }
        if (!$bnfgrpValid($scope.productSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Product. Please try again.")
            return false;
        }
       /* if (!$bnfgrpValid($scope.houseHoldValue)) {
            logger.logWarning("Opss! You may have skipped specifying the beneficiary group's househol value. Please try again.")
            return false;
        }*/
        if (!$bnfgrpValid($scope.bnfGrpId))
            bnfgrp.bnfGrpId = 0;
        else
        	bnfgrp.bnfGrpId = $scope.bnfGrpId;
        bnfgrp.bnfGrpCode=$scope.bnfGrpCode;
        bnfgrp.bnfGrpName=$scope.bnfGrpName;
        bnfgrp.productId=$scope.productSelect;
        bnfgrp.active = $scope.active;
        bnfgrp.houseHoldValue= 0;
        bnfgrp.createdBy = $rootScope.UsrRghts.sessionId;
        bnfgrp.minCap=0;
        bnfgrp.maxCap=0;
        bnfgrp.ageGroups=$scope.ageGroups
      // bnfgrp.programmes=$scope.programmes;
        blockUI.start()
        bnfgrpSvc.UpdBnfgrp(bnfgrp).success(function (response) {
            if (response.respCode == 200) {
            	logger.logSuccess("Great! The Vulnerability Criteria information was saved succesfully")
            	$scope.bnfGrpId = 0;
                $scope.bnfGrpCode = "";
                $scope.bnfGrpName = "";
                $scope.active = false;
                $scope.productSelect=0;
               
                $scope.bnfgrpEditMode = false;
                $scope.loadBnfgrpData();
                $scope.minCap="";
                $scope.maxCap="";
                $scope.houseHoldValue="";
            }
            else  {
                logger.logWarning(response.respMessage);
            }
           
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

} ]).factory('bnfgrpSvc', function ($http) {
    var EvBnfgrpsvc = {};
    EvBnfgrpsvc.GetBnfgrps = function (orgId) {
        return $http({
            url: '/compas/rest/product/gtBnfGrps',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    EvBnfgrpsvc.GetAgeGroups = function () {
        return $http({
            url: '/compas/rest/product/gtAgeGroups',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvBnfgrpsvc.UpdBnfgrp = function (bnfgrp) {
        return $http({
            url: '/compas/rest/product/updBnfGrp',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: bnfgrp
        });
    };
    return EvBnfgrpsvc;
}).factory('$bnfgrpValid', function () {
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