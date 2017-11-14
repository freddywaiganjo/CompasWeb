/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.VoucherDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public class VoucherDalImpl implements VoucherDal{
	private DataSource dataSource;

	public VoucherDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public List<Service> GetServiceProducts(String vType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceProducts);
			preparedStatement.setString(1, vType);
			resultSet = preparedStatement.executeQuery();
			List<Service> services = new ArrayList<Service>();
			while (resultSet.next()) {
				services.add(new Service(resultSet.getInt("ID"), resultSet
						.getString("serviceName"), resultSet
						.getString("serviceName"), resultSet
						.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200, resultSet.getString("componame"), resultSet
								.getString("compotype"),count,0,"","","",0,0));
				count++;
			}
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public CompasResponse UpdateVoucher(Voucher voucher) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int voucherId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

	        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"  
	        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
	        Date startdate = dt.parse(voucher.startDate);
	        Date enddate = dt.parse(voucher.endDate);

	        // *** same for the format String below
	        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			
			if (voucher.voucherId == 0) {
				if (CheckVoucherByCode(voucher.voucherCode)) {
					return new CompasResponse(201, "Voucher Code Already Exists");
				}
				if (CheckVoucherByName(voucher.voucherDesc)) {
					return new CompasResponse(201, "Voucher Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertVoucher,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, voucher.voucherCode);
				preparedStatement.setString(2, voucher.voucherDesc);
				preparedStatement.setString(3, voucher.voucherType);
				preparedStatement.setDouble(4, voucher.voucherValue);
				preparedStatement.setBoolean(5, voucher.active);
				preparedStatement.setInt(6, voucher.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8,dt1.format(startdate));
				preparedStatement.setString(9,dt1.format(enddate));
				
			} else {
				if (CheckVoucherNameByCode(voucher.voucherDesc,voucher.voucherCode)) {
					return new CompasResponse(201, "Voucher Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateVoucher);

				preparedStatement.setString(1, voucher.voucherDesc);
				preparedStatement.setString(2, voucher.voucherType);
				preparedStatement.setDouble(3, voucher.voucherValue);
				preparedStatement.setBoolean(4, voucher.active);
				preparedStatement.setInt(5, voucher.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(7, dt1.format(startdate));
				preparedStatement.setString(8, dt1.format(enddate));
				preparedStatement.setInt(9, voucher.voucherId);
				
				voucherId = voucher.voucherId;
			}
			if (preparedStatement.executeUpdate() > 0) {
				// Dispose
				if (voucher.voucherId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					voucherId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteVoucherServices);
				preparedStatement.setInt(1, voucherId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert branches
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertVoucherServices);
				for (int i = 0; i < voucher.services.size(); i++) {
					preparedStatement.setInt(1, voucherId);
					preparedStatement.setInt(2,
							voucher.services.get(i).serviceId);
					preparedStatement.setDouble(3,
							voucher.services.get(i).quantity);
					if(voucher.voucherType.equalsIgnoreCase("VA")){
						preparedStatement.setDouble(4,
								voucher.voucherValue);
					}else {
						preparedStatement.setDouble(4,
								voucher.services.get(i).serviceValue);
					}
				
					preparedStatement.setBoolean(5,
							voucher.services.get(i).isActive);
					preparedStatement.setInt(6,
							voucher.services.get(i).createdBy);
					preparedStatement.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(8, voucher.services.get(i).compoType);
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Service Id "
								+ voucher.services.get(i).serviceId);
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
				return new CompasResponse(201, "Programme Name Already Exists");
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

	public List<Voucher> GetAllVouchers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVouchers);

			resultSet = preparedStatement.executeQuery();
			List<Voucher> objVoucher = new ArrayList<Voucher>();
			while (resultSet.next()) {
				Voucher voucher = new Voucher();
				voucher.voucherId = resultSet.getInt("ID");
				voucher.voucherCode = resultSet.getString("voucher_Code");
				voucher.voucherDesc = resultSet.getString("voucher_Name");
				voucher.voucherType = resultSet.getString("voucher_type");
				voucher.voucherValue = resultSet.getDouble("voucher_value");
				voucher.active = resultSet.getBoolean("active");
				voucher.createdBy = resultSet.getInt("Created_By");
				voucher.startDate = resultSet.getString("start_date");
				voucher.endDate = resultSet.getString("end_date");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesByVoucherId);
				preparedStatement.setInt(1, voucher.voucherId);
				preparedStatement.setInt(2, voucher.voucherId);
				resultSet2 = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					services.add(new Service(resultSet2.getInt("Service_Id"),
							resultSet2.getString("serviceName"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getInt("Created_BY"), 200, resultSet2
									.getDouble("quantity"), resultSet2
									.getDouble("service_value"), resultSet2
									.getString("compoName"),resultSet2
									.getString("type")));
				}
				voucher.services = services;
				objVoucher.add(voucher);
			}
			return objVoucher;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	
	public List<Voucher> GetCashVouchers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCashVouchers);

			resultSet = preparedStatement.executeQuery();
			List<Voucher> objVoucher = new ArrayList<Voucher>();
			while (resultSet.next()) {
				Voucher voucher = new Voucher();
				voucher.voucherId = resultSet.getInt("ID");
				voucher.voucherCode = resultSet.getString("voucher_Code");
				voucher.voucherDesc = resultSet.getString("voucher_Name");
				voucher.voucherType = resultSet.getString("voucher_type");
				voucher.voucherValue = resultSet.getDouble("voucher_value");
				voucher.active = resultSet.getBoolean("active");
				voucher.createdBy = resultSet.getInt("Created_By");
				voucher.startDate = resultSet.getString("start_date");
				voucher.endDate = resultSet.getString("end_date");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesByVoucherId);
				preparedStatement.setInt(1, voucher.voucherId);
				preparedStatement.setInt(2, voucher.voucherId);
				resultSet2 = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					services.add(new Service(resultSet2.getInt("Service_Id"),
							resultSet2.getString("serviceName"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getInt("Created_BY"), 200, resultSet2
									.getDouble("quantity"), resultSet2
									.getDouble("service_value"), resultSet2
									.getString("compoName"),resultSet2
									.getString("type")));
				}
				voucher.services = services;
				objVoucher.add(voucher);
			}
			return objVoucher;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckVoucherByName(String voucherName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVoucherByName);
			preparedStatement.setString(1, voucherName);
	
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
	public boolean CheckVoucherByCode(String voucherCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVoucherByCode);
			preparedStatement.setString(1, voucherCode);
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
	public boolean CheckVoucherNameByCode(String voucherName,String voucherCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVoucherNameByCode);
			preparedStatement.setString(1, voucherName);
			preparedStatement.setString(2, voucherCode);
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
