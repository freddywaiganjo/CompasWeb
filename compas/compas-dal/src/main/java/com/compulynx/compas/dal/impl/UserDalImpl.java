/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.xmlbeans.impl.regex.REUtil;

import com.compulynx.compas.dal.UserDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BioImage;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.TmsRetailersDownload;
import com.compulynx.compas.models.TmsTransactionMst;
import com.compulynx.compas.models.TmsUserDownload;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TransactionVo;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndPrice;
import com.compulynx.compas.models.android.AndroidBeneficiary;
import com.compulynx.compas.models.android.AndroidBnfgrp;
import com.compulynx.compas.models.android.AndroidCategories;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidProducts;
import com.compulynx.compas.models.android.AndroidProgrammes;
import com.compulynx.compas.models.android.AndroidTopup;
import com.compulynx.compas.models.android.AndroidUsers;
import com.compulynx.compas.models.android.AndroidVoucher;
import com.compulynx.compas.models.android.ColorPin;

/**
 * @author Anita
 *
 */
public class UserDalImpl implements UserDal {
	private static final Logger logger = Logger.getLogger(UserDalImpl.class
			.getName());

	private DataSource dataSource;

	public UserDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean checkUserName(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserByName);
			preparedStatement.setString(1, userName);

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

	public List<User> GetClasses() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getClasses);
			resultSet = preparedStatement.executeQuery();
			List<User> classes = new ArrayList<User>();
			while (resultSet.next()) {
				classes.add(new User(resultSet.getInt("ID"), resultSet
						.getString("ClassName")));
			}
			return classes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateUser(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (user.userId == 0) {
				if (checkUserName(user.userName)) {
					return new CompasResponse(201, "User Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertUser);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				preparedStatement.setInt(4, user.userGroupId);
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setInt(7, 0);
				preparedStatement.setString(8, "");
				preparedStatement.setBoolean(9, false);
				preparedStatement.setLong(10, user.userLinkedID);
				preparedStatement.setLong(11, user.userBioID);
				preparedStatement.setBoolean(12, user.active);
				preparedStatement.setInt(13, user.createdBy);
				preparedStatement.setTimestamp(14, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(15, user.agentId);
				preparedStatement.setInt(16, user.locationId);
				preparedStatement.setInt(17, user.userTypeId);

				preparedStatement.setInt(18, user.posUserLevel);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUser);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				preparedStatement.setInt(4, user.userGroupId);
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setInt(7, user.userSecretQuestionId);
				preparedStatement.setString(8, "");
				preparedStatement.setBoolean(9, false);
				preparedStatement.setLong(10, user.userLinkedID);
				preparedStatement.setLong(11, user.userBioID);
				preparedStatement.setBoolean(12, user.active);
				preparedStatement.setInt(13, user.createdBy);
				preparedStatement.setTimestamp(14, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(15, user.agentId);
				preparedStatement.setInt(16, user.locationId);
				preparedStatement.setInt(17, user.userTypeId);
				preparedStatement.setInt(18, user.posUserLevel);
				preparedStatement.setInt(19, user.userId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
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

	// public CompasResponse UpdateUser(User user) {
	// Connection connection = null;
	// PreparedStatement preparedStatement = null;
	// ResultSet resultSet = null;
	// ResultSet rs = null;
	// int userId = 0;
	// try {
	// connection = dataSource.getConnection();
	// connection.setAutoCommit(false);
	// if (user.userId == 0) {
	// if (checkUserName(user.userName))
	// {
	// return new CompasResponse(201, "User Name is Already Exists");
	// }
	// preparedStatement = connection.prepareStatement(
	// Queryconstants.insertUserDetails,
	// Statement.RETURN_GENERATED_KEYS);
	// preparedStatement.setString(1, user.userName);
	// preparedStatement.setString(2, user.userFullName);
	// preparedStatement.setString(3, AES.encrypt(user.userPwd));
	// preparedStatement.setInt(4, user.userGroupId);
	// preparedStatement.setString(5, user.userEmail);
	// preparedStatement.setString(6, user.userPhone);
	// preparedStatement.setInt(7, user.userSecretQuestionId);
	// preparedStatement.setString(8,
	// AES.encrypt(user.userSecretAns));
	// preparedStatement.setBoolean(9, user.active);
	// preparedStatement.setInt(10, user.createdBy);
	// preparedStatement.setTimestamp(11, new java.sql.Timestamp(
	// new java.util.Date().getTime()));
	//
	// } else {
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.updateUserDeatils);
	// preparedStatement.setString(1, user.userName);
	// preparedStatement.setString(2, user.userFullName);
	// preparedStatement.setString(3, AES.encrypt(user.userPwd));
	// preparedStatement.setInt(4, user.userGroupId);
	// preparedStatement.setString(5, user.userEmail);
	// preparedStatement.setString(6, user.userPhone);
	// preparedStatement.setInt(7, user.userSecretQuestionId);
	// preparedStatement.setString(8, AES.encrypt(user.userSecretAns));
	// preparedStatement.setBoolean(9, user.active);
	// preparedStatement.setInt(10, user.createdBy);
	// preparedStatement.setTimestamp(11, new java.sql.Timestamp(
	// new java.util.Date().getTime()));
	// preparedStatement.setInt(12, user.userId);
	// userId = user.userId;
	// }
	// if (preparedStatement.executeUpdate() > 0) {
	//
	// // Dispose
	// if (user.userId == 0) {
	// rs = preparedStatement.getGeneratedKeys();
	// rs.next();
	// userId = rs.getInt(1);
	// }
	// DBOperations.DisposeSql(preparedStatement, rs);
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.deleteUserBranches);
	// preparedStatement.setInt(1, userId);
	// preparedStatement.executeUpdate();
	//
	// DBOperations.DisposeSql(preparedStatement);
	// // insert branches
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.insertUserBranchDetails);
	// for (int i = 0; i < user.userBranchDetails.size(); i++) {
	// preparedStatement.setInt(1, userId);
	// preparedStatement.setInt(2,
	// user.userBranchDetails.get(i).branchId);
	// preparedStatement.setBoolean(3,
	// user.userBranchDetails.get(i).isChecked);
	// preparedStatement.setInt(4,
	// user.userBranchDetails.get(i).createdBy);
	// preparedStatement.setTimestamp(5, new java.sql.Timestamp(
	// new java.util.Date().getTime()));
	// if (preparedStatement.executeUpdate() <= 0) {
	// throw new Exception("Failed to Insert Branch Id "
	// + user.userBranchDetails.get(i).branchId);
	// }
	//
	// }
	// connection.commit();
	// return new CompasResponse(200, "Records Updated");
	// } else {
	// connection.rollback();
	// return new CompasResponse(202, "Nothing To Update");
	//
	// }
	// } catch (SQLException sqlEx) {
	// sqlEx.printStackTrace();
	// if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
	// return new CompasResponse(201, "User Name is Already Exists");
	// } else {
	// return new CompasResponse(404, "Exception Occured");
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// try {
	// connection.rollback();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return new CompasResponse(404, "Exception Occured");
	// } finally {
	// DBOperations.DisposeSql(rs);
	// DBOperations.DisposeSql(connection, preparedStatement, resultSet);
	// }
	// }

	// public List<User> GetAllUsers() {
	// Connection connection = null;
	// PreparedStatement preparedStatement = null;
	// ResultSet resultSet = null;
	// try {
	// connection = dataSource.getConnection();
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.getUsers);
	//
	// resultSet = preparedStatement.executeQuery();
	// List<User> users = new ArrayList<User>();
	// while (resultSet.next()) {
	// users.add(new User(resultSet.getInt("ID"), resultSet
	// .getString("UserName"), resultSet
	// .getString("UserFullName"), AES.decrypt(resultSet
	// .getString("UserPassword")), resultSet
	// .getString("UserEmail"), resultSet
	// .getString("UserPhone"), resultSet
	// .getInt("UserGroupID"), resultSet
	// .getString("GroupName"), resultSet
	// .getInt("UserSecretQuestionID"), AES.decrypt(resultSet
	// .getString("UserSecretQuestionAns")), resultSet
	// .getBoolean("active"), resultSet.getInt("CreatedBy"),
	// 200));
	// }
	// return users;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// } finally {
	// DBOperations.DisposeSql(connection, preparedStatement, resultSet);
	// }
	// }
	// public List<User> GetAllUsers() {
	// Connection connection = null;
	// PreparedStatement preparedStatement = null;
	// ResultSet resultSet = null;
	// ResultSet resultSet2 = null;
	// try {
	// connection = dataSource.getConnection();
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.getUsers);
	//
	// resultSet = preparedStatement.executeQuery();
	// List<User> users = new ArrayList<User>();
	// while (resultSet.next()) {
	// User objUser = new User();
	// objUser.userId = resultSet.getInt("ID");
	// objUser.userName = resultSet.getString("UserName");
	// objUser.userFullName = resultSet.getString("UserFullName");
	// objUser.userPwd = AES.decrypt(resultSet
	// .getString("UserPassword"));
	// objUser.userEmail = resultSet.getString("UserEmail");
	// objUser.userPhone = resultSet.getString("UserPhone");
	// objUser.groupId = resultSet.getInt("UserGroupID");
	// objUser.groupName = resultSet.getString("GroupName");
	// objUser.userSecretQuestionId = resultSet
	// .getInt("UserSecretQuestionID");
	// objUser.userSecretAns = AES.decrypt(resultSet
	// .getString("UserSecretQuestionAns"));
	// objUser.active = resultSet.getBoolean("Active");
	// objUser.createdBy = resultSet.getInt("createdBy");
	// preparedStatement = connection
	// .prepareStatement(Queryconstants.getUserBranches);
	// preparedStatement.setInt(1, objUser.userId);
	// preparedStatement.setInt(2, objUser.userId);
	// preparedStatement.setInt(3, objUser.userId);
	// resultSet2 = preparedStatement.executeQuery();
	// List<Branch> branches = new ArrayList<Branch>();
	// while (resultSet2.next()) {
	// branches.add(new Branch(resultSet2.getInt("BranchID"),
	// resultSet2.getString("BranchName"), resultSet2
	// .getBoolean("isChecked")));
	// }
	// objUser.userBranchDetails = branches;
	// users.add(objUser);
	// }
	// return users;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// } finally {
	// DBOperations.DisposeSql(resultSet2);
	// DBOperations.DisposeSql(connection, preparedStatement, resultSet);
	// }
	// }
	public List<User> GetAllUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getInt("ID"), resultSet
						.getString("UserName"), resultSet
						.getString("UserFullName"), AES.decrypt(resultSet
						.getString("UserPassword")), resultSet
						.getInt("UserGroupID"), resultSet.getInt("AgentID"),
						resultSet.getInt("locationid"), resultSet
								.getString("UserEmail"), resultSet
								.getString("UserPhone"), resultSet
								.getInt("UserSecretQuestionID"), AES
								.decrypt(resultSet
										.getString("UserSecretQuestionAns")),
						resultSet.getBoolean("UserBioLogin"), resultSet
								.getInt("UserLinkedID"), resultSet
								.getInt("UserBioID"), resultSet
								.getBoolean("active"), resultSet
								.getInt("CreatedBy"), 200, resultSet
								.getInt("usertypeid"), count, resultSet
								.getString("usertypename"), resultSet
								.getInt("pos_user_level")));
				count++;
			}
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public User GetUserById(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserById);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new User(
						resultSet.getInt("UserID"),
						resultSet.getString("UserName"),
						resultSet.getString("UserFullName"),
						AES.decrypt(resultSet.getString("UserPassword")),
						resultSet.getString("UserEmail"),
						resultSet.getString("UserPhone"),
						resultSet.getInt("UserGroupID"),
						resultSet.getString("GroupName"),
						// resultSet.getInt("UserSecretQuestionID"),
						// AES.decrypt(resultSet.getString("UserSecretAns")),
						resultSet.getBoolean("active"),
						resultSet.getInt("CreatedBy"), 200);
			} else {
				return new User(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<User> GetQuestions() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getQuestions);

			resultSet = preparedStatement.executeQuery();
			List<User> questions = new ArrayList<User>();
			while (resultSet.next()) {
				questions.add(new User(resultSet.getString("Question"),
						resultSet.getInt("ID")));
			}
			return questions;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetBranchAgents(int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchAgents);
			preparedStatement.setInt(1, branchId);
			resultSet = preparedStatement.executeQuery();
			List<Agent> agents = new ArrayList<Agent>();
			while (resultSet.next()) {
				agents.add(new Agent(resultSet.getInt("Id"), resultSet
						.getString("agentDesc"), resultSet.getInt("BranchId"),
						resultSet.getBoolean("BranchId"), resultSet
								.getInt("CreatedBy"), 200));
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<TmsUserDownload> GetTmsUserDownload(String macAddress) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTmsUserDownloads);
			preparedStatement.setString(1, macAddress);
			resultSet = preparedStatement.executeQuery();
			List<TmsUserDownload> classes = new ArrayList<TmsUserDownload>();
			while (resultSet.next()) {

				classes.add(new TmsUserDownload(resultSet.getInt("ID"),
						resultSet.getString("UserFullName"), resultSet
								.getString("UserFullName"), resultSet
								.getString("UserName"), AES.decrypt(resultSet
								.getString("UserPassword")), resultSet
								.getInt("pos_user_level")));
			}
			return classes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public TmsRetailersDownload GetTmsRetailerDownload(String macAddress) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTmsRetailerDownload);
			preparedStatement.setString(1, macAddress);
			resultSet = preparedStatement.executeQuery();
			// /TmsRetailersDownload classes = new
			// ArrayList<TmsRetailersDownload>();
			TmsRetailersDownload ret = new TmsRetailersDownload();
			while (resultSet.next()) {

				ret.retailer_name = resultSet.getString("AgentDesc");
				ret.location = resultSet.getString("location_name");
				ret.contact_name = "";
				ret.phone_number = "";
				ret.mobile_number = "+254717555772";
			}
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public String UplodTmsTrans(List<TmsTransactionMst> trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		String response = "";
		int transMstId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			for (int i = 0; i < trans.size(); i++) {
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertTmsTransMst,
						Statement.RETURN_GENERATED_KEYS);
				System.out.println("$$" + trans.get(i).pos_terminal);
				System.out.println("$$" + trans.get(i).receipt_number);
				System.out.println("$$" + trans.get(i).user);

				preparedStatement.setString(1, trans.get(i).voucher);
				preparedStatement.setInt(2, trans.get(i).transaction_type);
				preparedStatement.setInt(3, trans.get(i).cycle);
				preparedStatement.setDouble(4,
						Double.valueOf(trans.get(i).value_remaining));
				preparedStatement.setDouble(5, 0.00);
				preparedStatement
						.setDouble(
								6,
								Double.valueOf(trans.get(i).total_amount_charged_by_retailer));
				preparedStatement.setString(7, trans.get(i).pos_terminal);
				preparedStatement.setString(8, trans.get(i).receipt_number);
				preparedStatement.setString(9, trans.get(i).user);
				preparedStatement.setString(10,
						trans.get(i).timestamp_transaction_created);
				preparedStatement.setInt(11, trans.get(i).authentication_type);
				preparedStatement.setString(12, trans.get(i).latitude);
				preparedStatement.setString(13, trans.get(i).longitude);
				preparedStatement.setInt(14, trans.get(i).createdBy);

				preparedStatement.setTimestamp(15, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(16, trans.get(i).uid);
				preparedStatement.setString(17, trans.get(i).cardNumber);
			preparedStatement.setString(18, trans.get(i).rationNo);
				if (preparedStatement.executeUpdate() > 0) {

					// Dispose

					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					transMstId = rs.getInt(1);
					DBOperations.DisposeSql(preparedStatement);
					// insert rights
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertTmsTransDtl);
					for (int j = 0; j < trans.get(i).commodities.size(); j++) {
						preparedStatement.setInt(1, transMstId);
						preparedStatement.setInt(2,
								trans.get(i).commodities.get(j).pos_commodity);
						System.out
								.println("remaining quantity##"
										+ trans.get(i).commodities.get(j).quantity_remaining);
						preparedStatement.setDouble(3, Double.valueOf(trans
								.get(i).commodities.get(j).quantity_remaining));
						System.out
								.println("deducted quantity##"
										+ trans.get(i).commodities.get(j).deducted_quantity);
						preparedStatement.setDouble(4, Double.valueOf(trans
								.get(i).commodities.get(j).deducted_quantity));
						preparedStatement
								.setDouble(
										5,
										Double.valueOf(trans.get(i).commodities
												.get(j).amount_charged_by_retailer));
						preparedStatement.setInt(6, 0);
						preparedStatement.setTimestamp(
								7,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement.setString(8,
								trans.get(i).timestamp_transaction_created);
						if (preparedStatement.executeUpdate() <= 0) {
							throw new Exception(
									"Failed to Insert Trans Id "
											+ trans.get(i).commodities.get(j).pos_commodity);
						}
					}
					connection.commit();
					response = "200";

				} else {
					connection.rollback();
					response = "500";
					return response;
				}

			}
			return response;

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			response = "500";
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response = "500";
			return response;
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<UserGroup> GetGroupsByUserType(int userTypeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupsByUserType);
			preparedStatement.setInt(1, userTypeId);
			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupCode = resultSet.getString("groupcode");
				objGroup.groupName = resultSet.getString("groupName");
				objGroup.active = resultSet.getBoolean("active");
				objGroup.grpTypeId = resultSet.getInt("grouptypeid");
				objGroup.createdBy = resultSet.getInt("createdBy");
				groups.add(objGroup);
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo GetAndroidDownloadData(String macAddress) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			AndroidDownloadVo downloadVo = new AndroidDownloadVo();
			preparedStatement=connection.prepareStatement(Queryconstants.checkDeviceRegister);
			preparedStatement.setString(1, macAddress);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()){
				DBOperations.DisposeSql(preparedStatement,resultSet);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAndUsers);
				preparedStatement.setString(1, macAddress);
				resultSet = preparedStatement.executeQuery();
				int locationId=0;
				List<AndroidUsers> users = new ArrayList<AndroidUsers>();
				while (resultSet.next()) {
					AndroidUsers objUser = new AndroidUsers();
					objUser.userId = resultSet.getInt("ID");
					objUser.userName = resultSet.getString("userName");
					objUser.userFullName = resultSet.getString("userfullname");
					objUser.password = AES.decrypt(resultSet
							.getString("userpassword"));
					objUser.level = resultSet.getString("pos_user_level");
					objUser.locationId=resultSet.getInt("locationid");
					locationId=objUser.locationId;
					users.add(objUser);
				}
				downloadVo.users = users;
				logger.info("Users##" + users.size());
				DBOperations.DisposeSql(preparedStatement, resultSet);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAndCategories);

				resultSet = preparedStatement.executeQuery();
				List<AndroidCategories> categories = new ArrayList<AndroidCategories>();
				while (resultSet.next()) {
					AndroidCategories objCategory = new AndroidCategories();
					objCategory.categoryId = resultSet.getInt("ID");
					objCategory.categoryCode = resultSet.getString("category_code");
					objCategory.categoryName = resultSet.getString("category_name");

					categories.add(objCategory);
				}
				downloadVo.categories = categories;
				logger.info("Categories##" + categories.size());
				DBOperations.DisposeSql(preparedStatement, resultSet);
				System.out.println("LocationId##"+locationId);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAndProducts);
				preparedStatement.setInt(1, locationId);

				resultSet = preparedStatement.executeQuery();
				List<AndroidProducts> services = new ArrayList<AndroidProducts>();
				while (resultSet.next()) {
					AndroidProducts objService = new AndroidProducts();
					objService.productId = resultSet.getInt("service_id");
					objService.productCode = resultSet.getString("servicecode");
					objService.productName = resultSet.getString("servicename");
					objService.productDesc = resultSet.getString("servicedesc");
					objService.uom = resultSet.getString("componame");
					objService.locationId=resultSet.getInt("location_id");
				//	objService.minPrice=resultSet.getDouble("min_price");
					//objService.maxPrice=resultSet.getDouble("max_price");
					objService.categoryId = resultSet.getInt("category_id");
					if (resultSet.getString("image") != "") {
						String[] strImage = resultSet.getString("image").split(",");

						objService.image = strImage[1].toString();

					} else {
						objService.image = "";
					}
					
					preparedStatement2=connection.prepareStatement(Queryconstants.getAndPriceDetails);
					preparedStatement2.setInt(1, objService.productId);
					resultSet2=preparedStatement2.executeQuery();
					ArrayList<AndPrice> prices= new ArrayList<AndPrice>();
					while(resultSet2.next()){
						AndPrice price=new AndPrice();
						//price.serviceId=resultSet2.getInt("service_id");
						price.maxPrice=resultSet2.getDouble("max_price");
						price.uom=resultSet2.getString("UOM");
						price.quantity=resultSet2.getDouble("quantity");
						prices.add(price);
					}
					objService.priceDetails=prices;
					services.add(objService);
				}
				downloadVo.products = services;
				logger.info("Services##" + services.size());
				DBOperations.DisposeSql(preparedStatement, resultSet);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAndBnfGrps);

				resultSet = preparedStatement.executeQuery();
				List<AndroidBnfgrp> bnfGrps = new ArrayList<AndroidBnfgrp>();
				while (resultSet.next()) {
					AndroidBnfgrp objbnfgrp = new AndroidBnfgrp();
					objbnfgrp.bnfGrpId = resultSet.getInt("ID");
					objbnfgrp.bnfGrpName = resultSet.getString("bnfgrp_name");

					bnfGrps.add(objbnfgrp);
				}
				downloadVo.bnfGrps = bnfGrps;
				logger.info("Beneficiary Groups##" + bnfGrps.size());
				DBOperations.DisposeSql(preparedStatement, resultSet);
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAndColorCodes);

				resultSet = preparedStatement.executeQuery();
				List<ColorPin> colors = new ArrayList<ColorPin>();
				while (resultSet.next()) {
					ColorPin objColor = new ColorPin();
					objColor.colorId = resultSet.getInt("ID");
					objColor.colorName = resultSet.getString("color_name");
					objColor.colorCode=resultSet.getString("color_code");
					objColor.colorNo=resultSet.getInt("color_no");
					colors.add(objColor);
				}
				downloadVo.colors = colors;
				logger.info("Colors##" + colors.size());
				downloadVo.programmes = GetAndroidProgrammes();
				// downloadVo.topupDetails=GetAndroidTopupDetails(macAddress);
				// System.out.println("Topup details##"+GetAndroidTopupDetails(macAddress).size());
				return downloadVo;
			}else{
				downloadVo.respCode=400;
				return downloadVo;
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<AndroidProgrammes> GetAndroidProgrammes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAndroidProgrammes);

			resultSet = preparedStatement.executeQuery();
			List<AndroidProgrammes> programmes = new ArrayList<AndroidProgrammes>();
			while (resultSet.next()) {
				AndroidProgrammes programme = new AndroidProgrammes();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeName = resultSet.getString("ProgrammeDesc");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startDate = sdf.format(sdf.parse(resultSet
						.getString("start_date")));
				String endDate = sdf.format(sdf.parse(resultSet
						.getString("end_date")));
				programme.startDate = startDate;
				programme.endDate = endDate;
				programme.bnfGrpId = resultSet.getInt("bnfgrpid");
				programme.prgType = resultSet.getString("prg_type");
				programme.itmModes = resultSet.getString("itm_modes");
				// programme.carryFwd=resulset.getBoo
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getAndroidVouchers);
				preparedStatement1.setInt(1, programme.programmeId);
				resultSet2 = preparedStatement1.executeQuery();
				List<AndroidVoucher> vouchers = new ArrayList<AndroidVoucher>();
				while (resultSet2.next()) {
					AndroidVoucher objvoucher = new AndroidVoucher();
					String vstartDate = sdf.format(sdf.parse(resultSet2
							.getString("start_date")));
					String vendDate = sdf.format(sdf.parse(resultSet2
							.getString("end_date")));
					objvoucher.voucherId = resultSet2.getInt("voucherId");
					objvoucher.voucherName = resultSet2
							.getString("voucher_name");
					objvoucher.startDate = vstartDate;
					objvoucher.endDate = vendDate;
					vouchers.add(objvoucher);
					// services
					preparedStatement2 = connection
							.prepareStatement(Queryconstants.getAndroidProducts);
					preparedStatement2.setInt(1, objvoucher.voucherId);
					resultSet3 = preparedStatement2.executeQuery();
					List<AndroidProducts> products = new ArrayList<AndroidProducts>();
					while (resultSet3.next()) {
						AndroidProducts objService = new AndroidProducts();
						objService.serviceId = resultSet3.getInt("Service_Id");
						// objService.productCode = resultSet3
						// .getString("servicecode");
						// objService.productName = resultSet3
						// .getString("servicename");
						// objService.productDesc = resultSet3
						// .getString("servicedesc");
						// objService.uom = resultSet3.getString("componame");
						// objService.categoryId = resultSet3
						// .getInt("category_id");
						// if(resultSet3.getString("image")!=""){
						// String[] strImage = resultSet3.getString("image")
						// .split(",");
						// objService.image = strImage[1].toString();
						// }else{
						// objService.image="";
						// }
						products.add(objService);
					}

					objvoucher.products = products;
				}
				programme.vouchers = vouchers;
				// System.out.println("Programmes##" + vouchers.size());
				programmes.add(programme);
				count++;
			}
			logger.info("Programmes##" + programmes.size());
			return programmes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet2);
			DBOperations.DisposeSql(preparedStatement2, resultSet3);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo GetAndroidTopupDetails(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			System.out.println("Macaddress Received from MObile&&" + serialNo);
			connection = dataSource.getConnection();
			AndroidDownloadVo download = new AndroidDownloadVo();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAndroidTopup);
			preparedStatement.setString(1, serialNo);
			resultSet = preparedStatement.executeQuery();
			List<AndroidTopup> topupDeatils = new ArrayList<AndroidTopup>();
			while (resultSet.next()) {
				AndroidTopup objtopup = new AndroidTopup();
				objtopup.programmeId = resultSet.getInt("programme_id");
				objtopup.beneficiaryId = resultSet.getInt("beneficiary_id");
				objtopup.cardNumber = resultSet.getString("card_number");
				objtopup.voucherId = resultSet.getInt("voucher_id");
				// objtopup.productId=resultSet.getInt("item_id");
				objtopup.productPrice = resultSet.getDouble("item_value");
				// objtopup.productQuantity=resultSet.getDouble("item_quantity");
				// objtopup.uom=resultSet.getString("uom");
				objtopup.voucherType = resultSet.getString("voucher_Type");
				objtopup.voucherIdNumber = resultSet
						.getString("voucher_id_number");
				objtopup.bnfGrpId = resultSet.getString("beneficiary_group_id");
				objtopup.cycle = resultSet.getInt("cycle");
				topupDeatils.add(objtopup);

			}

			count++;
			download.topupDetails = topupDeatils;
			logger.info("TopupDetails##" + download.topupDetails.size());
			return download;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {

			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo GetAndroidBeneficiary(String macAddress) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			AndroidDownloadVo downloadVo = new AndroidDownloadVo();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAndroidBeneficiary);
			preparedStatement.setString(1, macAddress);

			resultSet = preparedStatement.executeQuery();
			List<Customer> benficiary = new ArrayList<Customer>();
			while (resultSet.next()) {
				Customer objbnf = new Customer();
				objbnf.memberNo = resultSet.getString("memberno");
				objbnf.cardNumber = resultSet.getString("cardno");
				objbnf.serialNo = resultSet.getString("serialno");
				objbnf.cardPin = resultSet.getString("cardpin");
				objbnf.surName = resultSet.getString("surname");
				objbnf.title = resultSet.getString("title");
				objbnf.firstName = resultSet.getString("firstnames");
				objbnf.otherName = resultSet.getString("othername");
				objbnf.cellPhone = resultSet.getString("mobileno");
				objbnf.idPassPortNo = resultSet.getString("idppno");
				// objbnf.cardNumber=resultSet.getString("cardno");
				objbnf.gender = resultSet.getString("gender");
				// System.out.println("memberdob" + member.dateOfBirth);
				objbnf.dateOfBirth = resultSet.getString("dateofbirth");
				objbnf.maritalStatus = resultSet.getString("maritalStatus");
				objbnf.nationality = resultSet.getString("nationality");
				objbnf.email = resultSet.getString("email");
				objbnf.programmeId = resultSet.getInt("programmeid");
				objbnf.memberPic = resultSet.getString("pic");
				//System.out.println(objbnf.memberPic);
				//if (objbnf.memberPic.equalsIgnoreCase(null) ) {
				//	String[] strImage = resultSet.getString("pic").split(",");

					//objbnf.memberPic = strImage[1].toString();

			//	} else {
					objbnf.memberPic = "";
				//}

				objbnf.familySize = resultSet.getInt("familysize");
				objbnf.bnfGrpId = resultSet.getInt("bnfgrpid");

				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getCustomerBioImages);
				preparedStatement2.setInt(1, objbnf.memberId);
				resultSet3 = preparedStatement2.executeQuery();

				List<BioImage> images = new ArrayList<BioImage>();
				while (resultSet3.next()) {
					images.add(new BioImage(resultSet3.getInt("memberId"),
							resultSet3.getString("image")));
				}
				if (images.size() > 0) {
					objbnf.right_index = images.get(0).image;
					objbnf.right_thumb = images.get(1).image;
					objbnf.left_index = images.get(2).image;
					objbnf.left_thumb = images.get(3).image;
				}

				// objbnf.bioimages = images;

				benficiary.add(objbnf);
			}
			downloadVo.beneficiaries = benficiary;
			logger.info("Beneficiaries##" + downloadVo.beneficiaries.size());
			return downloadVo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet2);
			DBOperations.DisposeSql(preparedStatement2, resultSet3);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		logger.info("MacAddress##" + deviceId);
		String sql = "update mst_topup set downloaded = ? where cycle=? and topup_status='A' and agent_id=? and beneficiary_group_id=?";
		List<Topup> topups = new ArrayList<Topup>();
		topups = getVouchersDownloadedToUpdate(deviceId);
		logger.info("Cycle to Update##" + topups.size());
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (Topup topup : topups) {
				preparedStatement.setBoolean(1, true);
				preparedStatement.setInt(2, topup.cycle);
				preparedStatement.setInt(3, topup.agentId);
				preparedStatement.setInt(4, topup.beneficiary_group_id);
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

	private List<Topup> getVouchersDownloadedToUpdate(String serialNo) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		String sql2 = "select AgentId from DeviceIssueDetails di,DeviceRegDetails dr where dr.Id=di.DeviceRegId and dr.SerialNo=?";
		// String sql =
		// "select distinct t.voucher_id,t.voucher_id_number from BeneficiaryGroup b,ProductMaster p,\n"
		// +
		// "  AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d,mst_topup t,Voucher_dtl vd\n"
		// +
		// "   where p.id=b.product_id and a.merchantId=p.merchant_id and di.AgentId=a.Id \n"
		// +
		// "   and d.Id=di.DeviceRegId and t.voucher_id = vd.voucher_id and t.downloaded = ? "
		// +
		// " and topup_status='A' and t.agent_id=di.AgentId and a.Id=t.agent_id and t.agent_id=?";
		String sql = "select distinct t.cycle,t.beneficiary_group_id from "
				+ "  AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d,mst_topup t "
				+ " where   di.AgentId=a.Id "
				+ " and d.Id=di.DeviceRegId  and t.downloaded = ? "
				+ "  and topup_status='A' and t.agent_id=di.AgentId and a.Id=t.agent_id and t.agent_id=? ";

		try {
			connection = dataSource.getConnection();
			preparedStatement1 = connection.prepareStatement(sql2);
			preparedStatement1.setString(1, serialNo);
			resultSet1 = preparedStatement1.executeQuery();
			List<Topup> vouchers = new ArrayList<Topup>();
			if (resultSet1.next()) {
				preparedStatement = connection.prepareStatement(sql);
				// preparedStatement.setString(1, serialNo);
				preparedStatement.setBoolean(1, false);
				preparedStatement.setInt(2, resultSet1.getInt("agentid"));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Topup topup = new Topup();
					topup.cycle = resultSet.getInt("cycle");
					topup.agentId=resultSet1.getInt("agentid");
					topup.beneficiary_group_id=resultSet.getInt("beneficiary_group_id");
					vouchers.add(topup);
				}

			}

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Agent> GetLocationAgents(int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLocationAgents);
			preparedStatement.setInt(1, locationId);
			resultSet = preparedStatement.executeQuery();
			List<Agent> agents = new ArrayList<Agent>();
			while (resultSet.next()) {
				agents.add(new Agent(resultSet.getInt("Id"), resultSet
						.getString("agentDesc"), resultSet.getInt("BranchId"),
						resultSet.getBoolean("BranchId"), resultSet
								.getInt("CreatedBy"), 200));
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
