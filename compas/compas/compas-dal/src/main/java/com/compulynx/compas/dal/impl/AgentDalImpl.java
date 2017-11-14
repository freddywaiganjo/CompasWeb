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

import com.compulynx.compas.dal.AgentDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;

/**
 * @author Anita
 *
 */
public class AgentDalImpl implements AgentDal{

	private DataSource dataSource;

	public AgentDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	


	public boolean CheckAgentName(String agentDesc,int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentByName);
			preparedStatement.setString(1, agentDesc);
			preparedStatement.setInt(2, branchId);
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
	public boolean CheckAgentByCode(String agentCode,int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentByCode);
			preparedStatement.setString(1, agentCode);
			preparedStatement.setInt(2, branchId);
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
	public boolean CheckAgentNameByCode(String agentDesc,String agentCode,int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentNameByCode);
			preparedStatement.setString(1, agentDesc);
			preparedStatement.setString(2, agentCode);
			preparedStatement.setInt(3, branchId);
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

	public CompasResponse UpdateAgent(Agent agent) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int agentId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		
			if (agent.agentId == 0) {
				if (CheckAgentByCode(agent.agentCode, agent.locationId)) {
					return new CompasResponse(201, "Agent Code Already Exists");
				}
				if (CheckAgentName(agent.agentDesc, agent.locationId)) {
					return new CompasResponse(201, "Agent Name Already Exists");
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertAgentDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, agent.agentDesc);
				preparedStatement.setInt(2,agent.merchantId);
				preparedStatement.setBoolean(3, agent.active);
				preparedStatement.setInt(4, agent.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(6, agent.agentCode);
				preparedStatement.setInt(7, agent.locationId);
				preparedStatement.setString(8, agent.rationNo);
				preparedStatement.setInt(8, agent.vendorTypeId);
				preparedStatement.setString(9, agent.houseNo);
				preparedStatement.setString(10, agent.section);
				preparedStatement.setString(11, agent.phoneNo);
			} else {
				if (CheckAgentNameByCode(agent.agentDesc,agent.agentCode, agent.locationId)) {
					return new CompasResponse(201, "Agent name already exists for specified location");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateAgentDetails);
				preparedStatement.setString(1, agent.agentDesc);
				preparedStatement.setInt(2,agent.merchantId);
				preparedStatement.setBoolean(3, agent.active);
				preparedStatement.setInt(4, agent.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, agent.locationId);
				preparedStatement.setString(7, agent.rationNo);
				preparedStatement.setInt(8, agent.vendorTypeId);
				preparedStatement.setString(9, agent.houseNo);
				preparedStatement.setString(10, agent.section);
				preparedStatement.setString(11, agent.phoneNo);
				preparedStatement.setInt(12, agent.agentId);
				
				agentId=agent.agentId;
			}
			if(preparedStatement.executeUpdate()>0)
			{
				// Dispose

				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");
			
			}
	
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

	public List<Agent> GetAllAgents() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgents);

			
			resultSet = preparedStatement.executeQuery();
			List<Agent> objAgents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent agent= new Agent();
				agent.agentId=resultSet.getInt("ID");
				agent.agentCode=resultSet.getString("Agent_Code");
				agent.agentDesc=resultSet.getString("AgentDesc");
				agent.merchantId=resultSet.getInt("merchantId");
				agent.locationId=resultSet.getInt("locationid");
				agent.locationName=resultSet.getString("location_name");
				agent.rationNo=resultSet.getString("ration_no");
				agent.active=resultSet.getBoolean("active");
				agent.vendorTypeId=resultSet.getInt("vendor_type_id");
				agent.houseNo=resultSet.getString("house_no");
				agent.section=resultSet.getString("section");
				agent.phoneNo=resultSet.getString("mobile_no");
				agent.count=count;
				if(agent.vendorTypeId==1){
					//Get Device
					preparedStatement2=connection.prepareStatement(Queryconstants.getVendorDevice);
					preparedStatement2.setInt(1, agent.agentId);
					resultSet1=preparedStatement2.executeQuery();
					if(resultSet1.next()){
						agent.device=resultSet1.getString("serialno");
					}
					
					/*preparedStatement1=connection.prepareStatement(Queryconstants.getDetailsVendor);
					preparedStatement1.setString(1, agent.rationNo);
					resultSet2=preparedStatement1.executeQuery();
					if(resultSet2.next()){
						agent.houseNo=resultSet2.getString("height");
						agent.section=resultSet2.getString("BranchName");
						agent.phoneNo=resultSet2.getString("mobileno");
						//agent.device=resultSet2.getString("");
					}*/
				}

				objAgents.add(agent);
				count++;
			}
			return objAgents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Branch> GetBranchesByMerchant(int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchesByMerchant);
			preparedStatement.setInt(1, merchantId);
			
			resultSet = preparedStatement.executeQuery();
			List<Branch> objBranch = new ArrayList<Branch>();
			while (resultSet.next()) {
				Branch branch= new Branch();
				branch.branchId=resultSet.getInt("Id");
				branch.branchName=resultSet.getString("branchName");
				branch.active=resultSet.getBoolean("active");
				objBranch.add(branch);
			}
			return objBranch;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Location> GetLocationsByMerchant(int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLocationByMerchant);
			preparedStatement.setInt(1, merchantId);
			
			resultSet = preparedStatement.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while (resultSet.next()) {
				Location location= new Location();
				location.locationId=resultSet.getInt("Id");
				location.locationName=resultSet.getString("location_Name");
				locations.add(location);
			}
			return locations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
}
