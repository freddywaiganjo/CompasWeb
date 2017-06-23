/**
 * Member Angular Module
 */
'use strict';
angular.module('app.member', []).controller("membersCtrl", ["$scope", "$filter", "memberSvc","memberInquirySvc","programmeSvc","bnfgrpSvc", "locationSvc","$memberValid", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter, memberSvc,memberInquirySvc,programmeSvc,bnfgrpSvc,locationSvc, $memberValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.categories = [];
	$scope.members = [];
	$scope.departments = [];
	$scope.familySizes = [];
	$scope.programmes = [];
	$scope.relations = [];
	$scope.data={};
	$scope.covers = [];
	$scope.categories = [];
	$scope.bnfGrpSelect="";
	$scope.isParent=true;
	$scope.mem=[];
	$scope.donutChart2=[];
	$scope.data=[];
	$scope.memEnrollment = true;
	$scope.showEnroll=true;
	$scope.bioImage="";
	$scope.entry = [];
	$scope.famMemEntries = [];
	$scope.selection=[]
	$scope.beneficiaries = [];
	$scope.showMembers=false;
	$scope.beneficiaryEditMode = false;
	$scope.modalInstance = null;
	$scope.familyBioId=0;
	$scope.allItemsSelected = false;
	$scope.familyMemDetails =[];
	$scope.household=false;
	var familyMemId={};
	var style = {'background-color': '#eef8fc', 'color': '#26929c'};
	var styleInputs = {'color': '#26929c'};

	$scope.showProgrammes=true;
	$scope.memberEditMode = false;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true; 
	$scope.loadMemberData = function (locationId) {

		$scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberEditMode = false;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true; 
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
//	$scope.loadMemberData();
	
	$scope.$watch("locationSelect", function (newValue, oldValue) {
        if ($memberValid(newValue)) {
            if (newValue != oldValue) {
            	//if($scope.merchantSelect==""){
            	//$scope.memberEditMode = false;
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
	
	$scope.loadRelations=function(){
		$scope.relations=[];
		memberSvc.GetRelations().success(function(response){
			$scope.relations=response;
			// console.log($scope.relations);
		})
	}
	$scope.loadRelations();
	$scope.loadBnfgrpData = function () {
		$scope.bnfgrps = [], $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "", $scope.bnfgrpEditMode = false; $scope.previewServices=false;
		bnfgrpSvc.GetBnfgrps($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.bnfgrps = response
		})
	}
	$scope.loadBnfgrpData();

	$scope.LoadProgrammesByBnfGrp = function (bnfGrpId) {
		$scope.programmes = [];
		memberSvc.GetProgrammesByBnfGrp(bnfGrpId).success(function (response) {
			$scope.programmes = response;
			console.log( $scope.programmes)
		});
	}
	//$scope.LoadProgrammesByBnfGrp($scope.bnfGrpSelect)


	$scope.$watch("bnfGrpSelect", function (newValue, oldValue) {
		if ($memberValid(newValue)) {
			if (newValue != oldValue) {
				$scope.showProgrammes=false
				if(oldValue!=null && oldValue!=""){
					$scope.LoadProgrammesByBnfGrp(newValue);
				}else{
				if($scope.programmes.length<=0){
					$scope.LoadProgrammesByBnfGrp(newValue);
				}else {

				}}


			}
		}
		else
			$scope.groups = [];
	});

	$scope.fillData=function(){
		$scope.idPassPortNo = "H3020634";
		$scope.mem.dateOfBirth="12/12/1988";
		$scope.maritalStatus = "MARRIED";
		$scope.weight ="60KG";
		$scope.height = "5.2";
		$scope.nhifNo = "3999664";
		$scope.employerName ="J & J LIMITED";
		$scope.occupation ="JOB";
		$scope.nationality = "KENYAN";
		$scope.postalAdd ="0999-8888";
		$scope.physicalAdd = "MOGOTIO ROAD,PARKLANDS";
		$scope.homeTelephone ="0204958798";
		$scope.officeTelephone ="0208990098";
		$scope.cellPhone ="0717555778";
		$scope.email ="";
		$scope.nokName = "JOHN MUBURU";
		$scope.relationSelect ="3";
		$scope.relationDesc="SISTER";
		$scope.nokIdPpNo ="HJ8776009";
		$scope.nokTelephoneNo = "0717664554";
		$scope.nokPostalAdd = "HJ78889D";

		$scope.coverSelect=0;
		$scope.coverName="";
		$scope.categorySelect=0;
		$scope.categoryName="";

		$scope.ipLimit=0;
		$scope.opLimit=0;
		$scope.cams=0;
	}

	if($rootScope.UsrRghts.userClassId==1){
		$scope.loadMemberData();
		$scope.memberEditMode = false;
		console.log( $scope.memberEditMode)
	}
	else if($rootScope.UsrRghts.classId==4){

	}

	$scope.editMember = function (member) {
		$scope.memberEditMode = true;
		$scope.memberDetailMode = false;
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


		$scope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
		//$scope.dateOfBirth=member.dateOfBirth;
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
		$scope.cams=0;
		$scope.bnfGrpSelect=member.bnfGrpId;
		$scope.showProgrammes=false
		$scope.programmes=member.programmes;
		console.log( $scope.programmes)
		console.log(member.bioId)
		$scope.bioId=member.bioId;
		if($scope.bioId==0){
			$scope.showEnroll=false;
		}
		else{
			$scope.showEnroll=true;
		}
		$scope.famMemEntries=member.familyMemList;
		$scope.familySize=member.familySize;
		$scope.isDisabled=true;
		
	};


	$scope.addMember = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new Members.");
			return false;
		}
		// $scope.loadProgrammeData();
		$scope.allItemsSelected = false;
		$scope.isDisabled=false;
		$scope.photoUploadMode=true;
		$scope.memberEditMode = true;
		$scope.memberDetailMode = false;
		$scope.showCamera=false;
		$scope.capPic=true;
		$scope.showImage=true;
		$scope.memberId =0;
		$scope.memberNo = "";
		$scope.surName = "";
		$scope.title="";
		$scope.firstName="";
		$scope.otherName = "";
		$scope.idPassPortNo = "";
		$scope.feMale=false;
		$scope.male=false;  
		$scope.dateOfBirth="";
		$scope.maritalStatus = "";
		$scope.weight = "";
		$scope.height = "";
		$scope.nhifNo = "";
		$scope.employerName = "";
		$scope.occupation = "";
		$scope.nationality = "";
		$scope.postalAdd = "";
		$scope.physicalAdd = "";
		$scope.homeTelephone = "";
		$scope.officeTelephone = "";
		$scope.cellPhone = "";
		$scope.email = "";
		$scope.nokName = "";
		$scope.relationSelect = 0;
		$scope.nokIdPpNo = "";
		$scope.nokTelephoneNo = "";
		$scope.nokPostalAdd = "";
		$scope.ipLimit="";
		$scope.opLimit="";
		$scope.categorySelect=0;
		$scope.coverSelect=0;
		// $scope.departmentSelect =0;
		// $scope.familySizeSelect = 0;
		$scope.bioId=0;
		$scope.cams=0;
		// $scope.selected=false;
		// $scope.parentMemberNo="";
		$scope.memberPic="";
		$scope.bnfGrpSelect="";
		$scope.familySize=0;
		$scope.famMemEntries = [];
		$scope.showProgrammes=true
		$scope.programmes=[];

	};
	$scope.myImage='';
	$scope.myCroppedImage='';

	var handleFileSelect=function(evt) {
		$scope.showCamera=true;
		$scope.capPic=false;
		$scope.showImage=false;
		var file=evt.currentTarget.files[0];
		var reader = new FileReader();
		reader.onload = function (evt) {
			$scope.$apply(function($scope){
				$scope.myImage=evt.target.result;
			});
		};
		reader.readAsDataURL(file);

	};
	angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

	$scope.addParentMemNo=function(){
		if($scope.selected==true){
			$scope.isParent=true;
		}
		else{
			$scope.isParent=false;
		}
	}
	$scope.camInit=function(){
		var obj = $("#camId")[0].OnFindCams();
		var res = jQuery.parseJSON(obj);
		$scope.cameraList=[];

		var str={};
		if(res.code == 200)
		{

			str = res.names.split(";");
			$("#cams").empty();
			for(var i =0;i<(str.length-1);i= i+1){
				var obj =new Object();
				$("#cams").append('<option value='+i+'>'+str[i]+'</option>');
				obj.id=i;
				obj.name=str[i];
				$scope.cameraList.push(obj);
			}
		}
		else
		{
			// logger.logWarning("No Attached Cameras Detected");
			// return false;
		}
	}
	//$scope.camInit();

	$scope.camload=function(){
		var imgObj = $("#camId")[0].OnCapture();
		var res = jQuery.parseJSON(imgObj);
		console.log(res);
		if(res.code == 200)
		{
			$scope.showCamera=false;
			$scope.capPic=false;
			$scope.showImage=false;
			var src = "data:image/png;base64,";
			src = src + res.image;
			$scope.myImage=src;
			// $('.imgPic').removeAttr('src');
			// $('.imgPic').attr('src',src).attr('style','border-radius:20px;');
		}
	}


	$scope.showCam=function(){
		$scope.showCamera=false;
		$scope.capPic=true;
		$scope.showImage=true;
		console.log($("#cams").val());

		if (!$memberValid($scope.cams)) {
			logger.logWarning("Opss! You may have skipped specifying the Camera. Please try again.")
			return false;
		}
		var start = $("#camId")[0].OnLoadCam($("#cams").val());
		console.log(start);
	}
	Webcam.set({
		// live preview size
		width: 320,
		height: 240,

		// device capture size
		dest_width: 320,
		dest_height: 240,

		// final cropped size
		crop_width: 240,
		crop_height: 240,

		// format and quality
		image_format: 'jpeg',
		jpeg_quality: 90
	});


	$scope.take_snapshot=function() {
		$scope.showCamera=false;
		$scope.capPic=false;
		$scope.showImage=false;
		// take snapshot and get image data
		Webcam.snap( function(data_uri) {
			// display results in page
			// var src = "data:image/png;base64,";
			// src = src + data_uri;
			$scope.myImage=data_uri;
			// document.getElementById('results').innerHTML =
			// '<h2>Here is your image:</h2>' +
			// '<img src="'+data_uri+'"/>';
		} );
	}

	$scope.cancelMember = function () {
		$scope.memberEditMode = false;
		$scope.familySize=0;
		$scope.memberDetailMode = true;
		$scope.photoUploadMode=true;
		$scope.memberViewMode=true;
		$scope.showCamera=false;
		$scope.capPic=true;
		$scope.showImage=true;
		$scope.memberId =0;
		$scope.memberNo = "";
		$scope.surName = "";
		$scope.title="";
		$scope.firstName="";
		$scope.otherName = "";
		$scope.idPassPortNo = "";
		$scope.feMale=false;
		$scope.male=false;  
		$scope.dateOfBirth="";
		$scope.maritalStatus = "";
		$scope.weight = "";
		$scope.height = "";
		$scope.nhifNo = "";
		$scope.employerName = "";
		$scope.occupation = "";
		$scope.nationality = "";
		$scope.postalAdd = "";
		$scope.physicalAdd = "";
		$scope.homeTelephone = "";
		$scope.officeTelephone = "";
		$scope.cellPhone = "";
		$scope.email = "";
		$scope.nokName = "";
		$scope.relationSelect = 0;
		$scope.nokIdPpNo = "";
		$scope.nokTelephoneNo = "";
		$scope.nokPostalAdd = "";
		$scope.categorySelect=0;
		$scope.coverSelect=0;
		$scope.ipLimit="";
		$scope.opLimit="";
		$scope.showProgrammes=true;
		$scope.bnfGrpSelect="";
		// $scope.departmentSelect =0;
		// $scope.familySizeSelect = 0;

		$scope.isDisabled=false;
		// $scope.selected=false;
		// $scope.parentMemberNo="";
		$scope.memberPic="";
		$scope.programmes=[];
		$scope.bioId=0;
	}
	$scope.next=function(){
		if (!$memberValid($scope.firstName)) {
			logger.logWarning("Opss! You may have skipped specifying the Card Holder's fullname. Please try again.")
			return false;
		}
		if (!$memberValid($scope.idPassPortNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Card Holder's Ration no. Please try again.")
			return false;
		}
		if (!$memberValid($scope.dateOfBirth)) {
		logger.logWarning("Opss! You may have skipped specifying the Card Holder's Birth Date. Please try again.")
		return false;
		}
		if (!$memberValid($scope.bnfGrpSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Vulnerability Criteria. Please try again.")
			return false;
			}
		$scope.tempPrgs=[];
		for(var i=0;i<$scope.programmes.length;i++){
			if($scope.programmes[i].isActive==true){
				$scope.tempPrgs.push($scope.programmes[i]);
			}
		}
		if ($scope.tempPrgs.length<=0) {
			logger.logWarning("Opss! You may have skipped specifying the programme. Please try again.")
			return false;
		}
		if($scope.memberId==0){
			$scope.memberEditMode = true;
			$scope.memberDetailMode = true;
			$scope.photoUploadMode=false;
		}
		else{
			$scope.memberEditMode = true;
			$scope.memberDetailMode = true;
			$scope.photoUploadMode=false;
			$scope.capPic=false;
			$scope.showImage=true;
			$scope.showCamera=false;
		}

		if($scope.selection.length>0){
			for(var j=0;j<$scope.selection.length;j++){
				for(var i=0;i<$scope.programmes.length;i++){
					if($scope.selection[j].programmeId==$scope.programmes[i].programmeId){
						var getItms = $scope.programmes[i].itmModes.split(",")
						for(var k=0;k<getItms.length;k++){
							if(getItms[k]=="F" && $scope.memberId!=0){
								//$scope.memEnrollment = false;
								
								$scope.showEnroll=false;
								return;
							}else{
								$scope.memEnrollment = true;
								$scope.showEnroll=true;
							}
						}

					}
				}
			}
		}else{
			for(var i=0;i<$scope.programmes.length;i++){
				if($scope.programmes[i].isActive){
					//$scope.getItms=[];
					var getItms = $scope.programmes[i].itmModes.split(",")
					for(var k=0;k<getItms.length;k++){
						if(getItms[k]=="F" && $scope.memberId!=0){
							//$scope.memEnrollment = false;
							$scope.showEnroll=false;
							return;
						}else{
							$scope.memEnrollment = true;
							$scope.showEnroll=true;
						}
					}


				}
			}
		}
		

	}

	$scope.back=function(){
		$scope.memberDetailMode = false;
		$scope.photoUploadMode=true;
	}


	$scope.selectProgramme = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection=[];
		for (var i = 0; i < $scope.programmes.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.programmes[i].isActive) {
				$scope.selection.push(
						{"programmeId": $scope.programmes[i].programmeId
						});


			}
			else if(!$scope.programmes[i].isActive){

				$scope.selection.splice(i, 1);

			}

			if (!$scope.programmes[i].isActive) {
				$scope.allItemsSelected = false;
				return;
			}
		}

		// If not the check the "allItemsSelected" checkbox
		$scope.allItemsSelected = true;
	};

	// This executes when checkbox in table header is checked
	$scope.selectAllProgrammes = function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection=[];
		for (var i = 0; i < $scope.programmes.length; i++) {
			$scope.programmes[i].isActive = $scope.allItemsSelected;
			if ($scope.programmes[i].isActive) {
				$scope.selection.push(
						{"programmeId": $scope.programmes[i].programmeId
						});
			}
			else if(!$scope.programmes[i].isActive){
				$scope.selection.splice(i, 1);

			}
		}
	};

	$scope.addEntry=function(){
		$scope.tempBeneficiaryId=""
			$scope.famMemEntries.push({
				"familyMemFirstName":$scope.entry.familyMemFirstName,
				"familyMemLastName":$scope.entry.familyMemLastName,
				"relationId":$scope.entry.relationId,
				"relationDesc": $filter('getRelationById')($scope.relations, $scope.entry.relationId).relationDesc,
				"familyMemPic":"",
				"familyMemBioId":$scope.familyMemBioId,
				"tempFamilyMemId":Math.floor(Math.random()*89999+10000),
				"editMode": false,
				"iconStyle": "glyphicon glyphicon-pencil",
				"cancelled": false
			});
		$scope.entry=[];
		// console.log($scope.famMemEntries)
	}
	$scope.removeEntry = function(familyMemId){				
		var index = -1;		
		var memArr = eval( $scope.famMemEntries );
		for( var i = 0; i < memArr.length; i++ ) {
			if( memArr[i].familyMemId == familyMemId ) {
				index = i;
				break;
			}
		}
		if( index === -1 ) {
			alert( "Something gone wrong" );
		}
		$scope.famMemEntries.splice( index, 1 );		
	};

	$scope.updateMember = function () {
		var member = {};
		//$scope.memberEditMode = true;

		$scope.photoUploadMode=true;
//		if (!$memberValid($scope.memberNo)) {
//		logger.logWarning("Opss! You may have skipped specifying the Member's No.
//		Please try again.")
//		return false;
//		}
//		if ($scope.memberNo.length > 20) {
//		logger.logWarning("Opss!Member No is reach to maximum length of 20 ")
//		return false;
//		}

//		if (!$memberValid($scope.surName)) {
//			logger.logWarning("Opss! You may have skipped specifying the Member's surname. Please try again.")
//			return false;
//		}
		if (!$memberValid($scope.firstName)) {
			logger.logWarning("Opss! You may have skipped specifying the Card Holder's fullname. Please try again.")
			return false;
		}
		if (!$memberValid($scope.idPassPortNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Card Holder's Ration No. Please try again.")
			return false;
		}
		if (!$memberValid($scope.dateOfBirth)) {
		logger.logWarning("Opss! You may have skipped specifying the Card Holder's Birth Date. Please try again.")
		return false;
		}
		if (!$memberValid($scope.bnfGrpSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Card Holder's Group. Please try again.")
			return false;
			}
//		if (!$memberValid($scope.departmentSelect)) {
//		logger.logWarning("Opss! You may have skipped specifying the Member's
//		department. Please try again.")
//		return false;
//		}
//		if (!$memberValid($scope.familySizeSelect)) {
//		logger.logWarning("Opss! You may have skipped specifying the Member's family
//		size. Please try again.")
//		return false;
//		}
//		if (!$memberValid($scope.relationSelect)) {
//		logger.logWarning("Opss! You may have skipped specifying the Member's
//		relation. Please try again.")
//		return false;
//		}
//		if (!$memberValid($scope.categorySelect)) {
//		logger.logWarning("Opss! You may have skipped specifying the category. Please
//		try again.")
//		return false;
//		}
		$scope.tempPrgs=[];
		for(var i=0;i<$scope.programmes.length;i++){
			if($scope.programmes[i].isActive==true){
				$scope.tempPrgs.push($scope.programmes[i]);
			}
		}
		if ($scope.tempPrgs.length<=0) {
			logger.logWarning("Opss! You may have skipped specifying the programme. Please try again.")
			return false;
		}
		if (!$memberValid($scope.memberId))
			member.memberId = 0;
		else
			member.memberId=$scope.memberId ;
		member.memberNo=$scope.idPassPortNo;
		member.surName= $scope.surName;
		member.title=  $scope.title;
		member.firstName=   $scope.firstName;
		member.otherName= $scope.otherName;
		member.idPassPortNo=$scope.idPassPortNo;
		if($scope.male==true){
			member.gender="M";
		}
		else if($scope.feMale==true){
			member.gender="F";
		}
		var Dt= $filter('date')($scope.mem.dateOfBirth,'MM-dd-yyyy');
		console.log(Dt)
		member.dateOfBirth=Dt;//$scope.dateOfBirth;
		member.maritalStatus=$scope.maritalStatus;
		member.weight=$scope.weight;
		member.height=$scope.height;
		member.nhifNo=$scope.nhifNo;
		member.employerName=$scope.employerName;
		member.occupation=$scope.occupation;
		member.nationality=$scope.nationality;
		member.postalAdd=$scope.postalAdd;
		member.physicalAdd=$scope.physicalAdd;
		member.homeTelephone=$scope.homeTelephone;
		member.officeTelephone=$scope.officeTelephone;
		member.cellPhone=$scope.cellPhone;
		member.email=$scope.email;

		member.memberPic=  $scope.myCroppedImage;
		member.createdBy = $rootScope.UsrRghts.sessionId;
		member.programmes=$scope.programmes;
		member.familyMemList=$scope.famMemEntries;
		if($scope.familySize==0){
			member.familySize= 1;
		}else{
			member.familySize= $scope.familySize;
		}
	
		member.bnfGrpId=$scope.bnfGrpSelect;
		member.memType='B';
		blockUI.start()
		memberSvc.UpdMember(member).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Card Holder's information was saved succesfully")
				$scope.loadMemberData();
				$scope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
				if( $scope.male==true){
					$scope.gender="Male"; 
					// $scope.male=true;
				}
				else if( $scope.feMale==true){
					$scope.gender="Female"; 
					// $scope.feMale=true;
				}
				$scope.allItemsSelected = false;
				$scope.famMemEntries=[];
				$scope.isDisabled=false;
//				if($scope.bioId==0){
//				// $scope.showEnroll=false;
//				$scope.memberEditMode = true;
//				// $scope.memEnrollment=false;
//				$scope.isDisabled = true;
//				$scope.active = false;
//				$scope.photoUploadMode=true;
//				$scope.capPic=true;
//				$scope.showImage=true;
//				$scope.showCamera=true;
//				$scope.loadMemberData();

//				}
//				else{
//				$scope.showEnroll=true;
//				}

				// $scope.memberEditMode = true;
				// $scope.memberViewMode=false;
				// $scope.bioId=0;

			}
			else  {
				logger.logWarning(response.respMessage);
				$scope.memberDetailMode = false;
				$scope.photoUploadMode=true;
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};
//	--------------------Enrollment
	$scope.enrollMem = function () {
		$scope.memberEditMode = true;
		$scope.memEnrollment=false;
		$scope.isDisabled = true;
		$scope.active = false;
		$scope.photoUploadMode=true;
		$scope.capPic=true;
		$scope.showImage=true;
		$scope.showCamera=true;
	}

}]).factory('memberSvc', function ($http) {
	var compasMemberSvc = {};


	compasMemberSvc.UpdMember = function (member) {
		console.log(member);
		return $http({
			url: '/compas/rest/member/updCustomer',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: member
		});
	};

	compasMemberSvc.OnDeviceInit = function (socket) {
		return $http({
			url: '/compas/rest/member/socket_ops',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: socket
		});
	};//
	compasMemberSvc.GetProgrammesByBnfGrp = function (bnfGrpId) {
		return $http({
			url: '/compas/rest/member/gtProgrammesBnf/'+bnfGrpId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	compasMemberSvc.GetRelations = function () {
		return $http({
			url: '/compas/rest/member/gtRelations',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});
	};
	return compasMemberSvc;
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
}).filter('getRelationById', function () {
	return function (input, id) {
		var i = 0, len = input.length;
		for (; i < len; i++) {
			if (+input[i].relationId == +id) {
				return input[i];
			}
		}
		return null;
	}
});
