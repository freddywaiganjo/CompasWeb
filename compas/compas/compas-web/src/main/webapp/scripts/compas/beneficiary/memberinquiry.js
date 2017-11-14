/**
 * Member Angular Module
 */
'use strict';
angular.module('app.memberinquiry', []).controller("memberInquiryCtrl", ["$scope", "$filter", "memberInquirySvc","memStatementSvc", "locationSvc","$memberValid", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter, memberInquirySvc,memStatementSvc,locationSvc, $memberValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.bioView = true;
    $scope.memVerify = false;
    $scope.memVerify = true;
    $scope.showMemDetails=true;
    $scope.memberViewMode=true;
 var classId=$rootScope.UsrRghts.userClassId;
  $scope.loadMemberData = function (locationId) {
	  $scope.userClassId=classId;
        $scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberInquiryEditMode = false;
        memberInquirySvc.GetMembers(locationId).success(function (response) {
        	
            return $scope.members = response, $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMembers = $scope.filteredMembers.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredMembers = $filter("filter")($scope.members, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMembers = $filter("orderBy")($scope.members, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMembers = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });

    }
  $scope.loadCustomers = function () {
 	 $scope.memEnrollment=true;
     $scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberEditMode = false;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true; $scope.memEnrollment = true;
     customerSvc.GetCustomers().success(function (response) {
     	
         return $scope.members = response, $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.select = function (page) {
             var end, start;
             return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMembers = $scope.filteredMembers.slice(start, end)
         }, $scope.onFilterChange = function () {
             return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
         }, $scope.onNumPerPageChange = function () {
             return $scope.select(1), $scope.currentPage = 1
         }, $scope.onOrderChange = function () {
             return $scope.select(1), $scope.currentPage = 1
         }, $scope.search = function () {
             return $scope.filteredMembers = $filter("filter")($scope.members, $scope.searchKeywords), $scope.onFilterChange()
         }, $scope.order = function (rowName) {
             return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMembers = $filter("orderBy")($scope.members, rowName), $scope.onOrderChange()) : void 0
         }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMembers = [], (init = function () {
             return $scope.search(), $scope.select($scope.currentPage)
         })();
     }).error(function (data, status, headers, config) {
         logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
     });

 }
  $scope.$watch("locationSelect", function (newValue, oldValue) {
      if ($memberValid(newValue)) {
          if (newValue != oldValue) {
          	//if($scope.merchantSelect==""){
          	$scope.loadMemberData(newValue);
          	//}
          	
          }
      }
      
  });

	 /**
	 * gets the details of the location created
	 */
	$scope.loadLocationData = function () {
		$scope.locations = []

		locationSvc.GetLocations($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.locations = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadLocationData();
	
 
 
/*  if($rootScope.UsrRghts.sessionName=="Padmin"){
		$scope.loadCustomers();
	}else{
		$scope.loadMemberData();
	}*/
  $scope.loadSingleMember = function () {
	  $scope.userClassId=classId;
	  $scope.members = "";
      memberInquirySvc.GetSingleMember($rootScope.UsrRghts.linkExtInfo).success(function (response)
    		  {
    	  $scope.members = response;
    	  }).error(function (data, status, headers, config) {
          logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
      });
  }
  
  $scope.loadMembers = function () {
	  $scope.userClassId=classId;
      $scope.members = [];
      tranSvc.GetMembers().success(function (response) {
          $scope.memberCode = void 0;
          for (var i = 0; i <= response.length - 1; i++) {
              $scope.members.push(response[i].transMemberNo);
          }
      }).error(function (data, status, headers, config) {
          logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
      });
  };
  
     //if(classId==1){
    
    	 $scope.blNewInq=false;
    	// $scope.loadMemberData();
    
//     }else if(classId==2)
//	 {
    	 //$scope.loadMembers();
//	 }
//     else if(classId==3)
//     {
//    	  $scope.loadSingleMember();
//    	 
//     }
     $scope.proceedInq = function(){
     	 //alert("proceedInq");
         //$scope.bioView = true;
         $scope.memVerify = true;        
         $scope.memInq = true;
         $scope.blNewInq = true;
         $scope.showMemDetails=false;
        
     }
     $scope.loadBioMemberDetails = function (bioId) {
   	  $scope.userClassId=classId;
         $scope.members = [];
         memberInquirySvc.GetMemberBio(bioId).success(function (response) {
        	  $scope.members = response;
        	  console.log( $scope.members);
         }).error(function (data, status, headers, config) {
             logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
         });
     };
     
     $scope.bioMemberInfo = function (memberCode) {
    	 $scope.member = [];
   	   memberInquirySvc.GetMemberBio(memberCode).success(function (response)
       {
   		 
   		if (response.respCode == 200)
        {
   		 $scope.members = response;
   		 $scope.members.bioId;
            if($scope.members.bioId == 0)
        	{
                $scope.bioView = false;
                logger.logWarning("Opss!Member Not Enrolled For BIO.")
        	}
            else
        	{
            	 $scope.proceedInq ();
            	 if($scope.members.gender=="M"){
            		 $scope.members.gender="Male";
            	 }
            	 else
            		 {
            		 $scope.members.gender="Female";
            		 }
            	//$scope.memVerify = true;
            	// $scope.memInq = false;
                // $scope.blNewInq = false;
            	//$scope.bioView = true;
        		//$scope.memEnrollment = false;
        		//console.log($scope.members);
        	}
        }
        else
        {
        	logger.logWarning("Opss! The member number specified does not exist. Please try again.")
        }
                }).error(function (data, status, headers, config) {
             logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
         });    
     };
 
 
 $scope.viewMember = function (member) {
    	if (!$filter('checkRightToView')($rootScope.UsrRghts.rightsHeaderList, "#"+$location.path()))
     	{
     		logger.log("Oh snap! You are not allowed to view the member.");
     		return false;
     	}
        $scope.memberInquiryEditMode = true;
        $scope.showMemDetails=false;
        $scope.memberViewMode=false;
        $scope.blNewInq=true;
        $scope.memberId = member.memberId;
        $scope.memberNo = member.memberNo;
        $scope.showCamera=false;
        $scope.capPic=true;
        $scope.showImage=true;
        $scope.surName =member.surName;
        $scope.title=member.title;
        $scope.firstName=member.firstName;
        $scope.otherName = member.otherName;
        $scope.idPassPortNo = member.idPassPortNo;
        if(member.gender=="M"){
      	  $scope.gender="Male"; 
      	  $scope.male=true; 
        }
        else if(member.gender=="F"){
      	  $scope.gender="Female"; 
      	  $scope.feMale=true; 
        }
     
        $scope.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
        $scope.maritalStatus = member.maritalStatus;
        $scope.weight = member.weight;
        $scope.height = member.height;
        $scope.nhifNo = member.nhifNo;
        $scope.employerName =member.employerName;
        $scope.occupation = member.occupation;
        $scope.nationality = member.nationality;
        $scope.postalAdd = member.postalAdd;
        $scope.physicalAdd = member.physicalAdd;
        $scope.homeTelephone =member.homeTelephone;
        $scope.officeTelephone =member.officeTelephone;
        $scope.cellPhone =member.cellPhone;
        $scope.email =member.email;
        $scope.nokName = member.nokName;
        $scope.relationSelect =member.relationId;
        $scope.relationDesc=member.relationDesc;
        $scope.nokIdPpNo = member.nokIdPpNo;
        $scope.nokTelephoneNo = member.nokTelephoneNo;
        $scope.nokPostalAdd = member.nokPostalAdd;
        $scope.myCroppedImage=member.memberPic;
        $scope.coverSelect=member.coverId;
        $scope.coverName=member.coverName;
        $scope.categorySelect=member.categoryId;
        $scope.categoryName=member.categoryName;
        $scope.fullName=member.fullName;
        $scope.ipLimit=member.ipLimit;
        $scope.opLimit=member.opLimit;
        $scope.cardNumber=member.cardNumber;
        $scope.cardBalance=member.cardBalance;
        //$scope.loadSafComData($scope.cardNumber);
    };
    
    $scope.cancelMember=function(){
     $scope.memberInquiryEditMode = false;
     $scope.userClassId=classId;
        $scope.memberId = 0;
        $scope.surName = "";
        console.log("cancelled");
        $scope.memVerify = false;        
        $scope.showMemDetails=true;
        if($rootScope.UsrRghts.sessionName=="Padmin"){
    		$scope.loadCustomers();
    	}else{
    		$scope.loadMemberData();
    	}
   }
    
    $scope.cancelInq=function(){
        $scope.memberInquiryEditMode = false;
       
        $scope.memberViewMode=true;
    	
    }


}]).factory('memberInquirySvc', function ($http) {
    var shpMemberInquirySvc = {};
    shpMemberInquirySvc.GetMembers = function (locationId) {
        return $http({
            url: '/compas/rest/member/gtMembers/'+locationId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    shpMemberInquirySvc.GetSingleMember = function (memberNo) {
    	   return $http({
        	url: '/compas/rest/member/gtsingleMember?memberNo=' + encodeURIComponent(memberNo),
            method: 'GET'
            
        });
    };
    shpMemberInquirySvc.GetMemberBio = function (bioId) {
 	   return $http({
     	url: '/compas/rest/member/gtbioMember?bioId=' + encodeURIComponent(bioId),
         method: 'GET'
         
     });
 	
 };

    return shpMemberInquirySvc;
}).factory('$memberValid', function () {
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
}).
directive('capturem', function(logger, blockUI, loginSvc, localStorageService, $rootScope, $location) {
	return {
		link : function($scope, element, attrs) {
			element.bind('click', function() {
				var d = $.parseJSON($("#bioId")[0].OnDeviceInit());
				//console.log(d);
				if (d.code == '200') {
					//blockUI.start();				
					//if ($scope.members.bioId > 0) {
						var fingerPos = afisIp + '|' + afisPort;
						//console.log(fingerPos);
						var captureResponse = $.parseJSON($("#bioId")[0].OnInquiry(fingerPos));
						//console.log(captureResponse);
						if (captureResponse.code != '200') {
							logger.logError(captureResponse.detail);
						} else {
							logger.logSuccess(' Validated Succesfully!!');	
							var src = "data:image/png;base64,";
							src = src + captureResponse.rawImg64;
							$(this).removeAttr('src');
							$(this).attr('src', src).attr('style', 'border-radius:20px;width:258px;height:320px;');
							console.log(captureResponse.idkitId);
							 $scope.proceedInq();
							 if(captureResponse.idkitId==0)
								 {logger.logError('User Not Enrolled for BIO');}
							 else
								 { $scope.loadBioMemberDetails(captureResponse.idkitId);
								  $(this).attr('src', "images/enroll/noImage_1.png").attr('style', 'width:300px;height:320px;');}
							 
						}
	
					//} else {
						//logger.logError('User Not Enrolled for BIO');
					//}
					//blockUI.stop();
				}
				else
					{
					  logger.logError('No Device detected!!');
					}
			})
		},
	}
})
