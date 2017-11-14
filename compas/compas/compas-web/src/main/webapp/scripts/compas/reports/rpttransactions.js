/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptTransaction', []).controller("rptTransactionCtrl", ["$scope", "$filter", "memStatementSvc","issueDeviceSvc","agentSvc", "$memStatementValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, memStatementSvc,issueDeviceSvc, agentSvc,$memStatementValid, $rootScope, blockUI, logger, $location,$http,$window) {
	$scope.transMemList = [];
	$scope.mem=[];
	var init;
	$scope.masters=[]
	$scope.showdata=true;
	$scope.showSer=true;
	$scope.showAgent=false;
	$scope.previewReport=function(mem)
	{
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
		if ($rootScope.UsrRghts.userClassId == 3) {
			var memberNo=$rootScope.UsrRghts.linkExtInfo;
		}else{

			var memberNo=mem.memberCode;
		}
		$scope.url = '/compas/reports?type=MS&FrDt=' +FromDt+'&ToDt='+ToDt;
	}
	$scope.previewTxnSummary=function(mem)
	{
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');

		$scope.url = '/compas/reports?type=SU&FrDt=' +FromDt+'&ToDt='+ToDt;
	}
	$scope.previewRetailerReport=function(mem)
	{
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
		if ($rootScope.UsrRghts.userClassId == 3) {
			var memberNo=$rootScope.UsrRghts.linkExtInfo;
		}else{

			var memberNo=mem.memberCode;
		}
		$scope.url = '/compas/reports?type=MS&FrDt=' +FromDt+'&ToDt='+ToDt;
	}



	$scope.masters=[{id:1,masterName:"Agents"},{id:2,masterName:"Devices"}]
	$scope.exportPdfReport = function(mem) {
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');

		$scope.url = '/compas/reports?type=SU&eType=P&FrDt=' +FromDt+'&ToDt='+ToDt;
	}
	$scope.exportPdfTxnReport = function(mem) {
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');

		$scope.url = '/compas/reports?type=MS&eType=P&FrDt=' +FromDt+'&ToDt='+ToDt;
	}


	$scope.exportExcelTxnReport = function(mem) {
	
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date.Please try again.")
					return false;
		}
		if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;

		}
		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		
			$scope.url = '/compas/reports?type=MS&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		

		
	}

	$scope.exportExcelReport = function(mem) {
	
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date.Please try again.")
					return false;
		}
		if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;

		}
		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		
			$scope.url = '/compas/reports?type=SU&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		

		
	}
	$scope.exportCnsmPdfReport = function(mem) {
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var FromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
		var ToDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');

		$scope.url = '/compas/reports?type=CN&eType=P&FrDt=' +FromDt+'&ToDt='+ToDt;
	}


	$scope.exportCnsmExcelReport = function(mem) {
	
		if (!$memStatementValid(mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date.Please try again.")
					return false;
		}
		if (!$memStatementValid(mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;

		}
		var FromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
		var ToDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
		
			$scope.url = '/compas/reports?type=CN&eType=E&FrDt='+ FromDt + '&ToDt=' + ToDt;
		

		
	}




}]).factory('memStatementSvc', function ($http) {
	var shpMemStatement = {};
	shpMemStatement.GetSafComTxns= function (cardnumber) {
		return $http({
			url: '/compas/rest/transaction/gtSafComTxns/'+cardnumber,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	shpMemStatement.GetAgentList= function ($scope) {
		return $http({
			url: '/compas/rest/transaction/gtAgentList',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	shpMemStatement.GetDeviceList= function ($scope) {
		return $http({
			url: '/compas/rest/transaction/gtDeviceList',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	return shpMemStatement;
}).factory('$memStatementValid', function () {
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
