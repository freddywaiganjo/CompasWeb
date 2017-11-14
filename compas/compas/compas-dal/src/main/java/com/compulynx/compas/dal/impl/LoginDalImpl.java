/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.LoginDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Rights;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.SafComResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.SafComLogin;
import com.compulynx.compas.models.UserServices;

/**
 * @author Anita
 *
 */
public class LoginDalImpl implements LoginDal {
	private DataSource	dataSource;

	public LoginDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public LoginUser GetUserIdManual(String userName, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialManual);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			//System.out.println(AES.decrypt("ULOZKcQxo3ZBUw4_YPZHLw"));
		//	System.out.println(AES.encrypt(password));
			// preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new LoginUser(resultSet.getInt("UserID"), 200);
			} else {
				return new LoginUser(201);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public LoginUser GetUserIdBio(int bioId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialBio);
			preparedStatement.setInt(1, bioId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new LoginUser(resultSet.getInt("UserID"), 200);
			} else {
				return new LoginUser(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/*
	 * Anita Comment's Method should only be called if the user has a Right
	 * to View > 1. Hence Validate User Credentials Is Called First
	 */
	public LoginSession GetUserAssgnRightsList(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserGrpRights);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			// System.out.println("User Id: " + userId);
			resultSet = preparedStatement.executeQuery();
			List<Rights> objlist = new ArrayList<Rights>();
			LoginSession loginSession = new LoginSession();

			// Using to check Previous Header id if the ID's are the
			// same, if
			// not create new header object
			int headerId = 0;
			Rights objRights = null;
			while (resultSet.next()) {
				// The Values Remain the Same, No Harm
				// Reassigning

				loginSession.setLinkId(resultSet.getInt("LinkId"));
				loginSession.setSessionId(resultSet.getInt("ID"));
			//	loginSession.setUserClassID(resultSet.getInt("UserClassID"));
				loginSession.setUserGroupId(resultSet.getInt("UserGroupID"));
				loginSession.setLinkName(resultSet.getString("LinkName"));
				loginSession.setSessionName(resultSet.getString("UserName"));
				loginSession.setSessionFullName(resultSet.getString("UserFullName"));
				loginSession.setLinkExtInfo(resultSet.getString("LinkExtInfo"));
				loginSession.setTopupLimit(resultSet.getDouble("topup_limit"));
				// System.out.println("Comapring Header Id : " +
				// headerId +
				// " to : " + resultSet.getInt("HeaderID"));
				if (!(headerId == resultSet.getInt("HeaderID"))) {
					// Avoiding adding Null Object On First
					// loop
					if (!(objRights == null)) {
						objlist.add(objRights);
					}
					objRights = new Rights(resultSet.getString("HeaderName"), resultSet.getString("HeaderIconCss"),
							resultSet.getString("HeaderIconColor"));
				}
				if (!(objRights == null)) {
					objRights.rightsList.add(new RightsDetail(resultSet.getString("RightDisplayName"), resultSet
							.getString("RightShortCode"), resultSet.getString("RightViewName"), resultSet
							.getString("RightName"), resultSet.getBoolean("AllowAdd"), resultSet.getBoolean("AllowEdit"),
							resultSet.getBoolean("AllowDelete"), resultSet.getBoolean("AllowView"), resultSet
									.getInt("RightMaxWidth")));
				}
				headerId = resultSet.getInt("HeaderID");
				// System.out.println("HeaderId Set to: " +
				// headerId);
			}
			if (!(objRights == null)) {
				objlist.add(objRights);
			}
			loginSession.setRightsList(objlist);
			loginSession.setRespCode(200);
			return loginSession;
		} catch (Exception ex) {
			System.out.println(ex);
			return new LoginSession(500);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public LoginUser GetUserIdBio(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserBioManual);
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new LoginUser(resultSet.getInt("UserID"), resultSet.getInt("UserBioID"), 200);
			} else {
				return new LoginUser(201);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public LoginUser GetUserIdDevice(String userName, String password, String deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialDevice);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			preparedStatement.setString(3, deviceId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int userId = resultSet.getInt("UserID");
				if (resultSet.getBoolean("active") == false) {
					return new LoginUser(resultSet.getInt("UserID"), 201, "Device is Not Active", null);
				}
				List<Programme> programmes = GetAllUserServices(userId);
				System.out.println("services fetched###");
				return new LoginUser(resultSet.getInt("UserID"), 200, "Successfull Login", programmes);

			} else {
				return new LoginUser(resultSet.getInt("UserID"), 201, "Invalid Login Credential", null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(0, 201, "Invalid Login Credential", null);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Programme> GetAllUserServices(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeByUserID);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ProgrammeId");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.programmeValue = resultSet.getDouble("ProgrammeValue");
				programme.currency = resultSet.getString("currency");
				preparedStatement = connection.prepareStatement(Queryconstants.getServicesByProgrammes);
				preparedStatement.setInt(1, programme.programmeId);
				resultSet2 = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					Service objService = new Service();
					objService.serviceId = resultSet2.getInt("parentServiceId");
					objService.serviceName = resultSet2.getString("serviceName");
					// objService.serviceValue=resultSet2.getDouble("serviceValue");
					// objService.parentServiceId=resultSet2.getInt("parentServiceId");

					// preparedStatement = connection
					// .prepareStatement(Queryconstants.getActiveParams);
					// preparedStatement.setInt(1,
					// objService.parentServiceId);
					//
					// resultSet3 =
					// preparedStatement.executeQuery();
					// List<Params> params=new
					// ArrayList<Params>();
					// while (resultSet3.next()) {
					// params.add(new
					// Params(resultSet3.getInt("paramId"),resultSet3.getString("ParamName")));
					// }
					// objService.params=params;
					services.add(objService);

				}
				programme.services = services;
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

	public SafComResponse SafComLogin(String userName, String password, String deviceId, String userType, String licenseNumber) {
		System.out.println("username"+userName);
		System.out.println("password"+password);
		System.out.println("deviceid"+deviceId);
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		String posVersion = "";
		String ftpUrl = "";
		try {
			connection = dataSource.getConnection();
			//if (ValiodateDeviceLicense(deviceId, licenseNumber)) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSafComUser);
				preparedStatement.setString(1, userName);
				System.out.println(AES.encrypt(password));
				preparedStatement.setString(2, AES.encrypt(password));
				preparedStatement.setString(3, deviceId);
				preparedStatement.setString(4, userType);
				resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					int userId = resultSet.getInt("ID");
					if (resultSet.getBoolean("active") == false) {
						return new SafComResponse(201, "Device is not active", 0, "", "", "", "", "", 0);
					}
					// int userId =
					// resultSet.getInt("UserID");
					// if(resultSet.getBoolean("active")==false){
					// return new
					// LoginUser(resultSet.getInt("UserID"),
					// 201,
					// "Device is Not Active", null);
					// }
					// List<Programme> programmes =
					// GetAllUserServices(userId);
					// System.out.println("services fetched###");
					//preparedStatement1 = connection.prepareStatement(Queryconstants.getPOSVersion);

					//resultSet1 = preparedStatement1.executeQuery();
					//if (resultSet1.next()) {
					//	posVersion = resultSet1.getString("value");
					//}
					//preparedStatement2 = connection.prepareStatement(Queryconstants.getFTPUrl);

					//resultSet2 = preparedStatement2.executeQuery();
					//if (resultSet2.next()) {
						//ftpUrl = resultSet2.getString("value");
					//}
					return new SafComResponse(200, "Successfull Login", resultSet.getInt("ID"),
							resultSet.getString("UserFullName"), resultSet.getString("market"),
							resultSet.getString("paybill"), posVersion, ftpUrl, resultSet.getInt("agentid"));

				} else {
					return new SafComResponse(201, "Invalid Login Credential", 0, "", "", "", "", "", 0);
				}
			//}
			//else{
			//	return new SafComResponse(201, "Invalid License Number", 0, "", "", "", "", "", 0);
		//	}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new SafComResponse(400, "Exception", 0, "", "", "", "", "", 0);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public SafComResponse ChangePin(SafComLogin login) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		String posVersion = "";
		String ftpUrl = "";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.changePin);
			preparedStatement.setString(1, login.newPin);
			preparedStatement.setString(2, login.cardNumber);
			preparedStatement.setString(3, login.oldPin);

			// resultSet = preparedStatement.executeQuery();

			if (preparedStatement.executeUpdate() > 0) {
				System.out.println("###pin changed");
				return new SafComResponse(200, "Pin change Successfully", 0, "", "", "", "", "", 0);

			} else {
				return new SafComResponse(201, "Failed to change pin", 0, "", "", "", "", "", 0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new SafComResponse(201, "Failed to change pin", 0, "", "", "", "", "", 0);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean ValiodateAgentPin(int userid, String agentPin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkAgentPin);
			preparedStatement.setInt(1, userid);
			preparedStatement.setString(2, agentPin);

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

	public boolean ValiodateCardPin(String cardno, String cardpin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkCardPin);
			preparedStatement.setString(1, cardno);
			preparedStatement.setString(2, cardpin);

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

	public SafComResponse BlockCard(SafComLogin login) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		String posVersion = "";
		String ftpUrl = "";
		try {
			connection = dataSource.getConnection();
			if (ValiodateAgentPin(login.userId, login.agentPin) == false) {
				return new SafComResponse(201, "Agent Pin Invalid", 0, "", "", "", "", "", 0);
			}
			if (ValiodateCardPin(login.cardNumber, login.cardPin) == false) {
				return new SafComResponse(201, "Card Pin Invalid", 0, "", "", "", "", "", 0);
			}
			preparedStatement = connection.prepareStatement(Queryconstants.blockCard);

			preparedStatement.setString(1, login.cardNumber);

			// resultSet = preparedStatement.executeQuery();

			if (preparedStatement.executeUpdate() > 0) {
				System.out.println("###Card Block");
				return new SafComResponse(200, "Card Blocked Successfully", 0, "", "", "", "", "", 0);

			} else {
				return new SafComResponse(201, "Failed to block card", 0, "", "", "", "", "", 0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new SafComResponse(201, "Failed to block card", 0, "", "", "", "", "", 0);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public SafComResponse UnblockCard(SafComLogin login) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		String posVersion = "";
		String ftpUrl = "";
		try {
			connection = dataSource.getConnection();
			if (ValiodateAgentPin(login.userId, login.agentPin) == false) {
				return new SafComResponse(200, "Agent Pin Invalid", 0, "", "", "", "", "", 0);
			}
			if (ValiodateCardPin(login.cardNumber, login.cardPin) == false) {
				return new SafComResponse(200, "Card Pin Invalid", 0, "", "", "", "", "", 0);
			}
			preparedStatement = connection.prepareStatement(Queryconstants.unblockCard);

			preparedStatement.setString(1, login.cardNumber);

			// resultSet = preparedStatement.executeQuery();

			if (preparedStatement.executeUpdate() > 0) {
				System.out.println("###pin changed");
				return new SafComResponse(200, "Card UnBlocked Successfully", 0, "", "", "", "", "", 0);

			} else {
				return new SafComResponse(201, "Failed to unblock card", 0, "", "", "", "", "", 0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new SafComResponse(201, "Failed to block card", 0, "", "", "", "", "", 0);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean ValiodateDeviceLicense(String deviceId, String licenseNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.validateDeviceLicense);
			preparedStatement.setString(1, deviceId);
			preparedStatement.setString(2, licenseNumber);

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
}
