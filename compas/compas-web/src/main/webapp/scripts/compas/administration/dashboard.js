/**
 * Member Angular Module
 */
'use strict';
angular.module('app.dashboard', []).controller("dashBoardCtrl", ["$scope", "$filter", "$rootScope", "dashBoardSvc","blockUI", "logger" ,"$location", function ($scope, $filter, $rootScope,dashBoardSvc, blockUI, logger, $location) {
	var init;
	var percentage=0;
	$scope.hide=false;
	$scope.tempCountList = [];
	$scope.donutChart2=[];
	$scope.easypiechart3=[]
	$scope.countList=[];
	$scope.agentTxnList=[];
	$scope.userCount=0;
	$scope.memberCount=0;
	$scope.proiderCount=0;
	$scope.brokerCount=0;
	$scope.tempLineChartDetail=[];
	$scope.lineChartDetail=[];
	$scope.dashBoardViewMode=true;
	$scope.totalAmount=0.00;
	$scope.netAmount=0.00;
	$scope.voideAmount=0.00;
	$scope.tempAmountList =[];
	$scope.pieChart=[];
	$scope.flowChartList=[]
	$scope.totalCollection=0.00
	$scope.sbp=0.00
	$scope.landrates=0.00
	$scope.pos=0.00
	$scope.cmPercentage=0.00; 
	$scope.vaPercentage= 0.00; 
	$scope.caPercentage=0.00; 
	$scope.loadCountDetail=function(){
		dashBoardSvc.GetCountDetail().success(function (response) {
			$scope.tempCountList = response;
			if($scope.tempCountList.length>0){

				for (var i = 0; i <= $scope.tempCountList.length - 1; i++) {
					if($scope.tempCountList[i].detailDescription=="USERS"){
						$scope.userCount=$scope.tempCountList[i].detailCount;

					}else if($scope.tempCountList[i].detailDescription=="CUSTOMERS"){
						$scope.memberCount=$scope.tempCountList[i].detailCount;
					}
					else if($scope.tempCountList[i].detailDescription=="REGISTERED DEVICES"){
						$scope.deviceCount=$scope.tempCountList[i].detailCount;
					}
					else if($scope.tempCountList[i].detailDescription=="TRANSACTIONS"){
						$scope.brokerCount=$scope.tempCountList[i].detailCount;
					}else if($scope.tempCountList[i].detailDescription=="ISSUE CARDS"){
						$scope.issueCardsCount=$scope.tempCountList[i].detailCount;
					}

					var obj =new Object();
					obj.data=$scope.tempCountList[i].detailCount;
					obj.label=$scope.tempCountList[i].detailDescription;
					// $scope.countList.push( obj);
					$scope.isdata=true;
				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadCountDetail();

	$scope.loadTransChartDetail=function(){
		dashBoardSvc.GetTransChartDetail().success(function (response) {
			$scope.tempLineChartDetail = response;
			if($scope.tempLineChartDetail.length>0){
				for(var i=0;i <= $scope.tempLineChartDetail.length - 1; i++){
					var objd =new Object();
					objd.year=$scope.tempLineChartDetail[i].monthName;
					objd.a=$scope.tempLineChartDetail[i].load;
					objd.b=$scope.tempLineChartDetail[i].purchase;
					objd.c=$scope.tempLineChartDetail[i].totalTransCount;
					$scope.lineChartDetail.push(objd);
				}
			}

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	//$scope.loadTransChartDetail();
	$scope.loadFlowChartDetail=function(){
		dashBoardSvc.GetFlowChartDetail().success(function (response) {
			$scope.flowChartList = response;
			if($scope.flowChartList.length>0){

				for (var i = 0; i <= $scope.flowChartList.length - 1; i++) {
					var objflow =new Object();
					objflow.data=$scope.flowChartList[i].transCount;
					objflow.label=$scope.flowChartList[i].service;
					$scope.countList.push(objflow);
					//console.log($scope.countList)
					$scope.isdata=true;

				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadFlowChartDetail();
	$scope.loadAgentDetail=function(){
		dashBoardSvc.GetAgentDetail().success(function (response) {
			$scope.agentList = response;
			if($scope.agentList.length>0){

				for (var i = 0; i <= $scope.agentList.length - 1; i++) {
					var objflow =new Object();
					objflow.data=$scope.agentList[i].detailCount;
					objflow.label=$scope.agentList[i].detailDescription;
					$scope.agentTxnList.push(objflow);
					//console.log($scope.agentTxnList)
					$scope.isdata=true;

				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadAgentDetail();
	$scope.loadCollectionDetail=function(){

		dashBoardSvc.GetCollectionDetail().success(function (response) {
			$scope.collectionList = response;
			if($scope.collectionList.length>0){
				var sbp=0;
				var landRated=0;
				var pos=0;

				for (var i = 0; i <$scope.collectionList.length; i++) {
					var result=0;
					$scope.cvPercentage=0;
					if($scope.collectionList[i].detailDescription=="Commodity Voucher Collections"){
						$scope.sbp=$scope.collectionList[i].amount;
						var sbp=$scope.collectionList[i].totalTxns;

					}else if($scope.collectionList[i].detailDescription=="Value Voucher Collections"){
						$scope.landrates=$scope.collectionList[i].amount;
						var landrates=$scope.collectionList[i].totalTxns;
					}
					else if($scope.collectionList[i].detailDescription=="Cash Voucher Collections"){
						$scope.pos=$scope.collectionList[i].amount;
						var pos=$scope.collectionList[i].totalTxns;
					}
				}
				$scope.totalCollection=$filter('number')(sbp + landrates + pos, fractionSize);
				if(sbp>0){
					var comPer = ((sbp/$scope.totalCollection)*100)}else var comPer =0.00
				if(landrates){
					var valPer = ((landrates/$scope.totalCollection)*100)}else var valPer=0.00
				if(pos>0){
					var casPer = ((pos/$scope.totalCollection)*100)}else var casPer=0.00
//				/return Math.round(result, 2);
				$scope.cmPercentage= Math.round(comPer, 2); 
				$scope.vaPercentage= Math.round(valPer, 2); 
				$scope.caPercentage= Math.round(casPer, 2); 
			}

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});

	}
	$scope.loadCollectionDetail();

	$scope.loadPenAuthNoti=function(){
    	dashBoardSvc.GetPendingAuthNoti().success(function (response) {
         $scope.pendingNotification = response;
         $scope.pendingAuthCount=response[0].pendingAuthCount;
         
     }).error(function (data, status, headers, config) {
         logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
     });
    }
	$scope.loadPenAuthNoti()
	$scope.loadPercentage=function()
	{
		// var result = (($scope.sbp/$scope.totalCollection)*100)
		//$scope.cvPercentage=Math.round(result, 2);
		$scope.easypiechart3 = {

				percent:20,
				options: {
					animate: {
						duration: 1e3,
						enabled: !0
					},
					barColor: "#2EC1CC",
					lineCap: "square",
					size: 180,
					lineWidth: 20,
					scaleLength: 0
				}

		}
		$scope.easypiechart4 = {

				percent:50,
				options: {
					animate: {
						duration: 1e3,
						enabled: !0
					},
					barColor: "#b13d31",
					lineCap: "square",
					size: 180,
					lineWidth: 20,
					scaleLength: 0
				}

		}
		$scope.easypiechart5 = {

				percent:30,
				options: {
					animate: {
						duration: 1e3,
						enabled: !0
					},
					barColor: "#2333ae",
					lineCap: "square",
					size: 180,
					lineWidth: 20,
					scaleLength: 0
				}

		}
	}

	angular.foreach=function(e){
		for (var i=0;$scope.relations.length-1;i++ ){
			$scope.relations[i].slice(i,1)
		}
	}
	if($rootScope.UsrRghts.userClassId == 4){
		$scope.dashBoardViewMode=false;
		$scope.loadCountDetail();
		$scope.loadTransChartDetail();
	}

	$scope.loadAmpuntDetail=function(){
		dashBoardSvc.GetAmountDetail().success(function (response) {
			$scope.tempAmountList = response;
			console.log(response);
			if($scope.tempAmountList.length>0){

				for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {
					if($scope.tempAmountList[i].detailDescription=="TOTAL AMOUNT"){
						$scope.totalAmount=$scope.tempAmountList[i].amount;
					}
					else if($scope.tempAmountList[i].detailDescription=="NET AMOUNT"){
						$scope.netAmount=$scope.tempAmountList[i].amount;
					}
					else if($scope.tempAmountList[i].detailDescription=="VOIDED AMOUNT"){
						// $scope.voideAmount= parseFloat
						// ($scope.tempAmountList[i].amount);
						$scope.voideAmount= $scope.tempAmountList[i].amount;
					}

				}
			}
		})
	}
	$scope.loadAmpuntDetail();
	$scope.donutChart2.options = {
			series: {
				pie: {
					show: !0,
					innerRadius: .45
				}
			},
			legend: {
				show: !1
			},
			grid: {
				hoverable: !0,
				clickable: !0
			},
			colors: ["#176799", "#2F87B0", "#42A4BB", "#5BC0C4", "#78D6C7"],
			tooltip: !0,
			tooltipOpts: {
				content: "%p.0%, %s",
				defaultTheme: !1
			}
	}
	$scope.pieChart.options = {
			series: {
				pie: {
					show: !0
				}
			},
			legend: {
				show: !0
			},
			grid: {
				hoverable: !0,
				clickable: !0
			},
			colors: ["#23AE89", "#2EC1CC", "#FFB61C", "#E94B3B"],
			tooltip: !0,
			tooltipOpts: {
				content: "%p.0%, %s",
				defaultTheme: !1
			}
	}

	$scope.roundedPercentage = function(myValue, totalValue){
		var result = ((myValue/totalValue)*100)
		return Math.round(result, 2);
	}

	//$scope.loadPercentage()

}]).factory('dashBoardSvc', function ($http) {

	var compasDashBoardSvc = {};
	compasDashBoardSvc.GetFlowChartDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtFlowChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetCountDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtCountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	compasDashBoardSvc.GetTransChartDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtTransChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	compasDashBoardSvc.GetAmountDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtAmountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetAgentDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtAgentDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetCollectionDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtCollectionDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetPendingAuthNoti = function () {
	        return $http({
	            url: '/compas/rest/dashBoard/gtPenAuthNot',
	            method: 'GET',
	            headers: {'Content-Type': 'application/json'}
	        });
	    };
	return compasDashBoardSvc;
}).directive("flotChart", [function () {

	return {
		restrict: "A",
		scope: {
			data: "=",
			data2: "=",
			options: "="
		},

		link: function (scope, ele,attrs) {
			var data, options, plot,data2,plot2;
			scope.$watch("$parent.countList",function (newValue, oldValue){
				return data = scope.$parent.countList, options = scope.options, plot = $.plot(ele[0], data, options)
			},true)

		}
	}

}]).directive("agentchart", [function () {

	return {
		restrict: "A",
		scope: {
			data: "=",
			data2: "=",
			options: "="
		},

		link: function (scope, ele,attrs) {
			var data, options, plot,data2,plot2;
		
scope.$watch("$parent.agentTxnList",function (newValue, oldValue){
				return data = scope.$parent.agentTxnList, options = scope.$parent.donutChart2.options, plot = $.plot(ele[0], data, options)
			},true)

		}
	}

}])
.directive("morrisChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},
		link: function (scope, ele, attrs) {
			var colors, data, func, options;

			switch (data = scope.data, attrs.type) {
			case "bar":
				scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
					if(newValue.length>0){
						data = scope.$parent.lineChartDetail;
						data = scope.$parent.lineChartDetail;
						return colors = void 0 === attrs.barColors || "" === attrs.barColors ? null : JSON.parse(attrs.barColors), options = {
								element: ele[0],
								data: data,
								xkey: attrs.xkey,
								ykeys: JSON.parse(attrs.ykeys),
								labels: JSON.parse(attrs.labels),
								barColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
								stacked: attrs.stacked || null,
								resize: !0
						}, attrs.formatter && (func = new Function("y", "data", attrs.formatter), options.formatter = func),new Morris.Bar(options);}
				},true);


			}
		}

	}
}]).directive("lineChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},

		link: function (scope, ele,attrs) {
			var colors, data, func, options;
			scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
				if(newValue.length>0){
					data = scope.$parent.lineChartDetail;
					return colors = void 0 === attrs.lineColors || "" === attrs.lineColors ? null : JSON.parse(attrs.lineColors), options = {
							element: ele[0],
							data: data,
							xkey: attrs.xkey,
							ykeys: JSON.parse(attrs.ykeys),
							labels: JSON.parse(attrs.labels),
							lineWidth: attrs.lineWidth || 2,
							lineColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
							resize: !0
					}, new Morris.Line(options);
				}},true);

		}
	}

}])