package com.compulynx.compas.dal.operations;

public class Queryconstants {
	// Login
	public static String getUserCredentialManual = "Select ID As UserID From UserMaster Where UserName = ? And UserPassword = ? and usertypeid=2";
	public static String getUserCredentialDevice = "Select dr.active,U.ID As UserID,UserFullName,market,paybill,U.agentid From UserMaster U,DeviceIssueDetails DI ,DeviceRegDetails DR,AgentMaster A  "
			+ " Where UserName =? And UserPassword =? and A.Id=U.AgentId and DI.DeviceRegId=DR.Id and serialno=? AND DR.ACTIVE=1 ";
	public static String getUserBioManual = "Select ID As UserID,UserBioID From UserMaster Where UserName = ? ";
	public static String getUserCredentialBio = "Select ID As UserID From UserMaster Where UserBioID = ?";
	public static String getSafComUser = "Select * from usermaster where username=? and userpassword=? and deviceid=? and usertype=?";
	public static String getUserGrpRights = "Select Um.ID, Um.UserName, Um.UserFullName, Ug.ID As UserGroupID, 0  As LinkId,  useremail  As LinkExtInfo ,  "
			+ " '' AS LinkName, ifnull((Select topup_limit from dtl_topup_limits where user_id=?),0) as topup_limit, "
			+ " Hm.ID As HeaderID,Hm.HeaderName,Hm.HeaderIconCss,Hm.HeaderIconColor,Rm.RightDisplayName,Rm.RightShortCode,Rm.RightViewName,Rm.RightName,Ur.AllowView,Ur.AllowEdit,Ur.AllowAdd,Ur.AllowDelete,Rm.RightMaxWidth"
			+ " From UserAssignedRights Ur Inner Join RightsMaster Rm On Ur.RightID = Rm.ID"
			+ " Inner join UserMaster Um On Um.UserGroupID = Ur.GroupId "
			+ " Inner join MenuHeaderMaster Hm on Hm.ID = Rm.RightHeaderID"
			+ " Inner Join UserGroupsMaster Ug on Ug.ID = Um.UserGroupID And Ug.ID = Ur.GroupId"
			+ " Where Um.ID = ? And Ur.AllowView = 1"
			+ " Order By Hm.HeaderPos,rightsclassid";

	// User Master
	public static String insertUser = "INSERT INTO UserMaster (UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone,UserSecretQuestionID,UserSecretQuestionAns,UserBioLogin ,UserLinkedID,UserBioID,Active,CreatedBy,CreatedOn,AgentId,locationId,usertypeid,pos_user_level) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateUser = "UPDATE UserMaster SET UserName=?,UserFullName=?,UserPassword=?,UserGroupID=?,UserEmail=?,UserPhone=?,UserSecretQuestionID=?,UserSecretQuestionAns=?,UserBioLogin=? ,UserLinkedID=?,UserBioID=?,Active=?,CreatedBy=?,CreatedOn=? ,AgentId=?,locationId=?,usertypeid=?,pos_user_level=? WHERE ID=? ";
	public static String getUserByName = "Select ID From UserMaster Where UserName =?";
	public static String getClasses = "SELECT ID,ClassName FROM S_ClassMaster";
	public static String getBranchAgents = "SELECT * FROM AGENTMASTER WHERE BRANCHID=?";
	public static String getUsers = "SELECT a.ID,UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone,AgentId,locationId"
			+ ",UserSecretQuestionID,UserSecretQuestionAns,UserBioLogin,UserLinkedID,UserBioID,a.Active,a.CreatedBy,usertypeid,"
			+ "case when usertypeid=1 then 'POS User' else 'Web User' end as userTypeName,pos_user_level"
			+ ",a.CreatedOn FROM UserMaster a,UserGroupsMaster b "
			+ "where a.UserGroupID=b.ID ";
	public static String getUserBranches = "SELECT B.BranchName,u.BranchId as BranchId,u.isActive as isChecked "
			+ " FROM UserMaster UM "
			+ "INNER JOIN UserBranchDetails U ON U.UserId=UM.ID "
			+ "right JOIN BranchMaster B ON B.ID=U.BranchId "
			+ "WHERE UM.ID=? "
			+ "union all "
			+ "SELECT B.BranchName,b.Id as BranchId,0 as isChecked "
			+ "FROM UserMaster UM, BranchMaster B where "
			+ "UM.ID=? and B.Id not in(select BranchId from UserBranchDetails WHERE UserBranchDetails.UserId=?)	";
	public static String deleteUserBranches = "DELETE FROM UserBranchDetails WHERE UserId=?";
	public static String insertUserBranchDetails = "Insert Into UserBranchDetails (UserId,BranchId,IsActive,CreatedBy,CreatedOn) Values(?,?,?,?,?)";
	public static String getUserById = "SELECT a.ID as userid,UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone"
			+ ",a.Active,a.CreatedBy"
			+ ",a.CreatedOn,b.GroupName FROM UserMaster a,UserGroupsMaster b "
			+ "where a.UserGroupID=b.ID and a.active=1 and a.id=?";
	public static String getQuestions = " SELECT Id, Question, CreatedBY, CreatedOn FROM QuestionMaster";
	public static String getGroupsByUserType = "select * from usergroupsmaster where grouptypeid=?";
	public static String getLocationAgents = "SELECT * FROM AGENTMASTER WHERE locationID=?";
	// UserGroup

	public static String insertUserGroup = "INSERT INTO UserGroupsMaster (groupcode,GroupName,grouptypeid,Active,CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?)";
	public static String updateUserGroup = "UPDATE UserGroupsMaster Set GroupName = ?,grouptypeid=?,active=?,CreatedBy = ?, CreatedOn = ? Where ID = ?";
	public static String getGroupByName = "Select ID From UserGroupsMaster Where GroupName = ? ";
	public static String getGroupById = "SELECT U.RightID,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "U.AllowView AS RightView,U.AllowAdd AS RightAdd,U.AllowEdit AS RightEdit,U.AllowDelete AS RightDelete "
			+ "FROM UserGroupsMaster G "
			+ "INNER JOIN UserAssignedRights U ON U.GroupId=G.ID "
			+ "right JOIN RightsMaster R ON R.ID=U.RightID "
			+ "WHERE G.ID=? "
			+ "union all "
			+ "SELECT r.Id,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "0 AS RightView,0 AS RightAdd,0 AS RightEdit,0 AS RightDelete "
			+ "FROM UserGroupsMaster G ,RightsMaster R "
			+ "WHERE G.ID=? and r.Id not in(select rightid from UserAssignedRights WHERE UserAssignedRights.GroupId=?)	";
	public static String getGroups = "Select ID,groupcode,GroupName,grouptypeid,Active,CreatedBy,case when grouptypeid=1 then 'POS' else 'WEB' end as grpTypeName From UserGroupsMaster";
	public static String deleteGroupRights = "DELETE FROM UserAssignedRights WHERE GroupId=?";
	public static String getGroupTypes = "select * from mst_grp_type";
	public static String getGroupByCode = "select id from UserGroupsMaster where groupcode=?";
	public static String getGroupNameByCode = "select id from UserGroupsMaster where groupname=? and  groupcode<>?";
	// Rights
	// public static String getRights =
	// "SELECT ID,RightDisplayName,AllowView,AllowAdd,AllowEdit,AllowDelete,CreatedBy,CreatedOn FROM RightsMaster ";
	public static String getRights = "SELECT ID,RightDisplayName,AllowView,AllowAdd,AllowEdit,AllowDelete,RightsClassID,CreatedBy,CreatedOn FROM RightsMaster where righttypeid=?";
	public static String insertGroupRights = "INSERT INTO UserAssignedRights (RightID,GroupId ,AllowView,AllowAdd ,AllowEdit,AllowDelete,CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateGroupRights = "UPDATE UserAssignedRights SET AllowView=?,AllowAdd=? ,AllowEdit=?,AllowDelete=?,CreatedBy=?,CreatedOn=? WHERE RightID=? AND GroupId=?";

	// Branch
	public static String getBranches = "SELECT B.Id, BranchCode, BranchName, merchantId,merchantname,classId,b.Active, B.CreatedBy, B.CreatedOn,locationId,l.location_name "
			+ "From BranchMaster B,locationmaster l, merchantmaster m where  l.id=b.locationid and m.id=b.merchantid";
	public static String insertBranches = "Insert into BranchMaster(BranchCode, BranchName,merchantId,Active,CreatedBy, CreatedOn,locationId) Values(?,?,?,?,?,?,?)";
	public static String updateBranches = "Update BranchMaster  Set  BranchName=?,merchantid=?,Active=? ,CreatedBy=?, CreatedOn=?,locationId=? Where Id=? ";
	public static String getBranchByName = "Select ID From BranchMaster Where BranchName = ? and locationid=?";
	public static String getBranchByCode = "Select ID From BranchMaster Where BranchCode = ? and merchantid=?";
	public static String getBranchNameByCode = "Select ID From BranchMaster Where branchname=? and BranchCode <> ? and merchantid=?";
	public static String getCompanies = "SELECT Id,companyCode,CompanyName, CreatedBy, CreatedOn From CompanyMaster";

	// Device Registration
	public static String insertDeviceInfo = "Insert into DeviceRegDetails(serialNo,Active,CreatedBy, CreatedOn) Values(?,?,?,?)";
	public static String getDeviceSerialNo = "Select ID From DeviceRegDetails Where SerialNo = ? ";
	public static String getDeviceSerialNoById = "Select ID From DeviceRegDetails Where SerialNo = ? and id<>?";
	public static String updateDeviceInfo = "Update DeviceRegDetails  Set serialNo=?, Active=?, CreatedBy=?, CreatedOn=? Where Id=?";
	public static String getDevices = "SELECT Id,serialNo,active, CreatedBy, CreatedOn From DeviceRegDetails";
	public static String getActiveDevices = " select DR.Id,dr.SerialNo,DR.active,DR.createdby from DeviceRegDetails DR where Active=1 and DR.Id not in (select DeviceRegId from DeviceIssueDetails DI where DI.DeviceRegId=DR.Id) ";
	public static String checkUserAssignDevice = "select id from DeviceRegDetails UG where UG.Id in (select DeviceRegId from DeviceIssueDetails UM where um.DeviceRegId=UG.Id and Ug.active=1)and ug.Id=?";

	// Issue Device
	public static String insertIssueDeviceInfo = "Insert into DeviceIssueDetails(DeviceRegId,AgentId,branchid,CreatedBy,CreatedOn,license) Values(?,?,?,?,?,?)";
	public static String updateIssueDeviceInfo = "Update DeviceIssueDetails set DeviceRegId=?, AgentId=?,BranchId=?,CreatedBy=? , CreatedOn=? where Id=?";
	public static String getIssueDevices = "select DI.Id,DI.DeviceRegId,dr.SerialNo,DI.AgentId,UM.agentDesc,DI.CreatedBy ,DI.license from DeviceIssueDetails DI, "
			+ " DeviceRegDetails DR,AgentMaster UM where DI.DeviceRegId=dr.Id and DI.AgentId=UM.Id";
	// public static String getIssueDevices =
	// "select DI.Id,DI.DeviceRegId,dr.SerialNo,DI.AgentId,UM.agentDesc,DI.CreatedBy,UM.BranchId,DI.license from DeviceIssueDetails DI, "
	// +
	// " DeviceRegDetails DR,AgentMaster UM,BranchMaster B where DI.DeviceRegId=dr.Id and DI.AgentId=UM.Id AND B.Id=UM.BranchId";
	public static String getUserDeviceSerialNo = "Select ID from DeviceIssueDetails  WHERE AgentId=? and DeviceRegId=? ";
	public static String getDeviceExists = "Select ID from DeviceIssueDetails  WHERE deviceRegId=? ";
	public static String getActiveAgents = "select A.Id,AgentDesc from AgentMaster A where Active=1 and A.Id not in (select AgentId from DeviceIssueDetails DI where DI.AgentId=A.Id and deviceregid<>-1)";

	// Services
	public static String getServices = "SELECT s.Id,c.category_name, ServiceCode, ServiceName, s.Active, s.CreatedBy, s.CreatedOn,componame,compotype,category_id,image,servicedesc,ifnull(min_price,0) as min_price,ifnull(max_price,0) as max_price FROM ServiceMaster s ,mst_category c where c.id=s.category_id";
	public static String getServiceById = "SELECT ServiceCode, ServiceName, Active, CreatedBy,CreatedOn FROM ServiceMaster WHERE ID=?";
	public static String getSubServiceById = "SELECT * FROM SubServiceMap where parentserviceid=? ";
	public static String getServiceByName = "SELECT ID FROM ServiceMaster WHERE ServiceName=?";
	public static String getServiceNameByCode = "SELECT ID FROM ServiceMaster WHERE ServiceName=? and servicecode<>?";
	public static String getServiceByCode = "SELECT ID FROM ServiceMaster WHERE ServiceCode=?";
	public static String insertServiceDetails = "INSERT INTO ServiceMaster(ServiceCode, ServiceName,compoType,componame, Active, CreatedBy,CreatedOn,category_id,image,servicedesc,min_price,max_price) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateServiceDetails = "UPDATE ServiceMaster SET ServiceName=?,compoType=?,componame=?, Active=?, CreatedBy=?, CreatedOn=?, category_id=?,image=?,servicedesc=?,min_price=?,max_price=? WHERE ID=?";
	public static String insertSubServiceDetails = "INSERT INTO SubServiceMap(ParentServiceId, SubServiceName,HasChild,level,serviceValue, Active, CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateSubServiceDetails = "UPDATE SubServiceMap SET serviceValue=?, Active=?, CreatedBy=?,CreatedOn=? WHERE ParentServiceId=?";
	public static String getParams = "SELECT Id, ParamName, Active, CreatedBy,CreatedOn FROM ParamMaster where active=1";
	public static String insertParams = "INSERT INTO ParamDetails(ParamId,serviceId, IsActive, CreatedBy,CreatedOn) Values (?,?,?,?,?)";
	public static String deleteParams = "Delete from ParamDetails where serviceId=?";
	public static String getCurrencies = "select * from mst_currency";
	public static String getUoms = "select * from mst_uom";

	// Basket
	// Params
	public static String getParamName = "Select ID From ParamMaster Where paramName = ? ";
	public static String insertParamInfo = "Insert into parammaster(paramname,Active,CreatedBy, CreatedOn) Values(?,?,?,?)";
	public static String updateParamInfo = "Update parammaster  Set paramname=?, Active=?, CreatedBy=?, CreatedOn=? Where Id=?";

	public static String getBasketServiceById = "SELECT BM.Id,BasketCode,BM.BasketName,BM.BasketStatus,BM.BasketValue,BD.ServiceId,BD.Quantity,BD.Price,SM.ServiceName"
			+ " FROM BasketMaster BM,BasketDetails BD,ServiceMaster SM "
			+ " WHERE BM.Id=BD.BasketId AND SM.Id=BD.ServiceId AND BM.ID=?";
	public static String getBaskets = " SELECT Id, BasketCode, BasketName, BasketStatus,BasketValue ,CreatedBy, CreatedOn FROM BasketMaster";
	public static String deleteBasketServices = "DELETE FROM BASKETDETAILS WHERE BASKETID=?";
	public static String insertBasketServices = "INSERT INTO BasketDetails(BasketId,ServiceId,Quantity,Price,CreatedBy,CreatedOn)VALUES(?,?,?,?,?,?)";
	public static String insertBaskets = "INSERT INTO BasketMaster(BasketCode,BasketName,BasketStatus,BasketValue,CreatedBy,CreatedOn)VALUES(?,?,?,?,?,?)";
	public static String updateBaskets = "UPDATE BasketMaster SET BasketCode=?,BasketName=?,BasketStatus=?,BasketValue=?,CreatedBy=?,CreatedOn=? WHERE Id=?";
	public static String getBasketByName = "SELECT ID FROM BasketMaster WHERE BasketName=?";

	public static String getUserServices = "select UR.RightId,RightName from UserAssignedRights UR,RightsMaster RM,UserMaster UM,UserGroupsMaster UG "
			+ " where RM.ID=UR.RightId and UG.Id=UM.UserGroupId and UM.Id=? and UR.GroupId=UG.Id and UR.AllowView='true'";

	public static String getMemberDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd,"
			+ " case bioid  when 0 then 'Not Enrolled' else 'Enrolled' end as biostatus, familysize,"
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd,bnfgrpid, case  bnfstatus when 'N' then 'Not Verified' when 'V' then 'Rejected'  else 'Rejected' end as verifystatus, "
			+ "case  bnfstatus when 'AP' then 'Approved' when 'RA' then 'Rejected' else 'Not Approved' end as approvstatus,"
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ "PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID,bnfgrp_name,topup_count "
			+ "from M_MemberMaster MM,beneficiarygroup b where b.id=mm.bnfgrpid and mm.location_id=? ";
	public static String getCustomerDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd,"
			+ " case bioid  when 0 then 'Not Enrolled' else 'Enrolled' end as biostatus, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd,bnfgrpid, case  bnfstatus when 'N' then 'Not Verified' when 'V' then 'Rejected'  else 'Rejected' end as verifystatus, "
			+ "case  bnfstatus when 'AP' then 'Approved' when 'RA' then 'Rejected' else 'Not Approved' end as approvstatus,"
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ "PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID "
			+ "from M_MemberMaster MM where mem_type='C'";
	// + "from M_MemberMaster MM  WHERE CARDSTATUS='N'";
	public static String getVerifiedMemberDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd,bnfgrpid, case  bnfstatus when 'N' then 'Not Verified' when 'V' then 'Verified'  else 'NA' end as verifystatus, "
			+ "case  bnfstatus when 'AP' then 'Approved' else 'Not Approved' end as approvstatus,"
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ "PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID "
			+ "from M_MemberMaster MM where bnfstatus='V'";
	public static String getRegMemberDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd,bnfgrpid, case  bnfstatus when 'N' then 'Not Verified' when 'V' then 'Verified'  else 'NA' end as verifystatus, "
			+ "case  bnfstatus when 'AP' then 'Approved' else 'Not Approved' end as approvstatus,"
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ "PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID "
			+ "from M_MemberMaster MM where bnfstatus='N'";
	public static String getCustomerProgrammes = " SELECT CP.ProgrammeId,P.ProgrammeDesc,CP.IsActive,"
			+ "CP.createdby,cp.programmeValue,p.itm_modes,chtm_modes,intm_modes "
			+ "FROM CustomerProgrammeDetails CP,M_MemberMaster M,ProgrammeMaster P ,BeneficiaryGroup b   "
			+ "WHERE M.ID=CP.CustomerId AND P.Id=CP.ProgrammeId AND CP.CustomerId=? and b.id=m.bnfGrpId and b.product_id=p.productId "
			+ "UNION ALL  "
			+ "SELECT P.Id,P.ProgrammeDesc,0 as IsActive,p.CreatedBy,0 as programmeValue,p.itm_modes,chtm_modes,intm_modes "
			+ "FROM M_MemberMaster M,ProgrammeMaster P,BeneficiaryGroup b  "
			+ "WHERE P.Id NOT IN (SELECT ProgrammeId FROM CustomerProgrammeDetails A WHERE A.ProgrammeId=P.Id AND A.CustomerId=M.Id) "
			+ "AND M.Id=? and b.id=m.bnfGrpId and b.product_id=p.productId";
	public static String getPorgrammesForIssueCard = "select isactive,cp.programmeId,P.ProgrammeDesc,p.createdBY,programmeValue from CustomerProgrammeDetails CP,programmeMaster P where IsActive=1 and CustomerId=? and cp.programmeId=p.id";
	public static String getCustomerBioImages = "select memberId,Image from CustomerBioImages c where memberId=?";
	public static String checkMemberexists = "SELECT Id from M_MemberMaster WHERE IdPPNo=?";
	public static String insertMemberMaster = "INSERT INTO M_MemberMaster( MemberNo, Surname, Title, Firstnames, OtherName, CategoryID, RelationID, IdPPNo, Gender, DateOfBirth, MaritalStatus, NhifNo, Height, Weight, EmployerName, Occupation, Nationality, PostalAdd, HomeAdd, HomeTeleNo, OfficeTeleNo, MobileNo, Email,Pic, CreatedBy, CreatedOn,cardstatus,familysize,bnfGrpId,bnfStatus,location_id,branch_id,active) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertAndMemberMaster = "INSERT INTO M_MemberMaster( MemberNo, Surname, Title, Firstnames, OtherName, CategoryID, RelationID, IdPPNo, Gender, DateOfBirth, MaritalStatus, NhifNo, Height, Weight, EmployerName, Occupation, Nationality, PostalAdd, HomeAdd, HomeTeleNo, OfficeTeleNo, MobileNo, Email,Pic, CreatedBy, CreatedOn,cardstatus,familysize,bnfGrpId,bnfStatus,bioid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateMemberMaster = "UPDATE M_MemberMaster SET MemberNo=?, Surname=?, Title=?, Firstnames=?, OtherName=?, CategoryID=?, RelationID=?, IdPPNo=?, Gender=?, DateOfBirth=?, MaritalStatus=?, NhifNo=?, Height=?, Weight=?, EmployerName=?, Occupation=?, Nationality=?, PostalAdd=?, HomeAdd=?, HomeTeleNo=?, OfficeTeleNo=?, MobileNo=?, Email=?, Pic=?, CreatedBy=?, CreatedOn=?,familysize=?,bnfgrpid=? WHERE ID=?";
	public static String deleteCustomerProgramme = "DELETE FROM CustomerProgrammeDetails WHERE CustomerId=?";
	public static String updateAndCustBioId = "update M_MemberMaster set bioid=? WHERE Id=?";
	public static String insertCustomerProgramme = "Insert into CustomerProgrammeDetails (customerid,programmeid,programmeValue,isactive,createdBY,createdOn) values (?,?,?,?,?,?)";
	// public static String getListOfCardsToIssue =
	// "select * from M_MemberMaster where CardStatus='N'  ";
	public static String getListOfCardsToIssue = "select * from M_MemberMaster M  "
			+ "where CardStatus='N' and bnfstatus='AP'";
	public static String getCustCardsToIssue = "select * from M_MemberMaster M  "
			+ "where CardStatus='N' and mem_type='C'";
	public static String updateCardStatus = "UPDATE M_MemberMaster SET CardStatus=? WHERE ID=? ";
	public static String updateBnfStatus = "UPDATE M_MemberMaster SET bnfstatus=? WHERE ID=? ";
	public static String updateCardIssu = "UPDATE cardissuance SET Status=? WHERE customerid=? ";
	public static String updateCustomerEnr = "UPDATE M_MemberMaster SET bioId=? WHERE ID=? ";
	public static String insertCustBioImages = "insert into CustomerBioImages(memberid,image,createdOn) values (?,?,?)";

	public static String getCardIssDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd, "
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ " PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID  "

			+ "from M_MemberMaster MM  where MM.CardStatus=? and bnfstatus='AP'";

	// + "from M_MemberMaster MM  where MM.CardStatus=?";

	public static String updateMemberCardLinkId = "UPDATE CARDISSUANCE SET memberId=? ,status='I' where cardNo=?";
	public static String getListOfCards = "SELECT id,cardNo,accountType FROM CARDISSUANCE WHERE status='N'";
	public static String getListOfCardsToPrint = "select * from CardIssuance a,M_MemberMaster b where a.MemberId=b.ID and a.status='I'";
	public static String insertTransactionDetails = "Insert into TransactionDetail (CardNo, BillNo, TxnValue,RunningBalance,subserviceid,ServiceId,DeviceId,CreatedBy,CreatedOn,ProgrammeId,paymentmethod) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateCardBalance = "update CustomerProgrammeDetails set programmevalue=? where programmeId=? and customerid in(select customerid from cardissuance where cardno=?)";
	public static String insertTransactionParamDetails = "Insert into TransactionParamDetail(txnId,paramId,ParamValue,CreatedBy,CreatedOn) values (?,?,?,?,?)";
	public static String getDashBoardDetailCount = "select 'CUSTOMERS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster M WHERE M.CARDSTATUS='I' "
			+ "UNION "
			+ " SELECT 'USERS' AS NAME,COUNT(U.ID) AS COUNTNO FROM UserMaster U "
			+ "UNION  "
			+ "SELECT 'REGISTERED DEVICES' AS NAME,COUNT(D.ID) AS COUNTNO FROM  DeviceRegDetails D "
			+ "UNION "

			+ " SELECT 'TRANSACTIONS' AS NAME,COUNT(T.ID) AS COUNTNO FROM  TransactionDetail T "
			+ "UNION "
			+ " select 'ISSUE CARDS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster  M WHERE M.CARDSTATUS='I'";

	public static String getAgentTxns = "	select sum(amount_charged_by_retailer) as amount,agentdesc as agent "
			+ "from tms_transaction_dtl td "
			+ "inner join tms_transaction_mst tm on td.trans_mst_id=tm.id "
			+ "inner join usermaster u on u.username=tm.username "
			+ "inner join agentmaster a on a.id=u.agentid "
			+ "group by agentdesc";
	public static String getTransChartDetail = "select convert(varchar,DATEPART(Year,CreatedOn))+'-'+RIGHT('0'+CAST(MONTH(CreatedOn) AS varchar(2)),2) as Month, "
			+ "(select COUNT(id) from TransactionDetail where TxnType=1) as Load, (select COUNT(id) from TransactionDetail where TxnType=2) as Purchase,COUNT(id) as totalTrans "
			+ "from TransactionDetail "
			+ "GROUP BY DATEPART(month,TransactionDetail.CreatedOn),DATEPART(Year,CreatedOn)";
	// programme
	public static String getProgrammes = "SELECT prg_type,pm.Id, ProgrammeCode, ProgrammeDesc, pm.Active, pm.CreatedBy, pm.CreatedOn,start_date,end_date,productid,itm_Modes,chtm_Modes,intm_Modes,product_name "
			+ "FROM ProgrammeMaster pm,productmaster p where p.id=pm.productid";
	public static String getProgrammeById = "SELECT ProgrammeCode, ProgrammeDesc, Active,CreatedBy,CreatedOn FROM ProgrammeMaster WHERE ID=?";
	public static String getProgrammeByName = "SELECT ID FROM ProgrammeMaster WHERE ProgrammeDesc=?";
	public static String getProgrammeByCode = "SELECT ID FROM ProgrammeMaster WHERE ProgrammeCode=?";
	public static String getProgrammeNameByCode = "SELECT ID FROM ProgrammeMaster WHERE programmedesc=? and ProgrammeCode<>?";
	public static String insertProgrammeDetails = "INSERT INTO ProgrammeMaster(ProgrammeCode, ProgrammeDesc, Active, CreatedBy,CreatedOn,productid,start_date,end_date,itm_modes,chtm_modes,intm_modes,prg_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateProgrammeDetails = "UPDATE ProgrammeMaster SET ProgrammeDesc=?, Active=?, CreatedBy=?, CreatedOn=?,start_date=?,end_date=?,itm_modes=?,chtm_modes=?,intm_modes=?,productid=? WHERE ID=?";
	public static String getServiceProducts = "SELECT * FROM servicemaster where active=1 and compotype=?";
	public static String insertProgrammeVouchers = "INSERT INTO ProgrammevoucherDetails(ProgrammeId, voucherId,frqmode, IsActive, CreatedBy,CreatedOn,frq_type) VALUES (?,?,?,?,?,?,?)";
	public static String deleteProgrammeVouchers = "DELETE FROM ProgrammevoucherDetails WHERE programmeId=?";
	// public static String getServicesByProgrammeId =
	// "SELECT PS.ServiceId,S.SubserviceName,ps.IsActive,PS.createdby FROM ProgrammeServiceDetails PS,ProgrammeMaster PM,SubServiceMap S WHERE PM.ID=PS.ProgrammeId AND IsActive=1 AND S.Id=PS.ServiceId AND PS.ProgrammeId=? union all"
	// +
	// " SELECT PS.ServiceId,S.SubserviceName,ps.IsActive,PS.createdby FROM ProgrammeServiceDetails PS,ProgrammeMaster PM,SubServiceMap S WHERE PM.ID=PS.ProgrammeId AND IsActive=0 AND S.Id=PS.ServiceId AND PS.ProgrammeId=?";
	public static String getVouchersByProgrammeId = " SELECT PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,voucher_value,frqmode,frq_type "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=? "
			+ "union all "
			+ "SELECT S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as voucher_value,'' as frqmode,'' as frq_type "
			+ " FROM ProgrammeMaster PM,Voucher_mst S "
			+ " WHERE  S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId ) AND PM.Id=?";
	public static String getCashVouchersByProgrammeId = " SELECT PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,voucher_value,frqmode,frq_type "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=? and voucher_type='CA' "
			+ "union all "
			+ "SELECT S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as voucher_value,'' as frqmode,'' as frq_type "
			+ " FROM ProgrammeMaster PM,Voucher_mst S "
			+ " WHERE  S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId ) AND PM.Id=? and voucher_type='CA'";
	// Organization
	public static String insertOrganization = "INSERT INTO OrganizationMaster (OrganizationName,CreatedBy,CreatedOn) VALUES (?,?,?)";
	public static String updateOrganization = "UPDATE OrganizationMaster SET OrganizationName = ? ,CreatedBy = ?,CreatedOn = ? WHERE ID = ?";
	public static String getOrganizationByName = "SELECT ID,OrganizationName,CreatedBy,CreatedOn FROM OrganizationMaster WHERE OrganizationName = ?";
	public static String getOrganizationById = "SELECT ID,OrganizationName,CreatedBy,CreatedOn FROM OrganizationMaster WHERE ID = ?";
	public static String getOrganizations = "SELECT ID,OrganizationName,CreatedBy,CreatedOn FROM OrganizationMaster";
	// Merchant
	public static String insertMerchant = "INSERT INTO MerchantMaster (MerchantName,Active,CreatedBy,CreatedOn,merchantcode) VALUES (?,?,?,?,?)";
	public static String updateMerchant = "UPDATE MerchantMaster SET MerchantName = ? ,Active=?,CreatedBy = ?,CreatedOn = ?,merchantcode=? WHERE ID = ? ";
	public static String getMerchantByName = "SELECT ID,MerchantName,CreatedBy,CreatedOn FROM MerchantMaster WHERE MerchantName = ? ";
	public static String getMerchantByCode = "select id from merchantmaster where merchantcode=?";
	public static String getMerchantNameByCode = "select id from merchantmaster where merchantname=? and merchantcode<>?";
	public static String getMerchantById = "SELECT ID,MerchantName,Active,CreatedBy,CreatedOn FROM MerchantMaster WHERE ID = ? ";
	public static String getMerchants = "SELECT ID,MerchantName,merchantcode,Active,CreatedBy,CreatedOn FROM MerchantMaster ";
	// Agent
	public static String getAgents = "select ration_no,vendor_type_id,a.agent_code,house_no,mobile_no,section"
			+ ",a.AgentDesc,a.Id,a.merchantId,m.MerchantName,a.active,a.locationid,l.location_name "
			+ "from AgentMaster a, MerchantMaster m,locationmaster l  "
			+ "where a.merchantId=m.Id  and a.locationid=l.id ";
	public static String getDetailsVendor="select  height,BranchName,mobileno from m_membermaster m,branchmaster b "
			+ " where m.location_id=b.locationid and m.IdPPNo=? and m.branch_id=b.id"; 
	public static String getAgentsByLoc = "select a.agent_code,a.AgentDesc,a.Id,a.merchantId,m.MerchantName,a.active,a.locationid,l.location_name "
			+ "from AgentMaster a, MerchantMaster m,locationmaster l  "
			+ "where a.merchantId=m.Id  and a.locationid=l.id and a.locationid=? ";
	public static String getAgentsByMerchant = "select a.agent_code,a.AgentDesc,a.Id,a.merchantId,m.MerchantName,a.active,a.locationid,l.location_name "
			+ "from AgentMaster a, MerchantMaster m,locationmaster l  "
			+ "where a.merchantId=m.Id  and a.locationid=l.id and m.merchantname=? ";
	public static String getAgentById = "SELECT  AgentDesc,locationId, Active, CreatedBy,CreatedOn FROM AgentMaster WHERE ID=?";
	public static String getAgentByName = "SELECT ID FROM AgentMaster WHERE agentdesc=? and locationId=?";
	public static String getAgentNameByCode = "SELECT ID FROM AgentMaster WHERE agentdesc=? and agent_code<>? and locationId=?";
	public static String getAgentByCode = "SELECT ID FROM AgentMaster WHERE agent_code=? and locationId=?";
	public static String insertAgentDetails = "INSERT INTO AgentMaster(AgentDesc,merchantId, Active, CreatedBy,CreatedOn,agent_code,locationid,ration_no,vendor_type_id,house_no,section,mobile_no) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateAgentDetails = "UPDATE AgentMaster SET AgentDesc=?,merchantId=? ,Active=?, CreatedBy=?, CreatedOn=?,locationid=?,ration_no=?,vendor_type_id=?,house_no=?,section=?,mobile_no=? WHERE ID=?";
	public static String getAgentsByLocation = "select id,agentdesc from agentmaster where locationid=? and merchantid=?";
	public static String getLocationByMerchant = "select distinct l.location_name,l.id from MerchantMaster m,branchmaster b,locationmaster l"
			+ "  where m.id=b.merchantid and l.id=b.locationid and m.id=?";

	public static String insertAgentProgramme = "INSERT INTO AgentProgrammeDetails(AgentId,ProgrammeId,IsActive, CreatedBy,CreatedOn,ProgrammeValue,currency) VALUES (?,?,?,?,?,?,?)";
	public static String deleteAgentProgramme = "DELETE FROM AgentProgrammeDetails WHERE agentId=?";
	public static String getProgrammeByAgentId = " SELECT A.ProgrammeId,P.ProgrammeDesc,A.IsActive,A.createdby,A.programmeValue,A.currency  "
			+ "FROM AgentProgrammeDetails A,AgentMaster AM,ProgrammeMaster P  "
			+ "WHERE AM.ID=A.AgentId AND P.Id=A.ProgrammeId AND A.AgentId=? "
			+ "UNION ALL "
			+ "SELECT P.Id,P.ProgrammeDesc,0 AS IsActive,P.createdby, 0 as programmeValue , '' as  currency "
			+ "FROM AgentMaster AM,ProgrammeMaster P "
			+ "WHERE P.Id NOT IN (SELECT ProgrammeId FROM AgentProgrammeDetails A WHERE A.ProgrammeId=P.Id AND A.AgentId=AM.Id) "
			+ "AND AM.Id=? ";
	// UserProgramme

	public static String getProgrammeByUserID = "SELECT AP.ProgrammeId,P.ProgrammeDesc,Ap.programmeValue,ap.currency FROM UserMaster U  "
			+ "INNER JOIN AgentMaster AG ON AG.Id=U.AGENTID "
			+ "INNER JOIN AgentProgrammeDetails AP ON AP.AgentId=U.AGENTID AND AP.AgentId=AG.Id "
			+ "INNER JOIN ProgrammeMaster P ON P.Id=AP.ProgrammeId AND P.ACTIVE=1 "
			+ "WHERE U.Id=? AND AP.ISACTIVE=1";

	public static String getServicesByProgrammes = "SELECT S.Id as parentServiceId,S.ServiceName FROM SERVICEMASTER S,ProgrammeServiceDetails P WHERE P.ServiceId=S.Id AND P.ProgrammeId=? and isactive=1";
	public static String getServiceParams = "select PD.ParamId,PM.ParamName,Isactive from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id AND Isactive=1 AND ServiceId=? "
			+ "UNION ALL "
			+ "select PD.ParamId,PM.ParamName,Isactive from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id  AND Isactive=0 AND ServiceId=?";
	public static String getActiveParams = "select PD.ParamId,PM.ParamName from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id AND Isactive=1 AND ServiceId=? AND PM.ACTIVE=1";

	// wallets
	public static String getWallets = "SELECT * FROM wallet";
	public static String insertWalletDetails = "INSERT INTO WalletDetails(walletId,orgId,isactive,CreatedBy,CreatedOn) VALUES (?,?,?,?,?)";
	public static String deleteWalletDetails = "delete from WalletDetails where orgId=?";
	// public static String getOrgWallets =
	// "select * from walletDetails WD, wallet W where isactive=1 and w.wid=wd.walletid and orgId=?";
	public static String getOrgWallets = "select * from wallet W ";

	// Bin Allocation
	public static String checkBinRangeProgramme = "SELECT ID FROM BinMaster WHERE programmeId=? ";
	public static String checkBinRange = "SELECT ID FROM BinMaster WHERE binrange=? ";
	public static String insertBinRange = "INSERT INTO BinMaster(binrange,programmeid,createdby,createdon) values (?,?,?,?) ";

	// Card Issuance
	public static String insertCardIssuance = "Insert Into CardIssuance(CardNo, SerialNO, IssueDate, ExpiryDate, status, CustomerId,ProgrammeId, CreatedBy, CreatedOn,cardpin) values (?,?,?,?,?,?,?,?,?,?)";
	public static String updateCardIssuance = "update CardIssuance set CardNo=?, SerialNO=?, IssueDate=?, ExpiryDate=?, status=?,modifiedBy=?, modifiedOn=?,cardpin=? where customerid=?";
	public static String checkCardNumber = "SELECT Id from cardIssuance WHERE cardno=?";
	public static String getBinByProgrammeId = "Select Binrange from BinMaster where ProgrammeId=?";
	public static String getLoadWalletCardDetails = "SELECT CI.LoadAmount,CI.Balance,CI.programmeId,pm.ProgrammeDesc,cp.ProgrammeValue FROM CardIssuance CI ,ProgrammeMaster PM,CustomerProgrammeDetails CP WHERE CardNo=? AND PM.Id=CI.ProgrammeId AND CP.ProgrammeId=PM.Id AND CP.ProgrammeId=CI.ProgrammeId";
	public static String updateCardLoad = "Update CardIssuance set balance=?, loadAmount=? where cardNo=?";
	public static String insertCardReIssueLog = "insert into cardreissuance_log(customerid,cardno,serialno,pinno,createdby,createdon) values (?,?,?,?,?,?)";

	// customer enroll
	public static String verifyCustomerEnroll = "SELECT id FROM M_Membermaster WHERE bioid=?";
	public static String getProgrammeValue = "select ProgrammeValue,cp.ProgrammeId,ProgrammeDesc from CardIssuance c,CustomerProgrammeDetails cp,programmemaster pm where CardNo=? and pm.id=cp.programmeid and cp.CustomerId=c.CustomerId and IsActive=1";
	public static String getBioMemberDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd, "
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ " PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, "
			+ "MM.DateOfBirth,MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID  "
			+ "from M_MemberMaster MM "

			+ "WHERE MM.bioID=? ";
	public static String getAmountDetails = "select 'ISSUE CARDS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster  M WHERE M.CARDSTATUS='A' \n"
			+ "UNION select 'TOTAL AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M \n"
			+ "UNION \n"
			+ "select 'NET AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M  WHERE paymentmethod='card' \n"
			+ "UNION \n"
			+ "select 'VOIDED AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M   ";

	public static String insertSafComTopUp = "update cardissuance set balance=balance+? where cardno=? ";
	public static String insertSafCom = "update cardissuance set balance=balance-? where cardno=? ";
	public static String insertSafComTopTxn = "Insert into TransactionDetail (CardNo,TxnValue,DeviceId,CreatedBy,CreatedOn,TXNTYPE,oldTxnId,billno,accno,channel,utility) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static String checkAgentPin = "select * from usermaster where id=? and agentpin=?";
	public static String checkCardPin = "select * from cardissuance where cardno=? and cardpin=?";
	public static String getCustName = "select Surname+ ' ' +Firstnames as name from M_MemberMaster m ,CardIssuance c where c.CustomerId=m.ID and CardNo=? ";
	public static String getCustomerId = "select id,Surname+ ' ' +Firstnames as name from m_membermaster where id=?";
	// public static String
	// checkCardBalance="select * from cardissuance where cardno=? and cardpin=?";
	public static String getAllSafComTxns = "SELECT top 5 CASE TxnType WHEN '1' THEN 'LOAD' WHEN '2' THEN 'SALE/WITHDRAWAL' WHEN '3' THEN 'REVERSE' WHEN '4' THEN 'UTILITY TXN' END as txntype, t.CardNo,TxnValue,t.DeviceId,t.CreatedOn,ifnull(utility,'NA') as utility ,u.UserFullName,case channel when 'account' then 'Account No' else 'Card' end as channel_type FROM TransactionDetail T, CardIssuance c,usermaster u where u.id=t.createdby and c.CardNo=t.CardNo and t.CardNo=? order by t.id desc";
	public static String getPOSVersion = "select value from ConfigurationParam where code='013' ";
	public static String getFTPUrl = "select value from ConfigurationParam where code='014' ";
	public static String getReverseTxn = "select txnvalue from TransactionDetail where id=? and txntype in (1,2)";
	public static String checkCard = "select * from cardissuance where cardno=? and status='A'";
	public static String changePin = "update cardissuance set cardpin=? where  cardno=? and cardpin=?";
	public static String blockCard = "update cardissuance set Status='B' where  cardno=? ";
	public static String unblockCard = "update cardissuance set Status='A' where  cardno=? ";
	public static String getAgentList = "select * from agentmaster";
	public static String getDeviceList = "select * from deviceregdetails";
	public static String validateDeviceLicense = "select i.license from deviceissuedetails I,deviceregdetails D where  i.deviceregid=d.id and d.serialno=? and license=?";
	public static String getCardBalance = "SELECT C.CardNo,C.Balance,c.serialno,c.cardpin,c.customerid FROM CardIssuance C,M_MemberMaster M WHERE M.ID=C.CustomerId AND C.CustomerId=?";
	public static String getLicense = "select * from licensemanager";
	/**
	 * Start Tms Queries Here By Elly
	 */
	public static String insert_device_stats_query = "insert into tbl_device_stats(device_serial,battery_level,"
			+ "firmware_version,kernel_version,signal_strength,latitude,longitude,agent_id,battery_status) values(?,?,?,?,?,?,?,?,?)";

	public static String insert_generated_license = "insert into LicenseManager (description,licensenumber) values(?,?)";

	public static String fetch_device_details_query = " SELECT tt.*\n"
			+ "FROM tbl_device_firmware tt\n" + "INNER JOIN\n"
			+ "    (SELECT deviceSerial, MAX(id) AS MaxID\n"
			+ "    FROM tbl_device_firmware\n"
			+ "    GROUP BY deviceSerial) groupedtt \n"
			+ "ON tt.deviceSerial = groupedtt.deviceSerial \n"
			+ "AND tt.id = groupedtt.MaxID";

	public static String fetch_device_stats_query = "SELECT tt.id,tt.device_serial,tt.battery_level,tt.firmware_version,\n"
			+ "tt.kernel_version,tt.signal_strength,CONVERT(varchar(20),tt.last_updated,120) as last_updated,tt.latitude,\n"
			+ "tt.longitude,tt.agent_id,tt.battery_status FROM tbl_device_stats tt INNER JOIN\n"
			+ "           (SELECT device_serial, MAX(id) AS MaxID\n"
			+ "            FROM tbl_device_stats\n"
			+ "            GROUP BY device_serial) groupedtt\n"
			+ "           ON tt.device_serial = groupedtt.device_serial\n"
			+ "           AND tt.id = groupedtt.MaxID";

	public static String fetch_dashboard_stats = "select COUNT(*) as value,'total_devices' as name from DeviceRegDetails --Total Devices\n"
			+ "    union\n"
			+ "    select count(distinct device_serial) as value,'total_online' as name  from tbl_device_stats where\n"
			+ "    CONVERT(VARCHAR(10),last_updated,102) = CONVERT(VARCHAR(10), GETDATE(),102) --active devices";

	// select COUNT(*) as total_devices from DeviceRegDetails --Total Devices
	// union
	// select count(distinct device_serial) as active_devices from
	// tbl_device_stats where
	// CONVERT(VARCHAR(10),last_updated,102) = CONVERT(VARCHAR(10),
	// GETDATE(),102) --active devices

	/**
	 * End Tms Queries
	 */

	// Zone
	public static String insertZone = "INSERT INTO zoneMaster (Zone_code,Zone_Name,Active,Created_By,Created_On) VALUES (?,?,?,?,?)";
	public static String updateZone = "UPDATE zoneMaster SET Zone_Name = ? ,Active=?,Created_By = ?,Created_On = ? WHERE ID = ?";
	public static String getZoneByName = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_Name = ? ";
	public static String getZones = "SELECT ID,zone_code,Zone_Name,org_Id,Active,Created_By,Created_On FROM zoneMaster";
	public static String getZoneByCode = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_code = ? ";
	public static String getZoneNameByCode = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_Name = ? and zone_code<>?";

	// area
	public static String insertArea = "INSERT INTO areaMaster (area_code,area_Name,zone_id,Active,Created_By,Created_On) VALUES (?,?,?,?,?,?)";
	public static String updateArea = "UPDATE areaMaster SET area_Name=?,zone_id=?,Active=?,Created_By = ?,Created_On = ? WHERE ID = ? ";
	public static String getAreaByName = "SELECT ID,area_code,Created_By,Created_On FROM areaMaster WHERE area_Name = ?  and zone_id=?";
	public static String getAreas = "SELECT a.ID,area_code,area_name,org_Id,zone_id,zone_name,a.Active,a.Created_By,a.Created_On "
			+ "FROM areaMaster a,zonemaster z where a.zone_id=z.id ";
	public static String getAreaByCode = "select id from areamaster where area_code=? and zone_id=?";
	public static String getAreaNameByCode = "select id from areamaster where area_name=? and area_code<>? and zone_id=?";

	// Location
	public static String getLocations = "select m.id,location_code,location_name,area_id,m.active,w.zone_id,"
			+ " case when m.active=0 then 'Inactive' else 'Active' end as status, "
			+ "w.area_name,s.zone_name from areaMaster w, locationmaster m,zonemaster s"
			+ " where w.id=m.area_id and s.id=w.zone_id";
	public static String getActiveArea = "select a.id,area_name from areamaster a , zonemaster z  where a.active=1 and z.id=a.zone_id ";
	public static String getAreaByZone = "select id,area_name from areamaster where active=1 and zone_id=?";
	public static String checkLocationName = "select id from locationmaster where location_name=? and area_id=?";
	public static String checkLocationCode = "select id from locationmaster where location_code=? and area_id=?";
	public static String checkLocationNameByCode = "select id from locationmaster where location_name=? and location_code<>? and area_id=?";
	public static String insertLocation = "insert into locationmaster(location_code,location_name,area_id,active,created_by,created_on) values (?,?,?,?,?,?)";
	public static String updateLocation = "update locationmaster set location_name=?,area_id=?,active=?,created_by=?,created_on=?  where id=?";
	// Product
	public static String insertProduct = "INSERT INTO productMaster (product_code,product_Name,merchant_id,Active,Created_By,Created_On) VALUES (?,?,?,?,?,?)";
	public static String updateProduct = "UPDATE productMaster SET product_Name=?,merchant_id=?,Active=?,Created_By = ?,Created_On = ? WHERE ID = ? ";
	public static String getProductByName = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Name = ?  and merchant_id=?";
	public static String getProductByCode = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Code = ?  and merchant_id=?";
	public static String getProductNameByCode = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Name = ? and product_code<>? and merchant_id=?";
	public static String getProducts = "SELECT p.ID,product_code,product_name,organizationId,merchant_id,merchantname,p.Active,p.Created_By,p.Created_On "
			+ "FROM productMaster p,merchantmaster m where p.merchant_id=m.id ";
	// voucher
	public static String insertVoucher = "insert into voucher_mst(voucher_code, voucher_name, voucher_type, voucher_value, active, created_by, created_on,start_date,end_date) values (?,?,?,?,?,?,?,?,?)";
	public static String updateVoucher = "update voucher_mst set voucher_name=?, voucher_type=?, voucher_value=?, active=?, created_by=?, created_on=?,start_date=?,end_date=? where id=?";
	public static String insertVoucherServices = "insert into voucher_dtl(voucher_id, service_id, quantity, service_value, isactive, created_by, created_on,service_type) values (?,?,?,?,?,?,?,?)";
	public static String deleteVoucherServices = "delete from voucher_dtl where voucher_id=?";
	public static String getVouchers = "select id, voucher_code, voucher_name,"
			+ "  voucher_type, voucher_value, "
			+ "active, created_by, created_on,start_date,end_date from voucher_mst";
	public static String getCashVouchers = "select id, voucher_code, voucher_name,"
			+ "  voucher_type, voucher_value, "
			+ "active, created_by, created_on,start_date,end_date from voucher_mst where voucher_type='CA'";
	public static String getServicesByVoucherId = " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,quantity,service_value,compoName,service_type as type  "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.voucher_Id=? and s.CompoType=ps.service_type "
			+ " union all "
			+ "SELECT S.Id,S.serviceName,0 AS IsActive,S.createdby,0 as quantity,0 as service_value,componame,s.CompoType as type "
			+ "FROM voucher_mst PM,ServiceMaster S "
			+ "WHERE  S.Id NOT IN ( SELECT Service_Id FROM voucher_dtl PS) "
			+ "AND PM.Id=? and s.CompoType in (select service_type from Voucher_dtl)  ";
	public static String getServicesPrd = " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,quantity,service_value,compoName,service_type as type  "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.service_type=? and s.CompoType=ps.service_type "
			+ " union all "
			+ "SELECT S.Id,S.serviceName,0 AS IsActive,S.createdby,0 as quantity,0 as service_value,componame,s.CompoType as type "
			+ "FROM voucher_mst PM,ServiceMaster S "
			+ "WHERE  S.Id NOT IN ( SELECT Service_Id FROM voucher_dtl PS) "
			+ "AND s.compotype=? and s.CompoType in (select service_type from Voucher_dtl)  ";
	public static String getVoucherByName = "select id from voucher_mst where voucher_name=?";
	public static String getVoucherByCode = "select id from voucher_mst where voucher_code=?";
	public static String getVoucherNameByCode = "select id from voucher_mst where voucher_name=? and voucher_code<>?";
	// beneficiary group
	public static String insertBnfGrp = "INSERT INTO BeneficiaryGroup (bnfgrp_code, bnfgrp_name, product_id, active, created_by, created_at,house_hold_value,min_cap,max_cap) VALUES (?,?,?,?,?,?,?,?,?)";
	public static String updateBnfGrp = "UPDATE BeneficiaryGroup SET bnfgrp_name=?,product_id=?,Active=?,Created_By = ?,Created_at = ?,house_hold_value=?,min_cap=?,max_cap=? WHERE ID = ? ";
	// public static String getProByName =
	// "SELECT ID,productMaster,Created_By,Created_On FROM productMaster WHERE product_Name = ?  and zone_id=?";
	public static String getBnfGrps = "SELECT p.ID,bnfgrp_code,bnfgrp_name,product_id,product_name,p.Active,p.Created_By,p.Created_at,house_hold_value,min_cap,max_cap "
			+ "FROM BeneficiaryGroup p,productmaster m where p.product_id=m.id ";
	public static String getBnfGrpByCode = "select id from BeneficiaryGroup where bnfgrp_code=? and product_id<>?";
	public static String getBnfGrpByName = "select id from BeneficiaryGroup where bnfgrp_name=? and product_id<>?";
	public static String getBnfGrpNameByCode = "select id from BeneficiaryGroup where bnfgrp_name=? and bnfgrp_code<>? and product_id<>?";
	public static String getProgrammesByBnfGrp = "SELECT p.Id, ProgrammeCode, ProgrammeDesc, p.Active, p.CreatedBy, p.CreatedOn, "
			+ "start_date,end_date,productid,itm_Modes,chtm_Modes,intm_Modes "
			+ "FROM ProgrammeMaster p ,beneficiarygroup b "
			+ "where b.product_id=p.productid and b.id=?";
	public static String getRelations = "SELECT * FROM RELATIONMASTER";
	public static String getBeneficiaryFamilyMembers = "SELECT BD.Id, BeneficiaryId, FamMemFirstName, FamMemLastName, FamRelationId, FamMemPic, FamMemBioId "
			+ " FROM BeneficiaryFamilyMembersDetails BD,m_membermaster BM "
			+ " WHERE BD.BeneficiaryId=BM.Id AND BeneficiaryId=? ";
	public static String insertBeneficiaryFamilyMemDetails = "INSERT INTO BeneficiaryFamilyMembersDetails(BeneficiaryId, FamMemFirstName, FamMemLastName, FamRelationId, FamMemPic, FamMemBioId, CreatedBy, CreatedOn) VALUES (?,?,?,?,?,?,?,?)";
	public static String deleteBeneficiaryFamilyMemDetails = "DELETE FROM BeneficiaryFamilyMembersDetails WHERE BENEFICIARYID=?";
	public static String getBranchesByMerchant = "select * from BranchMaster where MerchantId=?";
	public static String getNoOfBnfs = "select count(id) as id from m_membermaster where bnfgrpid=?";

	// TmsDownloads
	public static String getTmsUserDownloads = "select u.id,UserName,UserFullName,UserPassword,UserGroupId,pos_pin,pos_User_Level "
			+ "from UserMaster u ,AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d "
			+ "where u.AgentId=a.Id and a.Id=di.AgentId and d.Id=di.DeviceRegId and d.SerialNo=? and usertypeid=1 ";
	public static String getTmsRetailerDownload = "select AgentDesc,location_name "
			+ "from AgentMaster a,BranchMaster b,LocationMaster l,DeviceIssueDetails di,DeviceRegDetails d "
			+ "where a.branchid=b.id and l.Id=b.locationid and d.Id=di.DeviceRegId and di.AgentId=a.Id and d.SerialNo=? ";

	public static String insertTmsTransMst = "insert into tms_transaction_mst(voucher, cycle, transaction_type, value_remaining, quantity_remaining, total_amount_charged_by_retailer, pos_terminal, receipt_number, username, timestamp_transaction_created, authentication_type, latitude, longitude, created_by, created_on,uid,card_number,ration_no) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertTmsTransDtl = "insert into tms_transaction_dtl(Trans_mst_id, pos_commodity, quantity_remaining, deducted_quantity, amount_charged_by_retailer, created_by, created_at,trans_date) values (?,?,?,?,?,?,?,?)";

	public static String getVoucherDetailsByProgramme = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,\n"
			+ "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n"
			+ "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type\n"
			+ "from BeneficiaryGroup b ,ProgrammeMaster p,\n"
			+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,\n"
			+ "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n"
			+ " where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 \n"
			+ " and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId\n"
			+ "   and vm.id = vd.voucher_id and p.Id in(?)";

	public static String insert_topup = "INSERT INTO mst_topup(beneficiary_group_id,programme_id,beneficiary_id,card_number,voucher_id,item_id,item_value,item_quantity,uom,downloaded,created_on,cycle,voucher_id_number,topup_Status,voucher_type,agent_id,location_id,remarks) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String update_topup="update mst_topup m set item_value=?,downloaded=0,remarks=? where beneficiary_id=(select id from m_membermaster t where t.idppno=? ) and agent_id=? and cycle=?";
	public static String updBnfGrpTopupCount = "update beneficiarygroup set topup_count=topup_count+? where id=?";
	public static String checkRationNoCamp="select id from m_membermaster where idppno=? and location_id=?";

	// For voucher Topup
	public static String getBnfGrpForTopup = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs ,m.location_id "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m  "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and m.active=1"
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap ";
	public static String getLocForTopup="select distinct count(id) as locid from locationmaster";
	public static String getTopupedBnfGrps = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and b.id in (select beneficiary_group_id from mst_topup)"
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	public static String getBnfGrpByPrgId = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and pm.id=? and m.active=1 "
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	public static String getBnfGrpByLoc = "select m.location_id,max_cap,min_cap,bnfgrp_code,b.id as bnfgrpid,b.bnfgrp_name,ca.cash as house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m, "
			+ " dtl_bnf_household hh,dtl_cashdetails ca  "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and m.location_id=? "
			+ " and hh.bnfgrp_id=m.bnfgrpid and hh.beneficiary_id=m.id and "
			+ " m.active=1 and ca.bnfgrp_id=hh.bnfgrp_id and ca.agegrp_id=hh.agegrp_id and ca.location_id=m.location_id "
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	// public static String getLastTopupDate =
	// "select top 1 ifnull(created_on,'') as last_topup from mst_topup where beneficiary_group_id=? order by created_on desc";
	//public static String getLastTopupDate = "select ifnull(created_on,'') as last_topup, case topup_status when 'N' then 'Pending Approval' else 'Approved' end as topupstatus from mst_topup where location_id=? order by created_on desc limit 1";
	public static String getLastTopupDate = " select distinct  l.location_name,count(t.id) as no_of_bnfs,sum(item_value) topup_value,ifnull(t.created_on,'') " 
			+ "  as last_topup ,case topup_status when 'P' then 'Pending Approval' else 'Approved' end as topupstatus from mst_topup t,locationmaster l "
	+ "  where l.id=t.location_id  "
	+ "  group by agent_id,location_name desc limit ? ";
	/*
	 * public static String getBnfGrpHHDetails =
	 * "  select distinct vm.voucher_value,m.familysize,voucher_type " +
	 * " from M_MemberMaster m ,BeneficiaryGroup b," +
	 * " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
	 * +
	 * " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1 and m.active=1  "
	 * +
	 * " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type in ('VA','CA')"
	 * ;
	 */
	/*
	 * public static String getBnfGrpHHDetails =
	 * " select distinct ca.cash as voucher_value,hh.household_no as familysize,voucher_type "
	 * + " from M_MemberMaster m ,BeneficiaryGroup b, " +
	 * " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm, "
	 * + " dtl_cashdetails ca,dtl_bnf_household hh " +
	 * " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1 and m.active=1   "
	 * +
	 * " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type in ('VA','CA') "
	 * +
	 * " and ca.bnfgrp_id=m.bnfgrpid and hh.beneficiary_id=m.id and hh.bnfgrp_id=ca.bnfgrp_id "
	 * + " and ca.agegrp_id=hh.agegrp_id and ca.isactive=1 and m.location_id=?";
	 */
	public static String getBnfGrpHHDetails = "  select ca.cash,hh.household_no from dtl_cashdetails ca,dtl_bnf_household hh "
			+ " where hh.bnfgrp_id=ca.bnfgrp_id and hh.agegrp_id=ca.agegrp_id and hh.beneficiary_id=? and ca.location_id=?  ";
	public static String getBnfGrpHHDetailsTopuped = "  select distinct vm.voucher_value,m.familysize,voucher_type "
			+ " from M_MemberMaster m ,BeneficiaryGroup b,"
			+ " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
			+ " where b.id=m.bnfGrpId and m.active=1 and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1  and b.id in(select beneficiary_group_id from mst_topup) "
			+ " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type in ('VA','CA')";
	/*
	 * public static String getBnfHHDetails =
	 * "select distinct vm.voucher_value,m.familysize,b.min_cap,b.max_cap " +
	 * " from M_MemberMaster m ,BeneficiaryGroup b," +
	 * " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
	 * +
	 * " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.isactive=1 "
	 * +
	 * " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type=? and pm.id=?"
	 * ;
	 */
	/*
	 * public static String getBnfHHDetails =
	 * " select distinct ca.cash as voucher_value,hh.household_no as familysize,voucher_type "
	 * + " from M_MemberMaster m ,BeneficiaryGroup b, " +
	 * " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm, "
	 * + " dtl_cashdetails ca,dtl_bnf_household hh " +
	 * " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1 and m.active=1 "
	 * +
	 * " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type=? "
	 * +
	 * " and ca.bnfgrp_id=m.bnfgrpid and hh.beneficiary_id=m.id and hh.bnfgrp_id=ca.bnfgrp_id "
	 * + " and ca.agegrp_id=hh.agegrp_id and ca.isactive=1 and pm.id=?";
	 */
	public static String getBnfHHDetails = " select distinct ca.cash as voucher_value,hh.household_no as familysize,voucher_type,m.id,bnfgrpid,ca.agegrp_id "
			+ " from M_MemberMaster m ,BeneficiaryGroup b, "
			+ "ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm, "
			+ " dtl_cashdetails ca,dtl_bnf_household hh "
			+ " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1 and m.active=1 "
			+ " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type=? "
			+ "and ca.bnfgrp_id=m.bnfgrpid and hh.beneficiary_id=m.id and hh.bnfgrp_id=ca.bnfgrp_id "
			+ "and ca.agegrp_id=hh.agegrp_id and ca.isactive=1 and pm.id=?  and ca.location_id=m.location_id "
			+ "and hh.bnfgrp_id=m.bnfgrpid and m.location_id=? and household_no>0 ";
	public static String updateHouseHoldValue = "update BeneficiaryGroup set old_value=house_hold_value, house_hold_value=? where id=?";
	public static String getTopupDetailsToApprove = "{call PRC_GetTopupDetails (?,?,?)}";
	public static String getTopupApprovalDtl = "select bnfgrp_name,count(m.id) as bnfcount from beneficiarygroup b,m_membermaster m where b.id=? and m.bnfgrpid=b.id and m.location_id=?";
	public static String getBeneficiaryCount = "select COUNT(id) bnfCount from M_MemberMaster m where bnfGrpId=? and m.active=1 and location_id=?";
	public static String getBeneficiaryId = "select m.id from M_MemberMaster m where bnfGrpId=? and location_id=? and m.active=1";

	// category
	public static String insertCategory = "insert into mst_category(category_code, category_name, active, created_by, created_on) values (?,?,?,?,?)";
	public static String updateCategory = "UPDATE mst_category SET category_name = ? ,Active=?,Created_By = ?,Created_On = ? WHERE ID = ? ";
	public static String getCategoryByName = "SELECT ID,category_name,Created_By,Created_On FROM mst_category WHERE category_name = ? ";
	public static String getCategoryByCode = "select id from mst_category where category_code=?";
	public static String getCategoryNameByCode = "select id from mst_category where category_name=? and category_code<>?";
	public static String getCategoryById = "SELECT ID,category_name,Active,Created_By,Created_On FROM mst_category WHERE ID = ? ";
	public static String getCategories = "SELECT ID,category_name,category_code,Active,Created_By,Created_On FROM mst_category ";

	// download data for android device
	/*
	 * public static String getAndUsers =
	 * "select u.Id,UserName,UserFullName,UserPassword,pos_user_level  " +
	 * "from UserMaster u,DeviceRegDetails dr,DeviceIssueDetails di  " +
	 * "where UserTypeId=1  and di.AgentId=u.AgentId and di.DeviceRegId=dr.Id "
	 * + "and dr.SerialNo=? and u.active=1";
	 */
	public static String getAndUsers = "select u.Id,UserName,UserFullName,UserPassword,pos_user_level ,a.locationid "
			+ " from UserMaster u,DeviceRegDetails dr,DeviceIssueDetails di ,agentmaster a  "
			+ " where UserTypeId=1  and di.AgentId=u.AgentId and di.DeviceRegId=dr.Id "
			+ " and dr.SerialNo=? and u.active=1 and a.id=di.agentid ";
	public static String getAndCategories = "select * from mst_category where active=1";
	public static String getAndBnfGrps = "select * from BeneficiaryGroup where active=1";
	public static String getAndColorCodes = "select * from mst_color_pin where active=1";
	// public static String getAndProducts =
	// "select * from servicemaster where active=1";

	public static String getAndProducts = "select distinct p.service_id,s.servicename,s.servicedesc,s.ServiceCode,s.CompoType,s.CompoName, "
			+ " category_id,image,location_id  "
			+ "from servicemaster s ,dtl_priceconfig p "
			+ "where active=1 and p.service_id=s.id and p.location_id=?";
	public static String getAndPriceDetails = "select * from dtl_priceconfig where service_id=? ";

	public static String getAndroidProgrammes = "SELECT prg_type,pm.Id, ProgrammeCode, ProgrammeDesc, pm.Active, pm.CreatedBy, "
			+ "pm.CreatedOn,start_date,end_date,productid,itm_Modes,chtm_Modes,intm_Modes,product_name,b.id as bnfgrpid "
			+ "FROM ProgrammeMaster pm,productmaster p,BeneficiaryGroup b  "
			+ "where p.id=pm.productid and b.product_id=p.id and b.product_id=pm.productId ";
	public static String getAndroidVouchers = " SELECT PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,"
			+ "voucher_value,frqmode,frq_type,s.start_date,s.end_date "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=?  and ps.isactive=1";
	public static String getAndroidProducts = " SELECT servicecode,PS.Service_Id,S.serviceName,image,"
			+ "ps.IsActive,PS.created_by,quantity,service_value,compoName,servicedesc,category_id "
			+ "FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S  "
			+ "WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.voucher_Id=? and ps.isactive=1 "
			+ "and s.CompoType=ps.service_type ";
	public static String getAndroidTopup = "select cycle,mt.id as topupid,mt.programme_id,mt.beneficiary_id,mt.card_number,beneficiary_group_id, "
			+ "mt.voucher_id,mt.item_id,mt.item_value,mt.item_quantity,mt.uom,mt.voucher_type,voucher_id_number "
			+ "from mst_topup mt,ProgrammeMaster pm,ProductMaster p, "
			+ "MerchantMaster m,AgentMaster a,DeviceIssueDetails di,DeviceRegDetails dr,usermaster u "
			+ "where pm.Id=mt.programme_id and p.id=pm.productId "
			+ "and m.Id=p.merchant_id and a.merchantId=m.Id and di.AgentId=a.Id and u.agentid=a.id "
			+ "and dr.Id=di.DeviceRegId and dr.SerialNo=? and mt.agent_id=di.agentid and topup_status='A' and downloaded=0 and pos_user_level=0";
	// public static String
	// updateAndDwonloadStatus="update mst_topup set downloaded=1 where id=? and topup_status='A'";
	public static String getFlowChartDetails = "select sm.ServiceName as service,count(bm.id) as trans_count  from Servicemaster sm inner join tms_transaction_dtl bm on bm.pos_commodity = sm.Id GROUP BY sm.ServiceName order by trans_count desc";
	public static String getTotalCollectionInfo =
	// "select ifnull(sum(a.value),0) as collection,'Value Voucher Collections' as name from (select sum(amount_charged_by_retailer) value,m.voucher_type  "
	// + "from tms_transaction_dtl td "
	// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id "
	// + "left join mst_topup m on m.voucher_id_number=tm.voucher and "
	// + "m.voucher_type='VA' and m.item_id=td.pos_commodity  "
	// +
	// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where a.voucher_type is not null  "
	// + "union "
	// +
	// "select ifnull(sum(a.value),0) as collection,'Commodity Voucher Collections' as name from (select sum(amount_charged_by_retailer) value,m.voucher_type "
	// + "from tms_transaction_dtl td "
	// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id  "
	// + "left join mst_topup m on m.voucher_id_number=tm.voucher and  "
	// + "m.voucher_type='CM' and m.item_id=td.pos_commodity  "
	// +
	// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where a.voucher_type is not null "
	"select sum(amount_charged_by_retailer) collection,'Value Voucher Collections' as name from tms_transaction_dtl td ";
	// + "union  "
	// +
	// "select ifnull(sum(a.value),0) as collection,'Cash Voucher Collections' as name from (select sum(amount_charged_by_retailer) value,m.voucher_type  "
	// + "from tms_transaction_dtl td "
	// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id  "
	// + "left join mst_topup m on m.voucher_id_number=tm.voucher and "
	// + "m.voucher_type='CA' and m.item_id=td.pos_commodity  "
	// +
	// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where a.voucher_type is not null ";
	/*
	 * public static String getAndroidBeneficiary =
	 * "select m.id,m.createdby,m.createdon,ci.CardNo,ci.SerialNO,ci.cardpin,cp.ProgrammeId,memberno,"
	 * +
	 * " surname,title,firstnames,othername,mobileno,idppno,gender,dateofbirth,maritalStatus,nationality,email,pic,familysize,bnfgrpid "
	 * + "from M_MemberMaster M, CardIssuance ci,CustomerProgrammeDetails cp " +
	 * "where ci.CustomerId=m.ID and cp.CustomerId=m.ID and cp.IsActive=1 and m.active=1"
	 * ;
	 */
	public static String getAndroidBeneficiary = " select m.id,m.createdby,m.createdon,ci.CardNo,ci.SerialNO,ci.cardpin,cp.ProgrammeId,memberno, "
			+ " surname,title,firstnames,othername,mobileno,idppno,gender,dateofbirth,maritalStatus,nationality,email,pic,familysize,bnfgrpid  "
			+ " from M_MemberMaster M, CardIssuance ci,CustomerProgrammeDetails cp ,agentmaster a,deviceissuedetails di, "
			+ " deviceregdetails dr "
			+ " where ci.CustomerId=m.ID and cp.CustomerId=m.ID and cp.IsActive=1 and m.active=1 "
			+ " and a.locationid=m.location_id and di.AgentId=a.id and dr.id=di.DeviceRegId and dr.SerialNo=? ";
	public static String checkTopupCycle = "select distinct cycle from mst_topup where agent_id=? and beneficiary_group_id=? and topup_status='P' and location_id=?";
	public static String checkTopupCycleDownloaded = "select distinct cycle from mst_topup where agent_id=? and beneficiary_group_id=? and topup_status='A' and downloaded=0 and location_id=?";

	// AgeGroups
	public static String getAgeGroups = "select * from mst_agegroup";
	public static String insertBnfGrpAgeGroupInfo = "insert into  dtl_bnfgrp_agegrp (bnfgrp_id,agegrp_id,isactive,created_by,created_on) values (?,?,?,?,?)";
	public static String getAgeGrpsByBnfGrps = "SELECT distinct ma.id,ma.agegrp_name,isactive FROM dtl_bnfgrp_agegrp bg,mst_agegroup ma where bnfgrp_id=? and bg.isactive=1";
	public static String insertLocCashAgeGrp = "insert into dtl_cashdetails (bnfgrp_id,location_id,isactive,agegrp_id,cash,created_by,created_on) values (?,?,?,?,?,?,?)";
	public static String deleteLocCashDtl = "delete from dtl_cashdetails where bnfgrp_id=?";
	public static String getCashDetialsByLoc = "SELECT distinct ma.id,ma.agegrp_name,ca.isactive ,cash,location_id,bg.bnfgrp_id "
			+ "	FROM dtl_bnfgrp_agegrp bg,mst_agegroup ma,dtl_cashdetails ca "
			+ " where bg.bnfgrp_id=? and bg.isactive=1 and ma.id=bg.agegrp_id "
			+ " and ca.agegrp_id=bg.agegrp_id and location_id=? and ca.bnfgrp_id=bg.bnfgrp_id "
			+ " union all "
			+ " SELECT distinct ma.id,ma.agegrp_name,false as isactive ,0 as cash,0 as location_id, bg.bnfgrp_id "
			+ "FROM dtl_bnfgrp_agegrp bg,mst_agegroup ma "
			+ " where bg.bnfgrp_id=? and bg.isactive=1 and ma.id=bg.agegrp_id "
			+ "  and bg.bnfgrp_id not in (select bnfgrp_id from dtl_cashdetails where location_id=?) ";
	public static String getAgeGroupsByBnfGrp = "select agegrp_id from dtl_bnfgrp_agegrp where isactive=1";
	public static String insertBnfHHdetials = "insert into dtl_bnf_household(beneficiary_id,bnfgrp_id,agegrp_id,household_no,created_by,created_on,location_id) values (?,?,?,?,?,?,?)";
	public static String getHHDetailsForImport = "SELECT id FROM m_membermaster m where m.IdPPNo=? and location_id=?";
	public static String updateHHDetails = "update dtl_bnf_household set household_no=? where agegrp_id=? and beneficiary_id=? and location_id=?";

	// Price Cofiguration
	public static String getVoucherServices = "select pv.voucherId,vm.voucher_name from programmevoucherdetails pv "
			+ " inner join voucher_mst vm on vm.id=pv.voucherId  "
			+ " and ProgrammeId=? and pv.IsActive=1 "
			+ " and vm.id not in (select distinct voucher_id from dtl_priceconfig where location_id=?) "
			+ " union all "
			+ " select distinct pv.voucherId,vm.voucher_name from programmevoucherdetails pv "
			+ " inner join voucher_mst vm on vm.id=pv.voucherId  "
			+ " inner join dtl_priceconfig dl on dl.voucher_id=pv.voucherId and dl.voucher_id=vm.id "
			+ " and ProgrammeId=? and pv.IsActive=1 and location_id=? ";
	public static String getServicesForPriceConfig = " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,0 as unit,0 as min_price, 0 as max_price,0 as service_value,compoName,service_type as type "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id and s.CompoType=ps.service_type  and ps.voucher_id not in ( "
			+ " select voucher_id from dtl_priceconfig where voucher_id=? and location_id=?)  "
			+ " union all "
			+ " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,dl.unit ,dl.min_price,dl.max_price,0 as service_value,compoName,service_type as type "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S,dtl_priceconfig dl "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.voucher_Id=? and s.CompoType=ps.service_type "
			+ " and dl.voucher_id=pm.id and dl.voucher_id=ps.voucher_id and dl.service_id=ps.service_id and location_id=?";
	public static String insertPriceConfig = "insert into dtl_priceconfig(location_id,service_id,uom,quantity,max_price,created_by,created_on) values (?,?,?,?,?,?,?)";
	public static String deletePriceConfig = "delete from dtl_priceconfig where service_id=? ";
	public static String checkDeviceRegister = "select id from deviceregdetails where serialno=?";
	
	//Topup Limits
	public static String getTopupLimits="select l.id,topup_limit,userfullname,user_id from dtl_topup_limits l,usermaster u where u.id=l.user_id";
	public static String checkLimitExists="select id from dtl_topup_limits where user_id=?";
	public static String insertLimitDetails="insert into dtl_topup_limits(user_id,topup_limit,created_by,created_on) values (?,?,?,?)";
	public static String updLimitDetails="update  dtl_topup_limits set topup_limit=? ,created_by=?,created_on=? where user_id=?";
	public static String getWebUsers="select id,userfullname from usermaster where usertypeid=2";
	
	public static String getPenNotificationDetails="select distinct sum(item_value) topup_value,location_name,t.created_on from mst_topup t,locationmaster l "
			+ " where topup_status='P' and l.id=t.location_id "
			+ " group by agent_id desc limit 1" ;
	public static String getCountNoti="select COUNT(id) as pendingCount from mst_topup where topup_status='P' desc limit 1"; 
	public static String getVendorDevice="select serialno from deviceissuedetails di,deviceregdetails dr where AgentId=? and dr.Id=di.DeviceRegId";
};