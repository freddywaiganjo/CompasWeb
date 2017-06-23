/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.voucher', []).controller("vouchersCtrl", ["$scope", "$filter", "voucherSvc","$voucherValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,voucherSvc,$voucherValid, $rootScope, blockUI, logger, $location) {

	var init;

	$scope.vouchers = [];
	$scope.allItemsSelected = false;
	$scope.showServices=true;
	$scope.selection=[];
	$scope.services = [];
	$scope.voucherType="";
	$scope.header="";
	$scope.isExisting=false;
	$scope.mem=[];
	$scope.loadVoucherData = function () {
		$scope.vouchers = [], $scope.searchKeywords = "", $scope.filteredVouchers = [], $scope.row = "", $scope.voucherEditMode = false; $scope.previewServices=false;
		voucherSvc.GetVouchers($scope).success(function (response) {
			return $scope.vouchers = response, $scope.searchKeywords = "", $scope.filteredVouchers = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageVouchers = $scope.filteredVouchers.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredVouchers = $filter("filter")($scope.vouchers, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredVouchers = $filter("orderBy")($scope.vouchers, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageVouchers = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadVoucherData();


	$scope.preview = function () {
		if (!$voucherValid($scope.basketSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Basket. Please try again.")
			return false;
		}
		$scope.previewServices=true;
	}
	$scope.editVoucher = function (voucher) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the voucher.");
			return false;
		}
		$scope.header="Edit Voucher";
		// cope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
		$scope.voucherEditMode = true;
		$scope.isExisting = true;
		$scope.showServices=false;
		$scope.voucherId = voucher.voucherId;
		$scope.voucherCode = voucher.voucherCode;
		$scope.voucherDesc = voucher.voucherDesc;
		$scope.voucherValue=voucher.voucherValue;
		$scope.active = voucher.active;

		$scope.voucherType=voucher.voucherType;
		$scope.mem.startDate=$filter('date')(new Date(voucher.startDate),'MM-dd-yyyy');
		$scope.mem.endDate=$filter('date')(new Date(voucher.endDate),'MM-dd-yyyy');

		if(voucher.voucherType=="CM"){
			$scope.typeSelect=1;

		}
		if(voucher.voucherType=="CA"){
			$scope.typeSelect=2;

		}
		if(voucher.voucherType=="VA"){
			$scope.typeSelect=3;
		}
		//$scope.loadServiceData();
		$scope.services=voucher.services;
	};
	$scope.$watch("typeSelect", function (newValue, oldValue) {
		//$scope.services=[];
		if ($voucherValid(newValue)) {
			if (newValue != oldValue) {
				if($scope.voucherId==0){
					$scope.showServices=false;
					$scope.loadServiceData();
				}else {

				}
			}
		}
		else
			$scope.services = [];
	});
	$scope.loadServiceData = function () {

		if($scope.typeSelect==1 || $scope.typeSelect==3){
			$scope.voucherType="CM";
		}if($scope.typeSelect==2){
			$scope.voucherType="CA";	
		}
		voucherSvc.GetServiceProducts($scope.voucherType).success(function (response) {
			return $scope.services = response;
		});
	}
	// $scope.loadServiceData();

	$scope.voucherTypes=[,{typeId:1,typeName:"Comodity"},
	                     {typeId:2,typeName:"Cash"},
	                     {typeId:3,typeName:"Value"}]



	$scope.addVoucher = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create voucher.");
			return false;
		}
		$scope.header="Create Voucher";
		$scope.voucherEditMode = true;
		$scope.isExisting = false;
		$scope.voucherId = 0;
		$scope.voucherCode = "";
		$scope.voucherDesc = "";
		$scope.active = false;
		$scope.previewServices=false;
		$scope.typeSelect="";

		$scope.mem=[];
	};

	$scope.cancelVoucher = function () {
		$scope.voucherEditMode = false;
		$scope.previewServices=false;
		$scope.active = false;
		$scope.basketSelect="";
		$scope.isExisting = false;
		$scope.projectSelect="";
		$scope.services=[];
		$scope.basketValue="";
		$scope.showServices=true;
		$scope.typeSelect="";
		$scope.mem=[];
		$scope.voucherId = 0;
		$scope.voucherCode = "";
		$scope.voucherDesc = "";

	}
	$scope.selectService = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection=[];
		for (var i = 0; i < $scope.services.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.services[i].isActive) {
				$scope.selection.push(
						{"serviceId": $scope.services[i].serviceId
						});


			}
			else if(!$scope.services[i].isActive){

				$scope.selection.splice(i, 1);

			}

			if (!$scope.services[i].isActive) {
				$scope.allItemsSelected = false;
				return;
			}
		}

		// If not the check the "allItemsSelected" checkbox
		$scope.allItemsSelected = true;
	};

	// This executes when checkbox in table header is checked
	$scope.selectAllServices = function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection=[];
		for (var i = 0; i < $scope.services.length; i++) {
			$scope.services[i].isActive = $scope.allItemsSelected;
			if ($scope.services[i].isActive) {
				$scope.selection.push(
						{"serviceId": $scope.services[i].serviceId
						});


			}
			else if(!$scope.services[i].isActive){

				$scope.selection.splice(i, 1);

			}

		}
	};

	$scope.updVoucher= function () {
		var voucher = {};
		$scope.tempServices=[];
		var vValue=0.00;
		if (!$voucherValid($scope.voucherCode)) {
			logger.logWarning("Opss! You may have skipped specifying the Voucher's Code. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.voucherDesc)) {
			logger.logWarning("Opss! You may have skipped specifying the Voucher's Description. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.mem.startDate)) {
			logger.logWarning("Opss! You may have skipped specifying the Voucher's Start Date. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.mem.endDate)) {
			logger.logWarning("Opss! You may have skipped specifying the Voucher's End Date. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.typeSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Voucher's Type. Please try again.")
			return false;
		}
		for(var i=0;i<$scope.services.length;i++){
			if($scope.services[i].isActive==true){
				$scope.tempServices.push($scope.services[i]);
			}
		}
		if ($scope.tempServices.length<=0) {
			logger.logWarning("Opss! You may have skipped specifying the Items for voucher. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.voucherId))
			voucher.voucherId = 0;
		else
			voucher.voucherId = $scope.voucherId;
		voucher.voucherCode = $scope.voucherCode;
		voucher.voucherDesc=$scope.voucherDesc;
		voucher.active = $scope.active;
		voucher.createdBy = $rootScope.UsrRghts.sessionId;
		voucher.services=$scope.services;
		voucher.voucherValue=vValue;
		voucher.startDate= $filter('date')($scope.mem.startDate,'MM-dd-yyyy');
		voucher.endDate= $filter('date')($scope.mem.endDate,'MM-dd-yyyy');

	    if($scope.typeSelect==1){
        	voucher.voucherType="CM";
        }else if($scope.typeSelect==2){
        	voucher.voucherType="CA";
        }else if($scope.typeSelect==3){
        	if(!$voucherValid($scope.voucherValue)){
				logger.logWarning("Opss! You may have skipped specifying the voucher's value. Please try again.")
				return false;
			}
        	voucher.voucherType="VA";
        }
        
        for(var i=0;i<$scope.services.length;i++){
        	if($scope.services[i].serviceValue>0){
        		vValue+=$scope.services[i].serviceValue;
        	}
        }
        if($scope.typeSelect==3){
        	 voucher.voucherValue=$scope.voucherValue;
        }
        else{
        	voucher.voucherValue=vValue;
        }
		blockUI.start()
		voucherSvc.UpdVoucher(voucher).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The voucher information was saved succesfully")
				$scope.voucherId = 0;
				$scope.voucherCode = "";
				$scope.voucherDesc = "";
				$scope.active = false;
				$scope.projectId=0;
				$scope.basketId=0;
				$scope.voucherEditMode = false;
				$scope.projectSelect="";
				$scope.services=[];
				$scope.basketValue="";
				$scope.showServices=true;
				$scope.loadVoucherData();
				$scope.mem=[];
				$scope.typeSelect="";
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

} ]).factory('voucherSvc', function ($http) {
	var EvVoucherSvc = {};
	EvVoucherSvc.GetVouchers = function ($scope) {
		return $http({
			url: '/compas/rest/voucher/gtVouchers/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
//	/
	EvVoucherSvc.GetServiceProducts = function (voucherType) {
		return $http({
			url: '/compas/rest/voucher/gtServiceProducts/'+voucherType,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	EvVoucherSvc.UpdVoucher = function (voucher) {
		return $http({
			url: '/compas/rest/voucher/updVoucher',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: voucher
		});
	};

	

	return EvVoucherSvc;
}).factory('$voucherValid', function () {
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