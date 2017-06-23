package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.DeviceDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.License;
import com.compulynx.compas.models.User;

public class DeviceDalImpl implements DeviceDal{

	private DataSource dataSource;

	public DeviceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public boolean checkDevicedSerialNo(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceSerialNo);
			preparedStatement.setString(1, serialNo);

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
	public boolean checkDevicedSerialNoById(String serialNo,int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceSerialNoById);
			preparedStatement.setString(1, serialNo);
			preparedStatement.setInt(2, id);
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
	public boolean checkUserDeviceSerialNo(int userId,int deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserDeviceSerialNo);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, deviceId);
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
	public boolean checkDeviceExists(int regId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceExists);
			preparedStatement.setInt(1, regId);

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
	public boolean checkUserAssignDevice(int deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkUserAssignDevice);
			preparedStatement.setInt(1, deviceId);

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
	public CompasResponse UpdateDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (deviceInfo.active == false) {
				if (checkUserAssignDevice(deviceInfo.regId)) {
					return new CompasResponse(201, "Device Already Assign to user cannot deactivate");
				}
			}
			if (deviceInfo.regId == 0) {
				if (checkDevicedSerialNo(deviceInfo.serialNo)) {
					return new CompasResponse(201, "SerialNo Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertDeviceInfo);
				preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(2, deviceInfo.active);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (checkDevicedSerialNoById(deviceInfo.serialNo,deviceInfo.regId)) {
					return new CompasResponse(201, "SerialNo Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateDeviceInfo);
				preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(2, deviceInfo.active);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, deviceInfo.regId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Serial No Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection,preparedStatement,resultSet);
		}
	}


	public List<Device> GetAllDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("ID"), resultSet
						.getString("SerialNo"), resultSet
						.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200));
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Device> GetActiveDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("ID"), resultSet
						.getString("SerialNo"), resultSet
						.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200));
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateIssueDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if(deviceInfo.regId!=-1)
			{
			if (checkDeviceExists(deviceInfo.regId)) {
				if (checkUserDeviceSerialNo(deviceInfo.agentId,deviceInfo.regId)) {
					return new CompasResponse(201, "Agent already exists with device,Return the device and assign the new ones");
				}
				else
				{
				return new CompasResponse(201, "Device already assign to agent");
				}
			}
			
			}
			if (deviceInfo.issueId == 0) {
				

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertIssueDeviceInfo);
				preparedStatement.setInt(1, deviceInfo.regId);
				preparedStatement.setInt(2, deviceInfo.agentId);
				preparedStatement.setInt(3, deviceInfo.branchId);
				preparedStatement.setInt(4, deviceInfo.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(6, deviceInfo.licenseNumber);
			} else {
//				if(deviceInfo.regId!=-1){
//				if (checkDeviceExists(deviceInfo.regId)) {
//					return new MrmResponse(201, "Device already assign to another user");
//				}
//				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateIssueDeviceInfo);
				preparedStatement.setInt(1, deviceInfo.regId);
				preparedStatement.setInt(2, deviceInfo.agentId);
				preparedStatement.setInt(3, deviceInfo.branchId);
				preparedStatement.setInt(4, deviceInfo.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, deviceInfo.issueId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Serial No Already Exists");
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
	

	public List<Device> GetAllIssueDevicesInfo(int classId,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getIssueDevices);
			//preparedStatement.setInt(1, classId);
			//preparedStatement.setInt(2, merchantId);
			resultSet = preparedStatement.executeQuery();
			List<Device> issueDevices = new ArrayList<Device>();
			while (resultSet.next()) {
				issueDevices.add(new Device(resultSet.getInt("ID"),resultSet.getInt("deviceRegId"), resultSet
						.getString("SerialNo"),resultSet.getInt("agentId"), resultSet
						.getString("agentDesc"), resultSet.getInt("CreatedBy"),
						200,0,resultSet.getString("license"),false));
			}
			return issueDevices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Agent> GetActiveAgents(int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveAgents);
			//preparedStatement.setInt(1, branchId);
			resultSet = preparedStatement.executeQuery();
			List<Agent> agents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent objAgent = new Agent();
				objAgent.agentId = resultSet.getInt("ID");
				objAgent.agentDesc = resultSet.getString("AgentDesc");
				
				agents.add(objAgent);
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<License> GetLicense() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLicense);
			//preparedStatement.setInt(1, branchId);
			resultSet = preparedStatement.executeQuery();
			List<License> agents = new ArrayList<License>();
			while (resultSet.next()) {
				License objAgent = new License();
				objAgent.description = resultSet.getString("description");
				objAgent.license = resultSet.getString("licensenumber");
				
				agents.add(objAgent);
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}
