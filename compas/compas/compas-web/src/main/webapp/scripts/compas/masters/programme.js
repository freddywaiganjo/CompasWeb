/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.programme', []).controller("programmesCtrl", ["$scope", "$filter", "programmeSvc","voucherSvc","productSvc","$programmeValid", "$rootScope", "blockUI", "logger", "$location","$modal","$sce", function ($scope, $filter,programmeSvc,voucherSvc,productSvc,$programmeValid, $rootScope, blockUI, logger, $location,$modal,$sce) {

	var init;

	$scope.programmes = [];
	$scope.allItemsSelected = false;
	$scope.selection=[];
	$scope.frqMode="";
	$scope.tranMode="";
	$scope.itmModes=[];
	$scope.chtmModes=[];
	$scope.intmModes=[];
	$scope.mem=[];
	$scope.header="";
	$scope.loadProgrammeData = function () {
		$scope.programmes = [], $scope.searchKeywords = "", $scope.filteredProgrammes = [], $scope.row = "", $scope.programmeEditMode = false; $scope.previewServices=false;
		programmeSvc.GetProgrammes().success(function (response) {
			return $scope.programmes = response, $scope.searchKeywords = "", $scope.filteredProgrammes = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageProgrammes = $scope.filteredProgrammes.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredProgrammes = $filter("filter")($scope.programmes, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredProgrammes = $filter("orderBy")($scope.programmes, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageProgrammes = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadProgrammeData();
	$scope.loadVoucherData = function () {
		$scope.vouchers = []
		voucherSvc.GetVouchers($scope).success(function (response) {
			 $scope.vouchers = response
		})
	}
	$scope.loadVoucherData()
	$scope.loadCashVoucherData = function () {
		$scope.cvouchers = []
		programmeSvc.GetCashVouchers($scope).success(function (response) {
			 $scope.cvouchers = response
		})
	}
	$scope.loadCashVoucherData()
	$scope.loadChannelSetting=function(){
		$scope.frqMode=[{modeId:"C",mode:"Cyclic"},{modeId:"N",mode:"Non-Cyclic"}]
		$scope.frqTypes=[{frqId:"D",frqName:"Daily"},
		                 {frqId:"W",frqName:"Weekly"},
		                 {frqId:"M",frqName:"Monthly"},
		                 {frqId:"Q",frqName:"Quarterly"},
		                 {frqId:"Y",frqName:"Yearly"}]
		$scope.itms=[{itmId:"P",itmName:"Pin"},
		             {itmId:"F",itmName:"Fp"},
		             {itmId:"I",itmName:"Iris"},
		             {itmId:"V",itmName:"Voice"}]

		$scope.chns=[{chnId:"P",chnName:"Pos"},
		             {itmId:"W",chnName:"Web"},
		             {itmId:"M",chnName:"Mobile"},
		             ]

		$scope.intms=[{intmId:"S",intmName:"SMC"},
		              {intmId:"P",intmName:"PVC"},
		              {intmId:"N",intmName:"NFC"},
		              {intmId:"M",intmName:"MSR"},
		              {intmId:"B",intmName:"BIO"}]
	}

	$scope.loadChannelSetting();
	$scope.preview = function () {
		if (!$programmeValid($scope.basketSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Basket. Please try again.")
			return false;
		}
		$scope.previewServices=true;
	}

	$scope.loadProductData = function () {
		$scope.products = [], $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "", $scope.productEditMode = false;
		productSvc.GetProducts($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.products = response
		})
	}
	$scope.loadProductData ();

	$scope.editProgramme = function (programme) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the programme.");
			return false;
		}
		$scope.header="Edit Programme";
		$scope.programmeEditMode = true;
		$scope.isExisting = true;
		$scope.programmeId = programme.programmeId;
		$scope.programmeCode = programme.programmeCode;
		$scope.programmeDesc = programme.programmeDesc;
		$scope.active = programme.active;
		$scope.productSelect=programme.productId;
		$scope.mem.startDate=$filter('date')(new Date(programme.startDate),'MM-dd-yyyy');
		$scope.mem.endDate=$filter('date')(new Date(programme.startDate),'MM-dd-yyyy');
		// $scope.startDate=programme.startDate;
		//$scope.endDate=programme.endDate;
		$scope.iModes=programme.itmModes;
		$scope.cModes=programme.chtmModes;
		$scope.inModes=programme.intmModes;
		
//		if( $scope.vouchers.length>0){
//			$scope.tranMode="D"
//		}
		//itm modes
		var ivalues = $scope.iModes.split(",");
		for(var i=0;i<ivalues.length;i++){
			if(ivalues[i]=="P")
				$scope.itmModes.pinMode=true;
			if(ivalues[i]=="F")
				$scope.itmModes.fpMode=true;
			if(ivalues[i]=="I")
				$scope.itmModes.irisMode=true;
			if(ivalues[i]=="V")
				$scope.itmModes.voiceMode=true;

		}
		//chtm modes
		var cvalues = $scope.cModes.split(",");
		for(var i=0;i<cvalues.length;i++){
			if(cvalues[i]=="P")
				$scope.chtmModes.posMode=true;
			if(cvalues[i]=="W")
				$scope.chtmModes.webMode=true;
			if(cvalues[i]=="M")
				$scope.chtmModes.mbMode=true;
			if(cvalues[i]=="O")
				$scope.chtmModes.oemMode=true;

		}
		//intm modes
		var invalues = $scope.inModes.split(",");
		for(var i=0;i<invalues.length;i++){
			if(invalues[i]=="S")
				$scope.intmModes.smMode=true;
			if(invalues[i]=="P")
				$scope.intmModes.pcvMode=true;
			if(invalues[i]=="N")
				$scope.intmModes.nfcMode=true;
			if(invalues[i]=="M")
				$scope.intmModes.msrMode=true;

		}
		
		$scope.tranMode=programme.programmeType;
		if($scope.tranMode=='D'){
			$scope.vouchers=programme.vouchers;
		}else if($scope.tranMode=='CT'){
		$scope.cvouchers=programme.vouchers;
		}

	};
	$scope.getTemplate = function(){
		var values = $scope.itmModes.split(",");
		for(var i=0;i<values.lenght;i++){
			if(values[i]=="P")
				$scope.itmModes.pinMode=true;
		}
		$scope.templateid = values[0];
		$scope.userid = values[1];
	};
	$scope.addProgramme = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create programme.");
			return false;
		}
		$scope.header="Create Programme";
		$scope.programmeEditMode = true;
		$scope.isExisting = false;
		$scope.programmeId = 0;
		$scope.programmeCode = "";
		$scope.programmeDesc = "";
		$scope.active = false;
		$scope.previewServices=false;
		$scope.itmModes={pinMode:false,fpMode:false,irisMode:false,voiceMode:false}
		$scope.chtmModes={posMode:false,webMode:false,mbMode:false,oemMode:false}
		$scope.intmModes={smMode:false,pcvMode:false,nfcMode:false,msrMode:false,bioMode:false}
	//	$scope.loadVoucherData();
//		$scope.pinMode=false;
//		$scope.fpMode="";
//		$scope.irisMode="";
//		$scope.voiceMode="";
	};

	$scope.cancelProgramme = function () {
		$scope.programmeEditMode = false;
		$scope.previewServices=false;
		$scope.active = false;
		$scope.basketSelect="";
		$scope.productSelect="";
		$scope.services=[];
		$scope.basketValue="";
		$scope.mem=[];
		$scope.vouchers=[];
		$scope.cvouchers=[];
		$scope.tranMode="";
		$scope.loadChannelSetting();
		$scope.itmModes={pinMode:false,fpMode:false,irisMode:false,voiceMode:false}
		$scope.chtmModes={posMode:false,webMode:false,mbMode:false,oemMode:false}
		$scope.intmModes={smMode:false,pcvMode:false,nfcMode:false,msrMode:false,bioMode:false}
	}
	$scope.selectVoucher = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection=[];
		if($scope.tranMode=='D'){
		for (var i = 0; i < $scope.vouchers.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.vouchers[i].isActive) {
				$scope.selection.push(
						{"voucherId": $scope.vouchers[i].voucherId
						});


			}
			else if(!$scope.vouchers[i].isActive){

				$scope.selection.splice(i, 1);

			}

			if (!$scope.vouchers[i].isActive) {
				$scope.allItemsSelected = false;
				return;
			}
		}

		// If not the check the "allItemsSelected" checkbox
		$scope.allItemsSelected = true;
		}
		if($scope.tranMode=='ST'){
			for (var i = 0; i < $scope.cvouchers.length; i++) {
				// var idx = $scope.selection.indexOf(branchId);

				if (index > -1 && $scope.cvouchers[i].isActive) {
					$scope.selection.push(
							{"voucherId": $scope.cvouchers[i].voucherId
							});


				}
				else if(!$scope.cvouchers[i].isActive){

					$scope.selection.splice(i, 1);

				}

				if (!$scope.cvouchers[i].isActive) {
					$scope.allItemsSelected = false;
					return;
				}
			}

			// If not the check the "allItemsSelected" checkbox
			$scope.allItemsSelected = true;
		}
	};

	// This executes when checkbox in table header is checked
	$scope.selectAllVouchers = function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection=[];
		if($scope.tranMode=='D'){
		for (var i = 0; i < $scope.vouchers.length; i++) {
			$scope.vouchers[i].isActive = $scope.allItemsSelected;
			if ($scope.vouchers[i].isActive) {
				$scope.selection.push(
						{"voucherId": $scope.vouchers[i].voucherId
						});


			}
			else if(!$scope.vouchers[i].isActive){

				$scope.selection.splice(i, 1);

			}

		}
		}
		if($scope.tranMode=='ST'){
			for (var i = 0; i < $scope.cvouchers.length; i++) {
				$scope.cvouchers[i].isActive = $scope.allItemsSelected;
				if ($scope.cvouchers[i].isActive) {
					$scope.selection.push(
							{"voucherId": $scope.cvouchers[i].voucherId
							});


				}
				else if(!$scope.cvouchers[i].isActive){

					$scope.selection.splice(i, 1);

				}

			}
			}
	};


	$scope.updProgramme= function () {
		console.log($scope.vouchers)
		// console.log($scope.itmModeValue)
		var programme = {};
		$scope.itmList=[];
		$scope.chtmList=[];
		$scope.intmList=[];
		if (!$programmeValid($scope.programmeCode)) {
			logger.logWarning("Opss! You may have skipped specifying the Programme's Code. Please try again.")
			return false;
		}
		if (!$programmeValid($scope.programmeDesc)) {
			logger.logWarning("Opss! You may have skipped specifying the Programme's Description. Please try again.")
			return false;
		}
		if (!$programmeValid($scope.mem.startDate)) {
			logger.logWarning("Opss! You may have skipped specifying the Start Date Please try again.")
			return false;
		}
		if (!$programmeValid($scope.mem.endDate)) {
			logger.logWarning("Opss! You may have skipped specifying the End Date Please try again.")
			return false;
		}
		if (!$programmeValid($scope.productSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Product Please try again.")
			return false;
		}
		// itm
		if($scope.itmModes.pinMode==true){
			$scope.itmList.push({itmValue:"P"})
		}
		if($scope.itmModes.fpMode==true){
			$scope.itmList.push({itmValue:"F"})
		}
		if($scope.itmModes.irisMode==true){
			$scope.itmList.push({itmValue:"I"})
		}
		if($scope.itmModes.voiceMode==true){
			$scope.itmList.push({itmValue:"V"})
		}
		// chtm
		if($scope.chtmModes.posMode==true){
			$scope.chtmList.push({chtmValue:"P"})
		}
		if($scope.chtmModes.webMode==true){
			$scope.chtmList.push({chtmValue:"W"})
		}
		if($scope.chtmModes.mbMode==true){
			$scope.chtmList.push({chtmValue:"M"})
		}
		if($scope.chtmModes.oemMode==true){
			$scope.chtmList.push({chtmValue:"O"})
		}
		// intm
		if($scope.intmModes.smMode==true){
			$scope.intmList.push({intmValue:"S"})
		}
		if($scope.intmModes.pvcMode==true){
			$scope.intmList.push({intmValue:"P"})
		}
		if($scope.intmModes.nfcMode==true){
			$scope.intmList.push({intmValue:"N"})
		}
		if($scope.intmModes.msrMode==true){
			$scope.intmList.push({intmValue:"M"})
		}
		if($scope.intmModes.bioMode==true){
			$scope.intmList.push({intmValue:"B"})
		}
		console.log($scope.itmList)
		if (!$programmeValid($scope.programmeId))
			programme.programmeId = 0;
		else
			programme.programmeId = $scope.programmeId;
		programme.programmeCode = $scope.programmeCode;
		programme.programmeDesc=$scope.programmeDesc;
		programme.productId=$scope.productSelect;
		programme.active = true;
		programme.startDate= $filter('date')($scope.mem.startDate,'MM-dd-yyyy');
		programme.endDate= $filter('date')($scope.mem.endDate,'MM-dd-yyyy');
		programme.createdBy = $rootScope.UsrRghts.sessionId;
		programme.programmeType=$scope.tranMode;
		if($scope.tranMode=='D'){
			if($scope.selection.length<=0){
				if (!$programmeValid($scope.productSelect)) {
					logger.logWarning("Opss! You may have skipped specifying vouchers. Please try again.")
					return false;
				}
			}
			programme.vouchers=$scope.vouchers;
		}
		if($scope.tranMode=='CT'){
			if($scope.selection.length<=0){
				if (!$programmeValid($scope.productSelect)) {
					logger.logWarning("Opss! You may have skipped specifying vouchers. Please try again.")
					return false;
				}
			}
			programme.vouchers=$scope.cvouchers;
		}
	
		programme.itmList=$scope.itmList;

		programme.chtmList=$scope.chtmList;

		programme.intmList=$scope.intmList;


		blockUI.start()
		programmeSvc.UpdProgramme(programme).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The programme information was saved succesfully")
				$scope.programmeId = 0;
				$scope.programmeCode = "";
				$scope.programmeDesc = "";
				$scope.active = false;
				$scope.projectId=0;
				$scope.basketId=0;
				$scope.programmeEditMode = false;
				$scope.vouchers=[];
				$scope.tranMode="";
				$scope.cvouchers=[]
				$scope.loadProgrammeData();
				$scope.loadChannelSetting();
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

	
	$scope.open = function () {

		// console.log(familyMemId);
		var modalInstance = $modal.open({
			templateUrl: 'views/NewFi.html',
			controller: ModalInstanceCtrl,
			backdrop: 'static',
			keyboard: false,
			scope: $scope,
			resolve: {
				vouchers:function(){
					return $scope.vouchers;
				}
			}
		});


	};
	var ModalInstanceCtrl = function ($scope, $modalInstance,vouchers) {

		$scope.save = function () {
			$modalInstance.close($scope.vouchers)
			$scope.vouchers= $('#reasonDesc').val();
			console.log($scope.vouchers)
			var app = {};

			app.status="C"
				app.cancelReason=$scope.reason;
			app.businessId=$scope.businessId;
			app.createdBy = $rootScope.UsrRghts.sessionId;

			blockUI.start()
			invoiceApprovalSvc.UpdAppInvoiceStatus(app).success(function (response) {
				if (response.respCode == 200) {
					logger.logSuccess("Great! Application information was saved succesfully")
					$scope.active = false;
					$scope.priceDetailMode=true;
					$scope.appEditMode=false;
					$scope.appDetailMode=true;
					$scope.isDisabled = false;
					$$scope.loadInvoiceData();
				}
				else  {
					logger.logWarning(response.respMessage);
				}

				blockUI.stop();
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
				blockUI.stop();
			});

		}
	}
	
//	$scope.$watch("tranMode", function (newValue, oldValue) {
//        if ($programmeValid(newValue)) {
//            if (newValue != oldValue) {
//               if(newValue=='D'){
//            		$scope.loadVoucherData();
//               }else if(newValue=='CT'){
//            	
//            		$scope.loadCashVoucherData();
//               }
//            }
//        }
//        else
//            $scope.groups = [];
//    });
} ]).factory('programmeSvc', function ($http) {
	var EvProgrammeSvc = {};
	EvProgrammeSvc.GetProgrammes = function ($scope) {
		return $http({
			url: '/compas/rest/programme/gtProgrammes/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvProgrammeSvc.GetCashVouchers = function ($scope) {
		return $http({
			url: '/compas/rest/voucher/gtCashVouchers/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
//	/
//	EvProgrammeSvc.GetServiceProducts = function ($scope) {
//	return $http({
//	url: '/compas/rest/programme/gtServiceProducts/',
//	method: 'GET',
//	headers: { 'Content-Type': 'application/json' }
//	});
//	};

	EvProgrammeSvc.UpdProgramme = function (programme) {
		return $http({
			url: '/compas/rest/programme/updProgramme',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: programme
		});
	};
	return EvProgrammeSvc;
}).factory('$programmeValid', function () {
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