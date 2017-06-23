/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.MerchantDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;


/**
 * @author ANita
 *
 */
public class MerchantDalImpl implements MerchantDal{
	private DataSource dataSource;

	public MerchantDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}


	public CompasResponse UpdateMerchant(Merchant merchant) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (merchant.merchantId == 0) {
				if (CheckMerchantByCode(merchant.merchantCode))
				{
					return new CompasResponse(201, "Merchant Code Already Exists");
				}
				if (CheckMerchantByName(merchant.merchantName))
				{
					return new CompasResponse(201, "Merchant Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertMerchant);
				preparedStatement.setString(1, merchant.merchantName);
				preparedStatement.setBoolean(2, merchant.active);
				preparedStatement.setInt(3, merchant.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(5, merchant.merchantCode);
			
			} else {
				if (CheckMerchantNameByCode(merchant.merchantName,merchant.merchantCode))
				{
					return new CompasResponse(201, "Merchant Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateMerchant);
				preparedStatement.setString(1, merchant.merchantName);
				preparedStatement.setBoolean(2, merchant.active);
				preparedStatement.setInt(3, merchant.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(5, merchant.merchantCode);
				preparedStatement.setInt(6, merchant.merchantId);
			
			
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			
				return new CompasResponse(404, "Exception Occured");	
			
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckMerchantByCode(String merchantCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMerchantByCode);
			preparedStatement.setString(1, merchantCode);
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

	public boolean CheckMerchantByName(String merchantName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMerchantByName);
			preparedStatement.setString(1, merchantName);
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
	public boolean CheckMerchantNameByCode(String merchantName,String merchantCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMerchantNameByCode);
			preparedStatement.setString(1, merchantName);
			preparedStatement.setString(2, merchantCode);
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

	public List<Merchant> GetMerchants() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMerchants);
			resultSet = preparedStatement.executeQuery();
			List<Merchant> merchants = new ArrayList<Merchant>();
			while (resultSet.next()) {
				merchants.add(new Merchant(resultSet.getInt("ID"), resultSet
						.getString("merchantName"),resultSet.getBoolean("Active"),
						resultSet.getInt("CreatedBy"), 200,resultSet
						.getString("merchantcode"),count));
				count++;
			}
			return merchants;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	

}
