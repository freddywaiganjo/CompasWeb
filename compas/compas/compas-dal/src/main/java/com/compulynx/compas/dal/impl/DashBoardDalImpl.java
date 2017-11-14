package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.DashBoardDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.DashBoard;

public class DashBoardDalImpl implements DashBoardDal {


	private DataSource dataSource;

	public DashBoardDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public List<DashBoard> GetDashBoardCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDashBoardDetailCount);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("COUNTNO"), resultSet
						.getString("Name"),
						 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<DashBoard> GetDashBoardAgentDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentTxns);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("amount"), resultSet
						.getString("agent"),
						 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<DashBoard> GetTransChartDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTransChartDetail);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> chartDetail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				chartDetail.add(new DashBoard(resultSet.getInt("Load"),
						resultSet.getInt("Purchase"),
						resultSet.getInt("totalTrans"),
						resultSet
						.getString("month"),
						 200));
			}
			return chartDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<DashBoard> GetDashBoardAmountDetail() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAmountDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("countno"));
			//System.out.println("Amount##"+obj.amount);
			detail.add(obj);
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets the service details for piechart
	 * @return list of services and the percentage of transaction done
	 */
	public List<DashBoard> GetFlowChartCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getFlowChartDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getString("service"), resultSet
						.getInt("trans_count")));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets details of total collections
	 * @return list of total collections 
	 */
	public List<DashBoard> GetDashBoardCollectionDetail() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTotalCollectionInfo);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("collection"));
			obj.totalTxns=resultSet.getDouble("collection");
			detail.add(obj);
			}
			
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<DashBoard> GetPenNotifications() {
		Connection connection = null;
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
	int pendingCount=0;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPenNotificationDetails);
			/*preparedStatement1 = connection
					.prepareStatement(Queryconstants.getCountNoti);
			resultSet1 = preparedStatement1.executeQuery();
			if(resultSet1.next()){
				pendingCount=0;
			}*/
			resultSet = preparedStatement.executeQuery();
			List<DashBoard> chartDetail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				pendingCount++;
				//PrettyTime p = new PrettyTime();
				//System.out.println(p.format(resultSet.getDate("createdon")));
				DashBoard objDash = new DashBoard();
				objDash.pendingAuthCount = pendingCount;
				objDash.locationName = resultSet.getString("location_name");
				objDash.date = resultSet.getString("created_on");
				objDash.topupValue = resultSet.getDouble("topup_value");
				objDash.topupValueDisplay = df.format(objDash.topupValue);
				chartDetail.add(objDash);
			}
			return chartDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
