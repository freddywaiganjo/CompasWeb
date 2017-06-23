package com.compulynx.compas.dal.impl;

import com.compulynx.compas.dal.CustomerDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.dal.operations.Utility;
import com.compulynx.compas.models.*;
import com.google.gson.Gson;
/*import com.innovatrics.idkit.IDKit;
import com.innovatrics.idkit.IDKit.FingerPosition;
import com.innovatrics.idkit.IDKitException;
import com.innovatrics.idkit.User;*/

import javax.sql.DataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class CustomerDalImpl implements CustomerDal {

	private static final Logger logger = Logger.getLogger(CustomerDalImpl.class
			.getName());

	private DataSource dataSource;

	public CustomerDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public long generateNumber() {
		return (long) (Math.random() * 100000 + 3333300000L);
	}

	public List<Customer> GetMembers(int locationId) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count=0;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMemberDetails);
preparedStatement.setInt(1, locationId);
			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				count++;
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				objMember.bnfGrpId = resultSet.getInt("bnfgrpid");
				objMember.verifyStatus = resultSet.getString("verifyStatus");
				objMember.approvStatus = resultSet.getString("approvStatus");
				objMember.bioStatus = resultSet.getString("bioStatus");
				objMember.topupCount=resultSet.getInt("topup_count");
				objMember.bnfGrpName=resultSet.getString("bnfgrp_name");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				objMember.count=count;
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,resultSet2.getString("itm_modes"),
							resultSet2.getString("chtm_modes"),
							resultSet2.getString("intm_modes")));
				}
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				members.add(objMember);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getBeneficiaryFamilyMembers);
				preparedStatement.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					familyMembers.add(new BeneficiaryFamilyMembers(resultSet2
							.getInt("ID"), resultSet2
							.getString("FamMemFirstName"), resultSet2
							.getString("FamMemLastName"), resultSet2
							.getInt("famRelationId"), resultSet2
							.getString("FamMemPic"), resultSet2
							.getInt("FamMemBioId"), 200));
				}
				objMember.familyMemList = familyMembers;
				objMember.familySize=resultSet.getInt("familysize");
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Customer> GetCustomers() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCustomerDetails);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				objMember.bnfGrpId = resultSet.getInt("bnfgrpid");
				objMember.verifyStatus = resultSet.getString("verifyStatus");
				objMember.approvStatus = resultSet.getString("approvStatus");
				objMember.bioStatus = resultSet.getString("bioStatus");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,resultSet2.getString("itm_modes"),
							resultSet2.getString("chtm_modes"),
							resultSet2.getString("intm_modes")));
				}
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				members.add(objMember);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getBeneficiaryFamilyMembers);
				preparedStatement.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					familyMembers.add(new BeneficiaryFamilyMembers(resultSet2
							.getInt("ID"), resultSet2
							.getString("FamMemFirstName"), resultSet2
							.getString("FamMemLastName"), resultSet2
							.getInt("famRelationId"), resultSet2
							.getString("FamMemPic"), resultSet2
							.getInt("FamMemBioId"), 200));
				}
				objMember.familyMemList = familyMembers;
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> GetVerifiedMembers() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVerifiedMemberDetails);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				objMember.bnfGrpId = resultSet.getInt("bnfgrpid");
				objMember.verifyStatus = resultSet.getString("verifyStatus");
				objMember.approvStatus = resultSet.getString("approvStatus");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,resultSet2.getString("itm_modes"),
							resultSet2.getString("chtm_modes"),
							resultSet2.getString("intm_modes")));
				}
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				members.add(objMember);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getBeneficiaryFamilyMembers);
				preparedStatement.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					familyMembers.add(new BeneficiaryFamilyMembers(resultSet2
							.getInt("ID"), resultSet2
							.getString("FamMemFirstName"), resultSet2
							.getString("FamMemLastName"), resultSet2
							.getInt("famRelationId"), resultSet2
							.getString("FamMemPic"), resultSet2
							.getInt("FamMemBioId"), 200));
				}
				objMember.isChecked=false;
				objMember.familyMemList = familyMembers;
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> GetRegMembers() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRegMemberDetails);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				objMember.bnfGrpId = resultSet.getInt("bnfgrpid");
				objMember.verifyStatus = resultSet.getString("verifyStatus");
				objMember.approvStatus = resultSet.getString("approvStatus");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,resultSet2.getString("itm_modes"),
							resultSet2.getString("chtm_modes"),
							resultSet2.getString("intm_modes")));
				}
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				members.add(objMember);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getBeneficiaryFamilyMembers);
				preparedStatement.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					familyMembers.add(new BeneficiaryFamilyMembers(resultSet2
							.getInt("ID"), resultSet2
							.getString("FamMemFirstName"), resultSet2
							.getString("FamMemLastName"), resultSet2
							.getInt("famRelationId"), resultSet2
							.getString("FamMemPic"), resultSet2
							.getInt("FamMemBioId"), 200));
				}
				objMember.isChecked=false;
				objMember.familyMemList = familyMembers;
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckMemberExists(String memberNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMemberexists);
			preparedStatement.setString(1, memberNo);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateMember(Customer member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String example = "SGVsbG8gV29ybGQ=";
			byte[] decoded = org.apache.commons.codec.binary.Base64
					.decodeBase64(example.getBytes());
			// System.out.println(decoded);
			if (member.memberId == 0) {
				//if (GetMemberByNo(member.idPassPortNo)) {
					//return new CompasResponse(201,
					//		"Beneficiary Already Exists With Entered UHNCR ID");
				//} else {

					preparedStatement = connection.prepareStatement(
							Queryconstants.insertMemberMaster,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, member.memberNo);
					// preparedStatement.setInt(2,
					// member.departmentId);
					preparedStatement.setString(2, member.surName);
					preparedStatement.setString(3, member.title);
					preparedStatement.setString(4, member.firstName);
					preparedStatement.setString(5, member.otherName);
					preparedStatement.setInt(6, 0);
					preparedStatement.setInt(7, 0);
					preparedStatement.setString(8, member.idPassPortNo);
					preparedStatement.setString(9, member.gender);
					// System.out.println("memberdob" + member.dateOfBirth);
					preparedStatement.setString(10, member.dateOfBirth);
					preparedStatement.setString(11, member.maritalStatus);
					preparedStatement.setString(12, member.nhifNo);
					preparedStatement.setString(13, member.height);
					preparedStatement.setString(14, member.weight);
					preparedStatement.setString(15, member.employerName);
					preparedStatement.setString(16, member.occupation);
					preparedStatement.setString(17, member.nationality);
					preparedStatement.setString(18, member.postalAdd);
					preparedStatement.setString(19, member.physicalAdd);
					preparedStatement.setString(20, member.homeTelephone);
					preparedStatement.setString(21, member.officeTelephone);
					preparedStatement.setString(22, member.cellPhone);
					preparedStatement.setString(23, member.email);
//					preparedStatement.setString(24, member.nokName);
//					preparedStatement.setString(25, member.nokIdPpNo);
//					preparedStatement.setString(26, member.nokTelephoneNo);
//					preparedStatement.setString(27, member.nokPostalAdd);

					// preparedStatement.setInt(7,
					// member.familySizeId);

					// preparedStatement.setString(9,
					// member.parentMemberNo);
					preparedStatement.setString(24, member.memberPic);
					preparedStatement.setInt(25, member.createdBy);
					preparedStatement.setTimestamp(26, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(27, "N");
					preparedStatement.setInt(28, member.familySize);
					preparedStatement.setInt(29, member.bnfGrpId);
					preparedStatement.setString(30, "N");
					

				//}
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateMemberMaster);

				preparedStatement.setString(1, member.memberNo);
				// preparedStatement.setInt(2,
				// member.departmentId);
				preparedStatement.setString(2, member.surName);
				preparedStatement.setString(3, member.title);
				preparedStatement.setString(4, member.firstName);
				preparedStatement.setString(5, member.otherName);
				preparedStatement.setInt(6, 0);
				preparedStatement.setInt(7, 0);
				preparedStatement.setString(8, member.idPassPortNo);
				preparedStatement.setString(9, member.gender);
				// System.out.println("memberdob" + member.dateOfBirth);
				preparedStatement.setString(10, member.dateOfBirth);
				preparedStatement.setString(11, member.maritalStatus);
				preparedStatement.setString(12, member.nhifNo);
				preparedStatement.setString(13, member.height);
				preparedStatement.setString(14, member.weight);
				preparedStatement.setString(15, member.employerName);
				preparedStatement.setString(16, member.occupation);
				preparedStatement.setString(17, member.nationality);
				preparedStatement.setString(18, member.postalAdd);
				preparedStatement.setString(19, member.physicalAdd);
				preparedStatement.setString(20, member.homeTelephone);
				preparedStatement.setString(21, member.officeTelephone);
				preparedStatement.setString(22, member.cellPhone);
				preparedStatement.setString(23, member.email);

				preparedStatement.setString(24, member.memberPic);
				preparedStatement.setInt(25, member.createdBy);
				preparedStatement.setTimestamp(26, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(27, member.familySize);
				preparedStatement.setInt(28, member.bnfGrpId);
				preparedStatement.setInt(29, member.memberId);
				
				customerId = member.memberId;

			}
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (member.memberId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					customerId = rs.getInt(1);
				}
				//member.memberId=customerId;
				member.cardNumber= "123456" + generateNumber();
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteCustomerProgramme);
				preparedStatement.setInt(1, customerId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				if(member.memberId==0){
				if (checkCardNumber(member.cardNumber)) {
					return new CompasResponse(201, "Crad Number Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCardIssuance);
				preparedStatement.setString(1, member.cardNumber);
				preparedStatement.setString(2, "1234567890");
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(4, member.expiryDate);
				preparedStatement.setString(5, "I");
				preparedStatement.setInt(6, customerId);
				preparedStatement.setInt(7, member.programmeId);
				preparedStatement.setInt(8, member.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(10, "1234");
				if (preparedStatement.executeUpdate() > 0) {
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateCardStatus);
					preparedStatement.setString(1, "I");
					preparedStatement.setInt(2, customerId);
				}
				preparedStatement.executeUpdate();
				}
				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCustomerProgramme);
				for (int i = 0; i < member.programmes.size(); i++) {
					preparedStatement.setInt(1, customerId);
					preparedStatement.setInt(2,
							member.programmes.get(i).programmeId);
					preparedStatement.setDouble(3,
							member.programmes.get(i).programmeValue);
					preparedStatement.setBoolean(4,
							member.programmes.get(i).isActive);
					preparedStatement.setInt(5,
							member.programmes.get(i).createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Programme Id "
								+ member.programmes.get(i).programmeId);
					}
				}

				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteBeneficiaryFamilyMemDetails);
				preparedStatement.setInt(1, customerId);
				preparedStatement.executeUpdate();
				DBOperations.DisposeSql(preparedStatement);
				// insert familyMembers
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertBeneficiaryFamilyMemDetails);
				for (int i = 0; i < member.familyMemList.size(); i++) {
					preparedStatement.setInt(1, customerId);
					preparedStatement.setString(2,
							member.familyMemList.get(i).familyMemFirstName);
					preparedStatement.setString(3,
							member.familyMemList.get(i).familyMemLastName);
					preparedStatement.setInt(4,
							member.familyMemList.get(i).relationId);
					preparedStatement.setString(5,
							member.familyMemList.get(i).familyMemPic);
					preparedStatement.setInt(6,
							member.familyMemList.get(i).familyMemBioId);
					preparedStatement.setInt(7,
							member.familyMemList.get(i).createdBy);
					preparedStatement.setTimestamp(8, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception(
								"Failed to Insert Family Member Id Id "
										+ member.familyMemList.get(i).beneficiaryId);
					}
				}
				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Sql Exception Occured");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public CompasResponse UpdateAndMember(List<Customer> member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			System.out.println("Beneficiary coming from Android device##"+member.size());
			//connection.setAutoCommit(false);
			String example = "SGVsbG8gV29ybGQ=";
			byte[] decoded = org.apache.commons.codec.binary.Base64
					.decodeBase64(example.getBytes());
			// System.out.println(decoded);
			
			for(int i=0;i<member.size();i++){
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertAndMemberMaster,
							Statement.RETURN_GENERATED_KEYS);
					System.out.println("MEMBERNO##"+ member.get(i).memberNo);
					preparedStatement.setString(1, member.get(i).memberNo);
					// preparedStatement.setInt(2,
					// member.departmentId);
					preparedStatement.setString(2, member.get(i).surName);
					preparedStatement.setString(3, member.get(i).title);
					preparedStatement.setString(4, member.get(i).firstName);
					preparedStatement.setString(5, member.get(i).otherName);
					preparedStatement.setInt(6, 0);
					preparedStatement.setInt(7, 0);
					preparedStatement.setString(8, member.get(i).idPassPortNo);
					preparedStatement.setString(9, member.get(i).gender);
					// System.out.println("memberdob" + member.dateOfBirth);
					preparedStatement.setString(10, member.get(i).dateOfBirth);
					preparedStatement.setString(11, member.get(i).maritalStatus);
					preparedStatement.setString(12, member.get(i).nhifNo);
					preparedStatement.setString(13, member.get(i).height);
					preparedStatement.setString(14, member.get(i).weight);
					preparedStatement.setString(15, member.get(i).employerName);
					preparedStatement.setString(16, member.get(i).occupation);
					preparedStatement.setString(17, member.get(i).nationality);
					preparedStatement.setString(18, member.get(i).postalAdd);
					preparedStatement.setString(19, member.get(i).physicalAdd);
					preparedStatement.setString(20, member.get(i).homeTelephone);
					preparedStatement.setString(21, member.get(i).officeTelephone);
					preparedStatement.setString(22, member.get(i).cellPhone);
					preparedStatement.setString(23, member.get(i).email);

					preparedStatement.setString(24, "data:image/png;base64,"+member.get(i).memberPic);
					preparedStatement.setInt(25, member.get(i).createdBy);
					preparedStatement.setTimestamp(26, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(27, "N");
					preparedStatement.setInt(28, member.get(i).familySize);
					preparedStatement.setInt(29, member.get(i).bnfGrpId);
					preparedStatement.setString(30, "N");
					preparedStatement.setInt(31, customerId);
					

				
			if (preparedStatement.executeUpdate() > 0) {

					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					customerId = rs.getInt(1);
					String cardNumber= "447744" + generateNumber();
					DBOperations.DisposeSql(preparedStatement);
					
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertCardIssuance);
					preparedStatement.setString(1,cardNumber);
					preparedStatement.setString(2, "1234567890");
					preparedStatement.setTimestamp(3, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(4, member.get(i).expiryDate);
					preparedStatement.setString(5, "I");
					preparedStatement.setInt(6, customerId);
					preparedStatement.setInt(7, member.get(i).programmeId);
					preparedStatement.setInt(8, member.get(i).createdBy);
					preparedStatement.setTimestamp(9, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(10, "1234");
					if (preparedStatement.executeUpdate() > 0) {
						DBOperations.DisposeSql(preparedStatement);
						preparedStatement = connection
								.prepareStatement(Queryconstants.updateCardStatus);
						preparedStatement.setString(1, "I");
						preparedStatement.setInt(2, customerId);
					}
					preparedStatement.executeUpdate();
					
					DBOperations.DisposeSql(preparedStatement);
					
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateAndCustBioId);
					preparedStatement.setInt(1, customerId);
					preparedStatement.setInt(2, customerId);
					preparedStatement.executeUpdate();
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteCustomerProgramme);
				preparedStatement.setInt(1, customerId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCustomerProgramme);
					preparedStatement.setInt(1, customerId);
					preparedStatement.setInt(2,
							member.get(i).programmeId);
					preparedStatement.setDouble(3,
							0);
					preparedStatement.setBoolean(4,
							true);
					preparedStatement.setInt(5,
							member.get(i).createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Programme Id");
					}
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertCustBioImages);
					System.out.println("bioimages size##"+member.get(i).bioimages.size());
					for (int j = 0; j < member.get(i).bioimages.size(); j++) {

						preparedStatement.setInt(1, customerId);
						preparedStatement.setString(2, member.get(i).bioimages.get(j).image);
						preparedStatement.setTimestamp(3, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						preparedStatement.executeUpdate();
					}
				}else{
					return new CompasResponse(201, "Failed to Update");
				}
			}
			//connection.commit();
			return new CompasResponse(200, "Record Updated Successfully");
			
		} catch (SQLException sqlEx) {
			
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Member No Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public CompasResponse UpdatePosCustomer(Customer member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int memberId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String example = "SGVsbG8gV29ybGQ=";
			byte[] decoded = org.apache.commons.codec.binary.Base64
					.decodeBase64(example.getBytes());
			// System.out.println(decoded);
			
				if (GetMemberByNo(member.memberNo)) {
					return new CompasResponse(201,
							"Member Id/Passport Already Exists");
				} else {

					preparedStatement = connection.prepareStatement(
							Queryconstants.insertMemberMaster,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, member.memberNo);
					// preparedStatement.setInt(2,
					// member.departmentId);
					preparedStatement.setString(2, member.surName);
					preparedStatement.setString(3, member.title);
					preparedStatement.setString(4, member.firstName);
					preparedStatement.setString(5, member.otherName);
					preparedStatement.setInt(6, 0);
					preparedStatement.setInt(7, 0);
					preparedStatement.setString(8, member.idPassPortNo);
					preparedStatement.setString(9, member.gender);
					// System.out.println("memberdob" + member.dateOfBirth);
					preparedStatement.setString(10, member.dateOfBirth);
					preparedStatement.setString(11, member.maritalStatus);
					preparedStatement.setString(12, member.nhifNo);
					preparedStatement.setString(13, member.height);
					preparedStatement.setString(14, member.weight);
					preparedStatement.setString(15, member.employerName);
					preparedStatement.setString(16, member.occupation);
					preparedStatement.setString(17, member.nationality);
					preparedStatement.setString(18, member.postalAdd);
					preparedStatement.setString(19, member.physicalAdd);
					preparedStatement.setString(20, member.homeTelephone);
					preparedStatement.setString(21, member.officeTelephone);
					preparedStatement.setString(22, member.cellPhone);
					preparedStatement.setString(23, member.email);
//					preparedStatement.setString(24, member.nokName);
//					preparedStatement.setString(25, member.nokIdPpNo);
//					preparedStatement.setString(26, member.nokTelephoneNo);
//					preparedStatement.setString(27, member.nokPostalAdd);

					// preparedStatement.setInt(7,
					// member.familySizeId);

					// preparedStatement.setString(9,
					// member.parentMemberNo);
					preparedStatement.setString(28, member.memberPic);
					preparedStatement.setInt(29, member.createdBy);
					preparedStatement.setTimestamp(30, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(31, "N");
					preparedStatement.setInt(32, member.familySize);
					preparedStatement.setInt(33, member.bnfGrpId);
					preparedStatement.setString(34, "N");
					preparedStatement.setString(35, member.memType);

				}
			
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (member.memberId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					memberId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteCustomerProgramme);
				preparedStatement.setInt(1, memberId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCustomerProgramme);
				for (int i = 0; i < member.programmes.size(); i++) {
					preparedStatement.setInt(1, memberId);
					preparedStatement.setInt(2,
							member.programmes.get(i).programmeId);
					preparedStatement.setDouble(3,
							member.programmes.get(i).programmeValue);
					preparedStatement.setBoolean(4,
							member.programmes.get(i).isActive);
					preparedStatement.setInt(5,
							member.programmes.get(i).createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Programme Id "
								+ member.programmes.get(i).programmeId);
					}
					
				
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				String cardno="123456"+generateNumber();
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCardIssuance);
				preparedStatement.setString(1, cardno);
				preparedStatement.setString(2, "1234567890");
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(4, "");
				preparedStatement.setString(5,"I");
				preparedStatement.setInt(6, memberId);
				preparedStatement.setInt(7,28);
				preparedStatement.setInt(8, member.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(10,"1234");
				if (preparedStatement.executeUpdate() > 0) {
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateCardStatus);
					preparedStatement.setString(1, "I");
					preparedStatement.setInt(2, memberId);
				}
				
				
				
				
				connection.commit();
				return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
						200, "Records Updated",memberId,member.firstName+" "+member.surName) : new CompasResponse(201,
						"Nothing To Update",0,"");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update",0,"");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Member No Already Exists",0,"");
			} else {
				return new CompasResponse(404, "Exception Occured",0,"");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured",0,"");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean GetMemberByNo(String memberNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMemberexists);
			preparedStatement.setString(1, memberNo);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> GetCardIssuanceList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getListOfCardsToIssue);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();

				objMember.memberId = resultSet.getInt("ID");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");
				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.bioId = resultSet.getString("bioid");
				objMember.memberPic = resultSet.getString("Pic");

				objMember.respCode = 200;

				preparedStatement = connection
						.prepareStatement(Queryconstants.getPorgrammesForIssueCard);
				preparedStatement.setInt(1, objMember.memberId);

				// objMember.binRange = resultSet.getString("BinRange");
				// objMember.programmeId =
				// resultSet.getInt("programmeId");
				// objMember.programmeDesc=
				// resultSet.getString("programmeDesc");
				// objMember.ipLimit=resultSet.getDouble("ipLimit");
				// objMember.opLimit=resultSet.getDouble("opLimit");
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getPorgrammesForIssueCard);
				preparedStatement1.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement1.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,"","",""));
				}
				objMember.programmes = programme;

				DBOperations.DisposeSql(preparedStatement, resultSet2);

				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerBioImages);
				preparedStatement.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement.executeQuery();

				// DBOperations.DisposeSql(preparedStatement1, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getCustomerBioImages);
				preparedStatement2.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement2.executeQuery();

				List<BioImage> images = new ArrayList<BioImage>();
				while (resultSet3.next()) {
					images.add(new BioImage(resultSet3.getInt("memberId"),
							resultSet3.getString("image")));
				}
				objMember.imageList = images;
				objMember.bioImage = getBioImages(images);

				System.out.println("ImageList##" + images.size()
						+ "##ImageId##" + objMember.memberId);
				System.out.println("CardNumber##" + objMember.cardNumber
						+ "##ImageId##" + objMember.memberId);
				objMember.imageList = images;
				members.add(objMember);
			}

			return members;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
	public List<Customer> GetCustCardIssuanceList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCustCardsToIssue);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();

				objMember.memberId = resultSet.getInt("ID");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");
				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.bioId = resultSet.getString("bioid");
				objMember.memberPic = resultSet.getString("Pic");

				objMember.respCode = 200;

				preparedStatement = connection
						.prepareStatement(Queryconstants.getPorgrammesForIssueCard);
				preparedStatement.setInt(1, objMember.memberId);

				// objMember.binRange = resultSet.getString("BinRange");
				// objMember.programmeId =
				// resultSet.getInt("programmeId");
				// objMember.programmeDesc=
				// resultSet.getString("programmeDesc");
				// objMember.ipLimit=resultSet.getDouble("ipLimit");
				// objMember.opLimit=resultSet.getDouble("opLimit");
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getPorgrammesForIssueCard);
				preparedStatement1.setInt(1, objMember.memberId);

				resultSet2 = preparedStatement1.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200,"","",""));
				}
				objMember.programmes = programme;

				DBOperations.DisposeSql(preparedStatement, resultSet2);

				preparedStatement = connection
						.prepareStatement(Queryconstants.getCustomerBioImages);
				preparedStatement.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement.executeQuery();

				// DBOperations.DisposeSql(preparedStatement1, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getCustomerBioImages);
				preparedStatement2.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement2.executeQuery();

				List<BioImage> images = new ArrayList<BioImage>();
				while (resultSet3.next()) {
					images.add(new BioImage(resultSet3.getInt("memberId"),
							resultSet3.getString("image")));
				}
				objMember.imageList = images;
				objMember.bioImage = getBioImages(images);

				System.out.println("ImageList##" + images.size()
						+ "##ImageId##" + objMember.memberId);
				System.out.println("CardNumber##" + objMember.cardNumber
						+ "##ImageId##" + objMember.memberId);
				objMember.imageList = images;
				members.add(objMember);
			}

			return members;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	/**
	 *
	 * @param images
	 * @return
	 */
	private String getBioImages(List<BioImage> images) {
		String result = "";

		for (int i = 0; i < images.size(); i++) {
			result += images.get(i).image;
		}
		return result;
	}

	/**
	 *
	 * @param member
	 * @return
	 */
	public CompasResponse UpdateCardStatus(Customer member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.updateCardStatus);
			preparedStatement.setString(1, member.cardStatus);
			preparedStatement.setInt(2, member.memberId);
			preparedStatement.executeUpdate();
			preparedStatement1 = connection
					.prepareStatement(Queryconstants.updateCardIssu);
			preparedStatement1.setString(1, member.cardStatus);
			preparedStatement1.setInt(2, member.memberId);
			return (preparedStatement1.executeUpdate() > 0) ? new CompasResponse(
					200, "Card Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "User Name Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> GetCardBlockList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet4 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardIssDetails);

			preparedStatement.setString(1, "A");
			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				// DBOperations.DisposeSql(preparedStatement);
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);

				resultSet4 = preparedStatement1.executeQuery();
				while (resultSet4.next()) {
					objMember.cardNumber = resultSet4.getString("cardno");
					objMember.cardBalance = resultSet4.getDouble("balance");

				}
				members.add(objMember);
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> GetCardActivationList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardIssDetails);

			preparedStatement.setString(1, "I");
			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;

				members.add(objMember);
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Personalize updateMemberCardLinkId(MemberCard memCard) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getOrgWallets);
			//preparedStatement.setInt(1, memCard.organizationId);
			resultSet = preparedStatement.executeQuery();
			List<Wallet> wallets = new ArrayList<Wallet>();
			while (resultSet.next()) {
				wallets.add(new Wallet(resultSet.getInt("wid"), resultSet
						.getString("wallet_name"), resultSet
						.getString("File_id"),
						resultSet.getString("file_type"), resultSet
								.getString("file_size"), resultSet
								.getString("record_length"), resultSet
								.getString("file_length"), resultSet
								.getString("system_code")));
			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBinByProgrammeId);
			preparedStatement.setInt(1, memCard.programmeId);
			resultSet2 = preparedStatement.executeQuery();
			while (resultSet2.next()) {
				memCard.binRange = resultSet2.getString("BInrange");
			}
			return (createdPersonalizeObject(memCard, wallets));
			// preparedStatement = connection
			// .prepareStatement(Queryconstants.updateMemberCardLinkId);
			// preparedStatement.setString(1, memCard.memberId);
			// preparedStatement.setString(2, memCard.cardNumber);
			// DBOperations.DisposeSql(preparedStatement);
			// preparedStatement = connection
			// .prepareStatement(Queryconstants.updateCardStatus);
			// preparedStatement.setString(1, "I");
			// preparedStatement.setString(2, memCard.memberId);
			//
			// return (preparedStatement.executeUpdate() > 0) ? new
			// MemberCard(
			// 200, "Records Updated") : new MemberCard(201,
			// "Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return null;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	private Personalize createdPersonalizeObject(MemberCard cardIssue,
			List<Wallet> walletList) {

		Personalize personalize = new Personalize();
		personalize.key = "6770776585828977";
		personalize.serialNumber = cardIssue.serialNumber;
		if (cardIssue.binRange == null) {
			personalize.cardNumber = "123456" + generateNumber();
		} else {
			personalize.cardNumber = cardIssue.binRange + generateNumber();
		}

		personalize.virginizeKey = "";
		personalize.fCardNumber = "0002";
		personalize.lCardNumber = "0010";
		personalize.fSerialNumber = "2FE2";
		personalize.lSerialNumber = "0010";
		personalize.fPesonalFile = "1001";
		personalize.lPersonalFile = "00EF";

		for (int j = 0; j < walletList.size(); j++) {
			Wallet wallet = walletList.get(j);
			switch (wallet.systemCode.trim()) {
			case "P":
				personalize.fUpkeepFile = wallet.fileId;
				personalize.lUpkeepFile = wallet.fileLength;
				personalize.rUpkeepFile = wallet.recordLength;
				personalize.tUpkeepFile = wallet.fileType;
				break;
			case "C":
				personalize.fCafeFile = wallet.fileId;
				personalize.lCafeFile = wallet.fileLength;
				personalize.rCafeFile = wallet.recordLength;
				personalize.tCafeFile = wallet.fileType;
				break;
			case "M":
				personalize.fFeeFile = wallet.fileId;
				personalize.lFeeFile = wallet.fileLength;
				personalize.rFeeFile = wallet.recordLength;
				personalize.tFeeFile = wallet.fileType;
				break;
			case "B":
				personalize.fBioFile = wallet.fileId;
				personalize.lBioFile = wallet.fileLength;
				personalize.rBioFile = wallet.recordLength;
				personalize.tBioFile = wallet.fileType;
				break;
			case "T":
				personalize.fTransFile = wallet.fileId;
				personalize.lTransFile = wallet.fileLength;
				personalize.rTransFile = wallet.recordLength;
				personalize.tTransFile = wallet.fileType;
				break;

			default:
				break;
			}

		}
		personalize.bioString = "";
		personalize.bioFileId = "";
		personalize.bioFileLength = "";
		personalize.fRunnigCafeFile = "";
		personalize.rRunnigCafeFile = "";
		personalize.tRunnigCafeFile = "";
		personalize.lRunnigCafeFile = "";
		personalize.personalString = padRight(
				createPersonalString(cardIssue, personalize), 245);
		personalize.programmeString = createProgrammeString(cardIssue);
		// personalize.bioString = createBioString(cardIssue);
		return personalize;
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	private static String createPersonalString(MemberCard cardIssue,
			Personalize personalize) {
		StringBuffer stringBuffer = new StringBuffer();
		/*
		 * 
		 * Card Number 20#card Level 1#Card Type 1#Issue date 8#Expiry Date 8#
		 * ReactDate 8 #CardHolder 30 # Course Name 10 # Fee Paid 12# Blood
		 * Group 3 # Allergies 50 # DepartMent Name 10# Card Status 1 # Temp Exp
		 * Date 8 # Emergency Nae 20 # Emergency Contact 20# DOB 8 # Fee Valid
		 * Date 8 # Finance Status 1 #
		 */
		stringBuffer.append("*").append(personalize.cardNumber).append("*")
				.append(cardIssue.firstName).append("|").append("NA")
				.append("|").append("NA").append("|").append("NA").append("*")
				.append("NA").append("|").append("NA").append("|").append("NA")
				.append("|").append("NA").append("*")
				.append(cardIssue.pinNumber);

		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(personalize.cardNumber.trim(),
		// "", 0, 20), " ", 20));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("S",
		// "", 0, 1), " ", 1));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("P",
		// "", 0, 1), " ", 1));
		// stringBuffer.append(GeneralUtility.getDate1("20150824",
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getDate1("00000000",
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getDate1(cardIssue.expiryDate.toString(),
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString((cardIssue.firstName),
		// "", 0, 30), " ", 30));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString((cardIssue.programmeDesc),
		// "", 0, 34), " ", 34));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("000000000000000000000000",
		// "", 0, 12), " ", 12));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("000000000000000000000000",
		// "", 0, 12), " ", 12));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(student.programme.unit.unitName,
		// "", 0, 53), " ", 53));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("",
		// "", 0, 50), " ", 50));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString((student.programme.programmeName),
		// "", 0, 10), " ", 10));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("A",
		// "", 0, 1), " ", 1));
		// stringBuffer.append(GeneralUtility.getDate1(cardIssue.expiryDate.toString(),
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(student.parentName,
		// "", 0, 20), " ", 20));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(student.parentContact,
		// "", 0, 20), " ", 20));
		// stringBuffer.append(GeneralUtility.getDate1(student.dateOfBirth.toString(),
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getDate1(cardIssue.expiryDate.toString(),
		// "yyyyMMdd"));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("N",
		// "", 0, 1), " ", 1));

		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();

	}

	private static String createProgrammeString(MemberCard cardIssue) {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility
				.getSubString("" + cardIssue.programmeId, "", 0, 20), " ", 20));
		stringBuffer
				.append(GeneralUtility.getStringPadRight(GeneralUtility
						.getSubString(cardIssue.programmeDesc, "", 0, 15), " ",
						15));
		stringBuffer.append(GeneralUtility.getStringPadRight(
				GeneralUtility.getSubString("KSH", "", 0, 5), " ", 5));
		stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility
				.getSubString("" + cardIssue.programmeValue, "", 0, 10), " ",
				10));
		if (cardIssue.issueType.equalsIgnoreCase("R")) {

			try {
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, cardIssue.customerId);
				resultSet = preparedStatement1.executeQuery();
				while (resultSet.next()) {
					stringBuffer.append(GeneralUtility.getStringPadRight(
							GeneralUtility.getSubString(
									"" + resultSet.getDouble("balance"), "", 0,
									10), " ", 10));

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// List<StringBuffer> listString = new
		// ArrayList<StringBuffer>();
		//
		// for (int i = 0; i < cardIssue.programmes.size(); i++) {
		// StringBuffer stringBuffer = new StringBuffer();
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(""
		// + cardIssue.programmes.get(i).programmeId, "", 0, 10), " ",
		// 10));
		// //
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("S",
		// // "", 0, 1), " ", 1));
		// //
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("P",
		// // "", 0, 1), " ", 1));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString((""
		// + cardIssue.programmes.get(i).programmeDesc), "", 0, 25),
		// " ", 25));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("KSH",
		// "", 0, 3), " ", 3));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString(""
		// + cardIssue.programmes.get(i).programmeValue, "", 0, 12),
		// " ", 12));
		// stringBuffer.append(GeneralUtility.getStringPadRight(GeneralUtility.getSubString("+",
		// "", 0, 200), " +", 200));
		// listString.add(stringBuffer);
		// }

		// stringBuffer.append(GeneralUtility.getStringPadRight(stu," ",85));
		// System.out.println(stringBuffer.toString());
		// System.out.println(stringBuffer.toString().length());
		return stringBuffer.toString();
	}

	private static String createBioString(MemberCard cardIssue) {

		StringBuffer stringBuffer = new StringBuffer();
		// for (int i = 0; i < cardIssue.bioImages.size(); i++) {
		//
		// stringBuffer.append(cardIssue.bioImages.get(i).image);
		//
		// }
		stringBuffer.append("*").append(cardIssue.bioImages.get(0).image)
				.append("*").append("NA").append("|").append("NA").append("|")
				.append("NA").append("|").append("NA").append("*").append("NA")
				.append("|").append("NA").append("|").append("NA").append("|")
				.append("NA").append("*").append("NA");
		// System.out.println("<>" + stringBuffer.toString());
		return stringBuffer.toString();

	}

	public List<MemberCard> getListOfCards() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getListOfCards);

			resultSet = preparedStatement.executeQuery();
			List<MemberCard> cards = new ArrayList<MemberCard>();
			while (resultSet.next()) {
				cards.add(new MemberCard(resultSet.getInt("id"), resultSet
						.getString("cardNo"), resultSet.getString("cardNo"),
						200, "Cards Fetched Successfully", 0, resultSet
								.getInt("accountType")));
			}
			return cards;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Customer> getListOfCardsToPrint() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getListOfCardsToPrint);

			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");
				// objMember.relationId =
				// resultSet.getInt("RelationID");
				// objMember.relationDesc =
				// resultSet.getString("RelationDesc");
				// objMember.categoryId =
				// resultSet.getInt("CategoryID");
				// objMember.categoryName =
				// resultSet.getString("CategoryName");
				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				// objMember.fullName=resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.coverName =
				// resultSet.getString("CoverName");
				// objMember.coverId=resultSet.getInt("coverID");
				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");

				objMember.cardNumber = resultSet.getString("CardNo");
				objMember.accType = resultSet.getInt("accountType");

				objMember.respCode = 200;
				// objMember.ipLimit=resultSet.getDouble("ipLimit");
				// objMember.opLimit=resultSet.getDouble("opLimit");
				members.add(objMember);
			}

			return members;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	public List<Wallet> GetWallets() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWallets);
			resultSet = preparedStatement.executeQuery();
			List<Wallet> classes = new ArrayList<Wallet>();
			while (resultSet.next()) {
				classes.add(new Wallet(resultSet.getInt("wID"), resultSet
						.getString("wallet_Name")));
			}
			return classes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateWallet(Organization org) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int agentId = 0;
		try {
			connection = dataSource.getConnection();
			// connection.setAutoCommit(false);
			preparedStatement = connection
					.prepareStatement(Queryconstants.deleteWalletDetails);
			preparedStatement.setInt(1, org.organizationId);
			preparedStatement.executeUpdate();

			DBOperations.DisposeSql(preparedStatement);
			// insert branches
			preparedStatement = connection
					.prepareStatement(Queryconstants.insertWalletDetails);
			for (int i = 0; i < org.wallets.size(); i++) {
				preparedStatement.setInt(1, org.wallets.get(i).walletId);
				preparedStatement.setInt(2, org.organizationId);
				preparedStatement.setBoolean(3, org.wallets.get(i).isActive);

				preparedStatement.setInt(4, org.wallets.get(i).createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Failed to Insert wallet Id "
							+ org.wallets.get(i).walletId);
				}
			}
			// connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Agent Name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateCardIssuance(MemberCard card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}

			preparedStatement = connection
					.prepareStatement(Queryconstants.insertCardIssuance);
			preparedStatement.setString(1, card.cardNumber);
			preparedStatement.setString(2, card.serialNumber);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(4, card.expiryDate);
			preparedStatement.setString(5, card.status);
			preparedStatement.setInt(6, card.customerId);
			preparedStatement.setInt(7, card.programmeId);
			preparedStatement.setInt(8, card.createdBy);
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(10, card.pinNumber);
			if (preparedStatement.executeUpdate() > 0) {
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardStatus);
				preparedStatement.setString(1, card.status);
				preparedStatement.setInt(2, card.customerId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Card Number Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean checkCardNumber(String cardNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCardNumber);
			preparedStatement.setString(1, cardNo);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public MemberCard GetLoadWalletCardDetails(String cardNumber) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLoadWalletCardDetails);
			preparedStatement.setString(1, cardNumber);
			resultSet = preparedStatement.executeQuery();
			MemberCard objCard = new MemberCard();
			if (resultSet.next()) {

				// objCard.loadAmount =
				// resultSet.getDouble("LoadAmount");
				// objCard.balance =
				// resultSet.getDouble("Balance");
				objCard.programmeId = resultSet.getInt("ProgrammeId");
				// objCard.programmeDesc =
				// resultSet.getString("ProgrammeDesc");
				objCard.programmeValue = resultSet.getDouble("ProgrammeValue");
				// objCard.loadCardString =
				// createProgrammeString(objCard);
				// card.add(objCard);
			}
			return objCard;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateCardLoad(MemberCard card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.updateCardLoad);
			preparedStatement.setDouble(1, card.balance);
			preparedStatement.setDouble(2, card.loadAmount);
			preparedStatement.setString(3, card.cardNumber);
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Card Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new CompasResponse(404, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateCustomerEnr(Customer customer) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.updateCustomerEnr);
			preparedStatement.setString(1, customer.bioId);
			preparedStatement.setInt(2, customer.memberId);
			preparedStatement.executeUpdate();
			DBOperations.DisposeSql(preparedStatement);
			preparedStatement = connection
					.prepareStatement(Queryconstants.insertCustBioImages);
			for (int i = 0; i < customer.imageList.size(); i++) {

				preparedStatement.setInt(1, customer.memberId);
				preparedStatement.setString(2, customer.imageList.get(i).image);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Failed to Insert Bio Image ");
				}
			}
			return new CompasResponse(200, "Card Updated");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "User Name Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse VerifyCustomerEnr(String bioImage) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.verifyCustomerEnroll);
			preparedStatement.setString(1, bioImage);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new CompasResponse(200, "Customer verified succesfully");
			} else {
				return new CompasResponse(201,
						"Failed to validate finger print");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/*private IDKit connectIdkit(String afisIp, String afisPort) {
		IDKit idkit = null;
		try {
			idkit = IDKit.getInstance();
		} catch (Exception e) {
			// jLabelStatus.setText("COULD NOT INITIATE IMAGE SDK-CONTACT ADMIN!!");
			e.printStackTrace();
		}

		try {
			idkit.initClient("type=service;server=" + afisIp + ":" + afisPort);

		} catch (Exception e) {
			// jLabelStatus.setText("NETWORK ERROR :CONTACT ADMIN");
			e.printStackTrace();
		}

		try {
			idkit.setParameter(
					com.innovatrics.idkit.IDKit.ConfigParameter.CFG_STORE_IMAGES,
					1);
			idkit.setParameter(
					com.innovatrics.idkit.IDKit.ConfigParameter.CFG_SIMILARITY_THRESHOLD,
					12300);
			idkit.setParameter(
					com.innovatrics.idkit.IDKit.ConfigParameter.CFG_BEST_CANDIDATES_COUNT,
					1);
			idkit.setParameter(
					com.innovatrics.idkit.IDKit.ConfigParameter.CFG_IDENTIFICATION_SPEED,
					5);
			idkit.setParameter(
					com.innovatrics.idkit.IDKit.ConfigParameter.CFG_RESOLUTION_DPI,
					500);

		} catch (Exception exception) {
			// jLabelStatus.setText("ERROR IN CONFIGURING CONTACT ADMIN!!");
			exception.printStackTrace();

		}
		return idkit;

	}*/

	public CompasResponse isImageDBDistinct(byte[] capturedImage,
			String capturePosition, String afisIp, String afisPort) {
		/*
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String response = "";
		IDKit idkit = connectIdkit(afisIp, afisPort);
		if (idkit != null) {
			boolean isCaptured = false;
			try {
				User validateUser = new User();
				FingerPosition fingerPosition = IDKit.FingerPosition.UNKNOWN_FINGER;
				validateUser.addFingerprint(fingerPosition,
						idkit.convertRawImage2Bmp(260, 300, capturedImage));
				IDKit.SearchResult[] result = idkit.findUser(validateUser);
				int userID = result[0].userID;
				if (userID > 0) {
					// isCaptured = true;
					// response = "Finger Already Enrolled";
					// idkit.terminateModule();

					connection = dataSource.getConnection();
					preparedStatement = connection
							.prepareStatement(Queryconstants.verifyCustomerEnroll);
					preparedStatement.setInt(1, userID);

					resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						return new CompasResponse(200,
								"Customer verified succesfully");
					} else {
						return new CompasResponse(201,
								"Failed to validate finger print");
					}

				} else {
					// isCaptured = false;
					// response = "0";
					// idkit.terminateModule();
					return new CompasResponse(201,
							"Failed to validate finger print");
				}
			} catch (IDKitException e) {
				e.printStackTrace();
				response = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				response = e.getMessage();
			}
		}*/
		return new CompasResponse(200, "validated finger print");
	}

	public List<ProgrammeValue> GetCardBalance(String cardNumber) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammeValue);
			preparedStatement.setString(1, cardNumber);
			resultSet = preparedStatement.executeQuery();
			List<ProgrammeValue> values = new ArrayList<ProgrammeValue>();

			while (resultSet.next()) {
				ProgrammeValue obj = new ProgrammeValue();
				// objCard.loadAmount =
				// resultSet.getDouble("LoadAmount");
				// objCard.balance =
				// resultSet.getDouble("Balance");
				obj.prgId = resultSet.getInt("ProgrammeId");
				obj.name = resultSet.getString("ProgrammeDesc");
				// objCard.programmeDesc =
				// resultSet.getString("ProgrammeDesc");
				obj.value = resultSet.getString("ProgrammeValue");
				// objCard.loadCardString =
				// createProgrammeString(objCard);
				// card.add(objCard);
				values.add(obj);
			}
			// System.out.println(values);
			return values;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Customer GetBioMembers(int bioId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			// CoverCategoryDalImpl dalImpl = new
			// CoverCategoryDalImpl(dataSource);
			// int yearId = dalImpl.GetCurrentYear();
			// if (yearId <= 0)
			// return null;
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBioMemberDetails);

			preparedStatement.setInt(1, bioId);
			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			Customer objMember = new Customer();
			if (resultSet.next()) {

				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;

				members.add(objMember);
				// members.add(new
				// Member(resultSet.getInt("ID"), resultSet
				// .getString("MemberNo"), resultSet
				// .getString("DepartmentDesc"), resultSet
				// .getString("FirstNames"), resultSet
				// .getString("Surname"), resultSet
				// .getString("RelationDesc"), resultSet
				// .getString("DateOfBirth"), resultSet
				// .getString("FamilyDesc"), resultSet
				// .getString("CategoryName"), resultSet
				// .getString("ParentMemberNo"), resultSet
				// .getString("CoverName"), resultSet
				// .getString("CoverPolicyNo"),
				// resultSet.getInt("bioid"),
				// 200, false,resultSet
				// .getString("Pic")));
			}
			return objMember;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// Card Reissue---get blocked card list
	public List<Customer> GetBlockedCards() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet4 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardIssDetails);

			preparedStatement.setString(1, "B");
			resultSet = preparedStatement.executeQuery();
			List<Customer> members = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objMember = new Customer();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.surName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
//				objMember.nokName = resultSet.getString("NokName");
//				objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
//				objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
//				objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				// DBOperations.DisposeSql(preparedStatement);
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);

				resultSet4 = preparedStatement1.executeQuery();
				while (resultSet4.next()) {
					objMember.cardNumber = resultSet4.getString("cardno");
					objMember.cardSerialNo = resultSet4.getString("serialno");
					objMember.cardPin = resultSet4.getString("cardpin");
					objMember.memberId = resultSet4.getInt("customerid");

				}
				members.add(objMember);
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse updateCardReissue(MemberCard card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}
			preparedStatement1 = connection
					.prepareStatement(Queryconstants.insertCardReIssueLog);
			preparedStatement1.setInt(1, card.customerId);
			preparedStatement1.setString(2, card.oldCardNo);
			preparedStatement1.setString(3, card.oldSerialNo);
			preparedStatement1.setString(4, card.oldPinNo);
			preparedStatement1.setInt(5, card.createdBy);
			preparedStatement1.setTimestamp(6, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			if (preparedStatement1.executeUpdate() > 0) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardIssuance);
				preparedStatement.setString(1, card.cardNumber);
				preparedStatement.setString(2, card.serialNumber);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(4, card.expiryDate);
				preparedStatement.setString(5, card.status);
				preparedStatement.setInt(6, card.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8, card.pinNumber);
				preparedStatement.setInt(8, card.customerId);
				if (preparedStatement.executeUpdate() > 0) {
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateCardStatus);
					preparedStatement.setString(1, card.status);
					preparedStatement.setInt(2, card.customerId);
				}
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Card Number Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 *
	 * @param bnfId
	 * @return
	 */
	public List<Programme> GetProgrammesByBnfId(int bnfId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammesByBnfGrp);
			preparedStatement.setInt(1, bnfId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.startDate = resultSet.getString("start_date");
				programme.endDate = resultSet.getString("end_date");
				programme.active = resultSet.getBoolean("active");
				programme.createdBy = resultSet.getInt("CreatedBy");
				programme.itmModes = resultSet.getString("itm_modes");
				programme.chtmModes = resultSet.getString("chtm_modes");
				programme.intmModes = resultSet.getString("intm_modes");
			//	programme.isActive=resultSet.getBoolean("isactive");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getVouchersByProgrammeId);
				preparedStatement.setInt(1, programme.programmeId);
				preparedStatement.setInt(2, programme.programmeId);
				resultSet2 = preparedStatement.executeQuery();
				List<Voucher> services = new ArrayList<Voucher>();
				while (resultSet2.next()) {
					services.add(new Voucher(resultSet2.getInt("voucherId"),
							resultSet2.getString("voucher_Name"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getDouble("voucher_value"), resultSet2
									.getString("frqmode"), resultSet2
									.getString("frqmode") != "" ? "Cyclic"
									: "Non-Cyclic"));
				}
				programme.vouchers = services;
				objProgrammes.add(programme);
			}
			return objProgrammes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 *
	 * @return
	 */
	public List<Relations> GetRelations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRelations);

			resultSet = preparedStatement.executeQuery();
			List<Relations> relations = new ArrayList<Relations>();
			while (resultSet.next()) {
				relations.add(new Relations(resultSet.getInt("ID"), resultSet
						.getString("RelationDesc"), resultSet
						.getInt("CreatedBy"), 200));
			}
			return relations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateBnfStatus(Customer member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			logger.info("## Beneficiary verifications update");
			for(int i=0;i<member.verifySelection.size();i++){
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateBnfStatus);
				preparedStatement.setString(1, member.bnfStatus);
				preparedStatement.setInt(2, member.verifySelection.get(i).memberId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					logger.info("Failed to Update beneficiary Id "
							+ member.verifySelection.get(i).memberId);
					return new CompasResponse(202, "Nothing To Update");
				}
			}
			
			//if(Utility.Sendmail(member.userName, member.bnfStatus, member.userEmail)){
			connection.commit();
			return new CompasResponse(200, "Records Updated");
			//}else{
				//connection.rollback();
				//return new CompasResponse(201, "Fail to send an email");
			//}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");

		}
	}

	/**
	 * @author kibet returns list of vouchers by programmeId
	 * @param programmeId
	 * @return list of vouchers
	 */
	public List<Topup> getTopupVouchersByProgram(String programmeId) {
		logger.info("ProgrammeId$$" + programmeId);
		Connection connection = null;
		// PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		 String sql =
		 "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,\n"
		 +
		 "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n" +
		 "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type\n"
		 +
		 "from BeneficiaryGroup b ,ProgrammeMaster p,\n" +
		 "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,\n" +
		 "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n" +
		 " where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 \n"
		 +
		 " and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId\n"
		 +
		 "   and vm.id = vd.voucher_id and vd.isActive=1 and p.Id in("+programmeId+")";
//		String sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc, "
//				+ "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value, "
//				+ "c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type "
//				+ "from ProgrammeMaster p,ProductMaster pm , "
//				+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv, "
//				+ "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm "
//				+ "where c.ProgrammeId=p.Id and c.IsActive=1 "
//				+ "and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId "
//				+ "and vm.id = vd.voucher_id and vd.isActive=1 and  p.Id in("+ programmeId + ")";

		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			// preparedStatement =
			// connection.prepareStatement(Queryconstants.getVoucherDetailsByProgramme);
			// preparedStatement.setString(1,programmeId);
			resultSet = statement.executeQuery(sql);
			List<Topup> vouchers = new ArrayList<Topup>();
			while (resultSet.next()) {
				Topup topup = new Topup();
				topup.programme_id = resultSet.getInt("programmeId");
				topup.programme_name = resultSet.getString("programmeDesc");
				topup.voucher_id = resultSet.getString("voucher_id");
				topup.service_id = resultSet.getInt("service_id");
				topup.service_quantity = resultSet.getDouble("quantity");
				topup.service_value = resultSet.getDouble("service_value");
				topup.beneficiary_group_id = resultSet.getInt("bgId");
				topup.beneficiary_id = resultSet.getInt("beneficiaryId");
				topup.uom = resultSet.getString("uom");
				topup.card_number = resultSet.getString("cardNumber");
				topup.voucher_type = resultSet.getString("voucher_type");
				vouchers.add(topup);
			}
			logger.info("##Voucher Size$$" + vouchers.size());

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, statement, resultSet);
		}
	}

	/**
	 * @author kibet returns list of vouchers by programmeId
	 * @param cardNumber
	 * @return list of vouchers
	 */
	public List<Topup> getTopupVouchersByCardNumber(String cardNumber) {
		logger.info("CardNumber" + cardNumber);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n"
				+ "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type from BeneficiaryGroup b ,\n"
				+ "ProgrammeMaster p,CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n"
				+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId\n"
				+ " and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId and vm.id = vd.voucher_id and vd.isActive=1 \n"
				+ " and ci.CardNo = ?  ";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, cardNumber);
			resultSet = preparedStatement.executeQuery();
			List<Topup> vouchers = new ArrayList<Topup>();
			while (resultSet.next()) {
				Topup topup = new Topup();
				topup.programme_id = resultSet.getInt("programmeId");
				topup.programme_name = resultSet.getString("programmeDesc");
				topup.voucher_id = resultSet.getString("voucher_id");
				topup.service_id = resultSet.getInt("service_id");
				topup.service_quantity = resultSet.getDouble("quantity");
				topup.service_value = resultSet.getDouble("service_value");
				topup.beneficiary_group_id = resultSet.getInt("bgId");
				topup.beneficiary_id = resultSet.getInt("beneficiaryId");
				topup.uom = resultSet.getString("uom");
				topup.card_number = resultSet.getString("cardNumber");
				topup.voucher_type = resultSet.getString("voucher_type");
				vouchers.add(topup);
			}
			logger.info("##Voucher Size$$" + vouchers.size());

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * @author Kibet
	 * @param topups
	 * @return success if inserted
	 */
	public CompasResponse insertTopupVoucher(List<Topup> topups) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int cycle = getCycleId();
		logger.info("Cycle##" + cycle);

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.insert_topup);
			for (Topup topup : topups) {
				String voucher_id_number = generateVoucherIdNumber(topup, cycle);
				preparedStatement.setInt(1, topup.beneficiary_group_id);
				preparedStatement.setInt(2, topup.programme_id);
				preparedStatement.setInt(3, topup.beneficiary_id);
				preparedStatement.setString(4, topup.card_number);
				preparedStatement.setString(5, topup.voucher_id);
				preparedStatement.setInt(6, topup.service_id);
				preparedStatement.setDouble(7, topup.service_value);
				preparedStatement.setDouble(8, topup.service_quantity);
				preparedStatement.setString(9, topup.uom);
				preparedStatement.setBoolean(10, false);
				preparedStatement.setTimestamp(11, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(12, cycle + 1);
				preparedStatement
						.setString(13, voucher_id_number);
				preparedStatement.addBatch();
			}

			int[] ins = preparedStatement.executeBatch();

			return (ins.length > 0) ? new CompasResponse(200,
					"Topup Done Successfully") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

		return new CompasResponse(201,
				"Error occurred while saving, please try again!!");
	}

	/**
	 * @author kibet returns list of vouchers by programmeId
	 * @param
	 * @return list of vouchers
	 */
	public String buildDownloadVouchers(String serialNo) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// String sql = "select * from mst_topup where voucher_id=?";
		String sql = "  select mt.voucher_id_number,mt.beneficiary_id,mt.card_number,mt.item_id,mt.item_quantity,mt.item_value,mt.programme_id,\n"
				+ "  mt.cycle,mt.voucher_id,mt.uom,am.agent_code,bg.bnfgrp_name,dr.SerialNo,sm.ServiceCode,sm.ServiceName,prm.ProgrammeDesc from mst_topup mt,AgentMaster am,BeneficiaryGroup bg,\n"
				+ "  ProductMaster pm,DeviceIssueDetails di,DeviceRegDetails dr,ServiceMaster sm,ProgrammeMaster prm where pm.merchant_id = am.merchantId \n"
				+ "  and bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and am.Id = di.AgentId \n"
				+ "   and di.DeviceRegId = dr.Id and sm.Id = mt.item_id and prm.Id = mt.programme_id and dr.SerialNo = ? and voucher_id_number=? and mt.downloaded = ?";

		Gson gson = new Gson();
		List<Topup> voucherIds = getVouchers(serialNo);
		logger.info("VoucherIds##" + voucherIds.size());
		List<VoucherVO> voucherVOList = new ArrayList<>();
		try {
			if (voucherIds.size() > 0) {
				connection = dataSource.getConnection();
				for (Topup topup : voucherIds) {
					logger.info("VoucherIdNumLoop##" + topup.voucher_id);
					VoucherVO voucherVO = new VoucherVO();
					String type = "";
					Topup topup1 = getVoucherDetails(topup.voucher_id);
//					Topup topup1 = getVoucherDetails(topup.id);
					
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, serialNo);
					preparedStatement.setString(2, topup.voucher_id);
					preparedStatement.setBoolean(3, false);
					resultSet = preparedStatement.executeQuery();
					voucherVO.cycle = topup1.cycle;
					voucherVO.intervention_id = topup1.programme_id;
					voucherVO.intervention = topup1.programme_name;
					voucherVO.card_number = topup1.card_number;
					voucherVO.voucher_id_number = topup1.voucher_id;
					voucherVO.valid_from = topup1.valid_from;
					voucherVO.valid_until = topup1.valid_until;

					if (topup1.voucher_type.trim().equalsIgnoreCase("CM")) {
						type = "0";
					}

					if (topup1.voucher_type.trim().equalsIgnoreCase("CV")) {
						type = "2";
					}

					if (topup1.voucher_type.trim().equalsIgnoreCase("CA")) {
						type = "3";
					}

					voucherVO.type = type;
					voucherVO.value = topup1.service_value;
					// connection = dataSource.getConnection();
					// preparedStatement = connection.prepareStatement(sql);
					// preparedStatement.setString(1, serialNo);
					// resultSet = preparedStatement.executeQuery();
					List<Commodity> commodities = new ArrayList<Commodity>();
					while (resultSet.next()) {
						Commodity commodity = new Commodity();
						commodity.pos_commodity = resultSet.getInt("item_id");
						commodity.commodity = resultSet
								.getString("ServiceName");
						commodity.unit = resultSet.getString("uom");
						commodity.code = resultSet.getInt("item_id");
						commodity.quantity = resultSet.getInt("item_quantity");
						commodity.price = resultSet.getInt("item_value");
						commodities.add(commodity);
					}
					voucherVO.commodities = commodities;
					voucherVOList.add(voucherVO);

				}
			}
			return new Gson().toJson(voucherVOList).toString();
			// return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * @author kibet returns list of vouchers
	 * @param
	 * @return list of vouchers
	 */
	private List<Topup> getVouchers(String deviceId) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "  select distinct voucher_id_number as voucher_id_number,mt.id as vid from mst_topup mt,AgentMaster am,ProductMaster pm,BeneficiaryGroup bg,DeviceIssueDetails di,DeviceRegDetails dr\n"
				+ "    where  bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and di.DeviceRegId = dr.Id and dr.SerialNo = ? and mt.downloaded = ?";
		Gson gson = new Gson();

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, deviceId);
			preparedStatement.setBoolean(2, false);
			List<Topup> vouchers = new ArrayList<Topup>();
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Topup topup = new Topup();
				// topup.programme_id=resultSet.getInt("programme_id");
				// topup.programme_name=resultSet.getString("programmeDesc");
				topup.id = resultSet.getInt("vid");
				topup.voucher_id = resultSet.getString("voucher_id_number");
				// topup.service_id = resultSet.getInt("item_id");
				// topup.service_quantity= resultSet.getDouble("item_quantity");
				// topup.service_value= resultSet.getDouble("item_value");
				// topup.beneficiary_group_id=resultSet.getInt("beneficiary_group_id");
				// topup.beneficiary_id = resultSet.getInt("beneficiary_id");
				// topup.uom = resultSet.getString("uom");
				// topup.card_number = resultSet.getString("card_number");
				// // topup.voucher_type = resultSet.getString("voucher_type");
				vouchers.add(topup);
			}
			logger.info("##Voucher Size$$" + vouchers.size());

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 *
	 * @param voucherId
	 * @return
	 */
	private Topup getVoucherDetails(String voucherId) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// String sql =
		// "select  top 1 * from mst_topup where voucher_id_number=?";
		String sql = "  select mt.voucher_id_number as voucher_id_number,mt.beneficiary_id,mt.card_number,mt.item_id,mt.item_quantity,mt.item_value,mt.programme_id,\n"
				+ "  mt.cycle,mt.voucher_id,mt.uom,am.agent_code,bg.bnfgrp_name,dr.SerialNo,sm.ServiceCode,sm.ServiceName,prm.ProgrammeDesc,vm.voucher_type,vm.start_date,vm.end_date\n"
				+ "   from mst_topup mt,AgentMaster am,BeneficiaryGroup bg,CardIssuance ci,M_MemberMaster mm,\n"
				+ "  ProductMaster pm,DeviceIssueDetails di,DeviceRegDetails dr,ServiceMaster sm,ProgrammeMaster prm,Voucher_mst vm,Voucher_dtl vd where pm.merchant_id = am.merchantId \n"
				+ "  and bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and am.Id = di.AgentId \n"
				+ "   and di.DeviceRegId = dr.Id and sm.Id = mt.item_id and prm.Id = mt.programme_id "
				+ "and vm.id = vd.voucher_id  and mt.voucher_id = vm.id and ci.CardNo=mt.card_number and mt.beneficiary_id = mm.ID  and mt.voucher_id_number = ? ";

		Gson gson = new Gson();
		Topup topup = new Topup();

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, voucherId);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				topup.programme_id = resultSet.getInt("programme_id");
				topup.programme_name = resultSet.getString("programmeDesc");
				topup.voucher_id = resultSet.getString("voucher_id_number");
				topup.service_id = resultSet.getInt("item_id");
				topup.service_quantity = resultSet.getDouble("item_quantity");
				topup.service_value = resultSet.getDouble("item_value");
				// topup.beneficiary_group_id=resultSet.getInt("beneficiary_group_id");
				topup.beneficiary_id = resultSet.getInt("beneficiary_id");
				topup.uom = resultSet.getString("uom");
				topup.card_number = resultSet.getString("card_number");
				topup.voucher_type = resultSet.getString("voucher_type");
				topup.cycle = resultSet.getInt("cycle");
				topup.service_name = resultSet.getString("ServiceName");
				topup.valid_from = resultSet.getString("start_date");
				topup.valid_until = resultSet.getString("end_date");
			}
			return topup;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * @author Kibet
	 * @param deviceId
	 * @return success if inserted
	 */
	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		logger.info("MacAddress##" + deviceId);
		String sql = "update mst_topup set downloaded = ? where voucher_id_number=? ";
		List<Topup> topups = getVouchersDownloadedToUpdate(deviceId);
		logger.info("Topups to Update##" + topups.size());
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (Topup topup : topups) {
				preparedStatement.setBoolean(1, true);
				preparedStatement.setString(2, topup.voucher_id);
				preparedStatement.addBatch();
			}

			int[] ins = preparedStatement.executeBatch();
			return (ins.length > 0) ? new CompasResponse(200,
					"Topup Update Done Successfully") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

		return new CompasResponse(201,
				"Error occurred while saving, please try again!!");
	}

	/**
	 * @author kibet returns list of vouchers
	 * @param
	 * @return list of vouchers
	 */
	private List<Topup> getVouchersDownloadedToUpdate(String serialNo) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "select t.voucher_id,t.voucher_id_number from BeneficiaryGroup b,ProductMaster p,\n"
				+ "  AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d,mst_topup t,Voucher_dtl vd\n"
				+ "   where p.id=b.product_id and a.merchantId=p.merchant_id and di.AgentId=a.Id \n"
				+ "   and d.Id=di.DeviceRegId and t.voucher_id = vd.voucher_id and d.SerialNo = ? and t.downloaded = ?";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, serialNo);
			preparedStatement.setBoolean(2, false);
			List<Topup> vouchers = new ArrayList<Topup>();
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Topup topup = new Topup();
				topup.voucher_id = resultSet.getString("voucher_id_number");
				vouchers.add(topup);
			}
			logger.info("##Voucher Update Size$$" + vouchers.size());

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * @author Kibet
	 * @param itemsList
	 * @return
	 */
	public String concatenateListwithComma(List itemsList) {
		String result = "";
		for (int i = 0; i < itemsList.size(); i++) {
			if (i != (itemsList.size() - 1))
				result += "'" + itemsList.get(i) + "'" + ",";
			if (i == (itemsList.size() - 1))
				result = result + "'" + itemsList.get(itemsList.size() - 1)
						+ "'";
		}
		logger.info(result);
		return result;
	}

	/**
	 *
	 * @return
	 */
	private String generateVoucherIdNumber(Topup topup, int cycle) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(topup.beneficiary_id)
				.append(topup.beneficiary_group_id).append(topup.programme_id)
				.append(topup.voucher_id).append(cycle + 1);
		logger.info("##" + stringBuffer.toString());
		return stringBuffer.toString();
	}

	/**
	 *
	 * @return
	 */
	private int getCycleId() {
		logger.info("Get Cycle Id$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "select max(id) as id from mst_topup";
		int maxId = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				maxId = resultSet.getInt("id");
			}

			return maxId;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public CompasResponse GetCustId(Customer customer) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String name = "";
		String cardnumber = "";
		String cust_name = "";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCustomerId);

			preparedStatement.setInt(1, customer.customerId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				cardnumber="123456"+generateNumber();
				cust_name=resultSet.getString("name");
				return new CompasResponse(
						200, "Records Updated",cardnumber,cust_name);
			} else {
				return  new CompasResponse(201,
						"Nothing To Update","","");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(401,
					"Exception Occured","","");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	
	/**
	 * send password to mail
	 * 
	 * @param userName
	 * @param randomid
	 * @param emailaddress
	 * @return
	 */

}
