/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.TransactionDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SafComTxns;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public class TransactionDalImpl implements TransactionDal {

	private DataSource dataSource;

	public TransactionDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public CompasResponse UpdateTransaction(List<Transaction> txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int txnId = 0;
		try {
			connection = dataSource.getConnection();
			// connection.setAutoCommit(false);

			preparedStatement = connection.prepareStatement(
					Queryconstants.insertTransactionDetails,
					Statement.RETURN_GENERATED_KEYS);
			System.out.println(txn.size());
			for (int i = 0; i < txn.size(); i++) {
				// preparedStatement.setInt(1, txn.txnType);
				preparedStatement.setString(1, txn.get(i).cardNo);
				preparedStatement.setString(2, txn.get(i).billNo);
				preparedStatement.setString(3, txn.get(i).txnAmount);
				preparedStatement.setString(4, txn.get(i).runningBalance);
				preparedStatement.setString(5, txn.get(i).subServiceId);
				preparedStatement.setString(6, txn.get(i).serviceId);
				preparedStatement.setString(7, txn.get(i).deviceId);
				preparedStatement.setString(8, txn.get(i).userId);
				// preparedStatement.setString(10, txn.txnTime);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(10, txn.get(i).programmeId);
				preparedStatement.setString(11, txn.get(i).paymentmethod);
				if (preparedStatement.executeUpdate() > 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					txnId = rs.getInt(1);
					if (txn.get(i).params.size() > 0) {
						System.out.println(txn.get(i).params.size());
						System.out.println(txn.get(i).params);
						// DBOperations.DisposeSql(preparedStatement);
						preparedStatement2 = connection
								.prepareStatement(Queryconstants.insertTransactionParamDetails);
						for (int x = 0; x < txn.get(i).params.size(); x++) {
							preparedStatement2.setInt(1, txnId);
							preparedStatement2.setInt(2,
									txn.get(i).params.get(x).paramId);
							preparedStatement2.setString(3,
									txn.get(i).params.get(x).paramValue);
							preparedStatement2.setString(4, txn.get(i).userId);
							preparedStatement2.setTimestamp(
									5,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							if (preparedStatement2.executeUpdate() <= 0) {
								throw new Exception(
										"Failed to Insert Param Id "
												+ txn.get(i).params.get(x).paramId);
							}
						}

					}
					// DBOperations.DisposeSql(preparedStatement,
					// resultSet);
					// preparedStatement =
					// connection.prepareStatement(
					// Queryconstants.updateCardBalance);
					// preparedStatement.setString(1,
					// txn.get(i).runningBalance);
					// preparedStatement.setString(2,
					// txn.get(i).programmeId);
					// preparedStatement.setString(3,
					// txn.get(i).cardNo);
					// preparedStatement.executeUpdate();
					// connection.commit();
					// return new CompasResponse(200,
					// "Records Updated");

				} else {
					// connection.rollback();
					return new CompasResponse(202, "Nothing To Update");
				}
			}

			// preparedStatement.setString(12, txn.createdOn);
			// System.out.println(txn.txnTime);

			// insert rights
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new CompasResponse(201, "Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(201, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateSafComTxn(SafComTxns txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet rs = null;
		int txnId = 0;
		double txnValue = 0;
		String cust_name = "";
		try {
			connection = dataSource.getConnection();

			if (ValiodateCard(txn.cardNumber) == false) {
				return new CompasResponse(201, "Card Is Not Active", 0, 0, "");
			}

			if (txn.txnType.equalsIgnoreCase("1") == true) {
				if (ValiodateAgentPin(txn.userId, txn.agentPin) == false) {
					return new CompasResponse(201, "Agent Pin Invalid", 0, 0,
							"");
				}
			}
			if (ValiodateCardPin(txn.cardNumber, txn.cardPin) == false) {
				return new CompasResponse(201, "Card Pin Invalid", 0, 0, "");
			}
			if (txn.channel.equalsIgnoreCase("account")) {
				cust_name = GetCustName(txn.cardNumber);
			}
			if (txn.txnType.equalsIgnoreCase("2") == true) {
				if (txn.amount > ValiodateCardBalance(txn.cardNumber,
						txn.cardPin)) {
					return new CompasResponse(201,
							"Amount is greater than card balance", 0, 0, "");
				} else {
					if (txn.txnType.equalsIgnoreCase("3") == true) {
						preparedStatement2 = connection
								.prepareStatement(Queryconstants.getReverseTxn);
						preparedStatement2.setInt(1, txn.txnId);

						resultSet1 = preparedStatement2.executeQuery();

						if (resultSet1.next()) {
							txnValue = resultSet1.getDouble("txnvalue");
							txn.amount = txnValue;
						} else {
							return new CompasResponse(201,
									"Transaction can not be reversed", 0, 0, "");
						}
					}

					preparedStatement = connection.prepareStatement(
							Queryconstants.insertSafComTopTxn,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, txn.cardNumber);
					preparedStatement.setDouble(2, txn.amount);
					preparedStatement.setString(3, txn.deviceId);
					preparedStatement.setInt(4, txn.userId);
					preparedStatement.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(6, txn.txnType);
					preparedStatement.setInt(7, txn.txnId);
					preparedStatement.setString(8, txn.paybillNo);
					preparedStatement.setString(9, txn.accNo);
					preparedStatement.setString(10, txn.channel);
					preparedStatement.setString(11, txn.utility);
					if (preparedStatement.executeUpdate() > 0) {
						if ((txn.txnType.equalsIgnoreCase("1") == true)
								|| (txn.txnType.equalsIgnoreCase("3") == true)) {
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.insertSafComTopUp);
							preparedStatement1.setDouble(1, txn.amount);
							preparedStatement1.setString(2, txn.cardNumber);
						}
						if ((txn.txnType.equalsIgnoreCase("2") == true)
								|| (txn.txnType.equalsIgnoreCase("4") == true)) {
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.insertSafCom);
							preparedStatement1.setDouble(1, txn.amount);
							preparedStatement1.setString(2, txn.cardNumber);
						}
					}
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					txnId = rs.getInt(1);
					return (preparedStatement1.executeUpdate() > 0) ? new CompasResponse(
							200, "Transaction Saved Successfully", txnId,
							txnValue, cust_name) : new CompasResponse(201,
							"Failed to save Transaction", 0, 0, "");
				}
			} else {
				if (txn.txnType.equalsIgnoreCase("3") == true) {
					preparedStatement2 = connection
							.prepareStatement(Queryconstants.getReverseTxn);
					preparedStatement2.setInt(1, txn.txnId);

					resultSet1 = preparedStatement2.executeQuery();

					if (resultSet1.next()) {
						txnValue = resultSet1.getDouble("txnvalue");
						txn.amount = txnValue;
					}
				}
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertSafComTopTxn,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, txn.cardNumber);
				preparedStatement.setDouble(2, txn.amount);
				preparedStatement.setString(3, txn.deviceId);
				preparedStatement.setInt(4, txn.userId);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(6, txn.txnType);
				preparedStatement.setInt(7, txn.txnId);
				preparedStatement.setString(8, txn.paybillNo);
				preparedStatement.setString(9, txn.accNo);
				preparedStatement.setString(10, txn.channel);
				preparedStatement.setString(11, txn.utility);
				if (preparedStatement.executeUpdate() > 0) {
					if ((txn.txnType.equalsIgnoreCase("1") == true)
							|| (txn.txnType.equalsIgnoreCase("3") == true)) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.insertSafComTopUp);
						preparedStatement1.setDouble(1, txn.amount);
						preparedStatement1.setString(2, txn.cardNumber);
					}
					if ((txn.txnType.equalsIgnoreCase("2") == true)
							|| (txn.txnType.equalsIgnoreCase("4") == true)) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.insertSafCom);
						preparedStatement1.setDouble(1, txn.amount);
						preparedStatement1.setString(2, txn.cardNumber);
					}
				}
				rs = preparedStatement.getGeneratedKeys();
				rs.next();
				txnId = rs.getInt(1);
				return (preparedStatement1.executeUpdate() > 0) ? new CompasResponse(
						200, "Transaction Saved Successfully", txnId, txnValue,
						cust_name) : new CompasResponse(201,
						"Failed to save Transaction", 0, 0, "");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new CompasResponse(201, "Exception Occured", 0, 0, "");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(201, "Exception Occured");
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
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkAgentPin);
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

	public String GetCustName(String cardnumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String name = "";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCustName);

			preparedStatement.setString(1, cardnumber);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				return name = resultSet.getString("name");
			} else {
				return name;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return name;
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
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCardPin);
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

	public boolean ValiodateCard(String cardno) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCard);
			preparedStatement.setString(1, cardno);

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

	public double ValiodateCardBalance(String cardno, String cardpin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double cardBalance = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCardPin);
			preparedStatement.setString(1, cardno);
			preparedStatement.setString(2, cardpin);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				cardBalance = resultSet.getDouble("balance");
				System.out.println();
				return cardBalance;
			} else {
				return 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<SafComTxns> GetAllTxns(String cardnumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllSafComTxns);
			preparedStatement.setString(1, cardnumber);
			resultSet = preparedStatement.executeQuery();
			List<SafComTxns> txns = new ArrayList<SafComTxns>();
			while (resultSet.next()) {

				SafComTxns obj = new SafComTxns();
				obj.txnType = resultSet.getString("txntype");
				obj.cardNumber = resultSet.getString("cardno");
				obj.amount = resultSet.getDouble("txnvalue");
				obj.deviceId = resultSet.getString("deviceid");
				obj.txnDate = resultSet.getString("createdon");
				obj.channel = resultSet.getString("channel_type");
				obj.utility = resultSet.getString("utility");
				obj.userFullName=resultSet.getString("userFullname");
				obj.count = count++;

				txns.add(obj);

			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetAgentList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentList);

			resultSet = preparedStatement.executeQuery();
			List<Agent> txns = new ArrayList<Agent>();
			while (resultSet.next()) {

				Agent obj = new Agent();
				obj.agentId = resultSet.getInt("id");
				obj.agentDesc = resultSet.getString("agentdesc");

				obj.count = count++;

				txns.add(obj);

			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Device> GetDeviceList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceList);

			resultSet = preparedStatement.executeQuery();
			List<Device> txns = new ArrayList<Device>();
			while (resultSet.next()) {

				Device obj = new Device();
				obj.regId = resultSet.getInt("id");
				obj.serialNo = resultSet.getString("serialNo");

				obj.count = count++;

				txns.add(obj);

			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
