/**
 * 
 */
package com.compulynx.compas.dal.impl;

import com.compulynx.compas.dal.ProgrammeDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.*;

import javax.sql.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Anita
 *
 */
public class ProgrammeDalImpl implements ProgrammeDal {

	private DataSource dataSource;

	public ProgrammeDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean checkProgrammeName(String programmeDesc) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammeByName);
			preparedStatement.setString(1, programmeDesc);

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

	public CompasResponse UpdateProgramme(Programme programme) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int programmeId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			 SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		        Date startdate = dt.parse(programme.startDate);
		        Date enddate = dt.parse(programme.endDate);

		        // *** same for the format String below
		        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			if (programme.programmeId == 0) {
				if (CheckProgrammeCode(programme.programmeCode)) {
					return new CompasResponse(201,
							"Programme Code Already Exists");
				}
				if (checkProgrammeName(programme.programmeDesc)) {
					return new CompasResponse(201,
							"Programme Name Already Exists");
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertProgrammeDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, programme.programmeCode);
				preparedStatement.setString(2, programme.programmeDesc);
				preparedStatement.setBoolean(3, programme.active);
				preparedStatement.setInt(4, programme.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, programme.productId);
				preparedStatement.setString(7, dt1.format(startdate));
				preparedStatement.setString(8, dt1.format(enddate));
				preparedStatement.setString(9, getItms(programme.itmList));
				preparedStatement.setString(10, getChtms(programme.chtmList));
				preparedStatement.setString(11, getIntms(programme.intmList));
				preparedStatement.setString(12, programme.programmeType);
			} else {
				if (CheckProgrammeNameByCode(programme.programmeDesc,
						programme.programmeCode)) {
					return new CompasResponse(201,
							"Programme Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateProgrammeDetails);
				preparedStatement.setString(1, programme.programmeDesc);
				preparedStatement.setBoolean(2, programme.active);
				preparedStatement.setInt(3, programme.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(5, dt1.format(startdate));
				preparedStatement.setString(6, dt1.format(enddate));
				preparedStatement.setString(7, getItms(programme.itmList));
				preparedStatement.setString(8, getChtms(programme.chtmList));
				preparedStatement.setString(9, getIntms(programme.intmList));
				preparedStatement.setInt(10, programme.productId);
				preparedStatement.setInt(11, programme.programmeId);
				programmeId = programme.programmeId;
			}
			if (preparedStatement.executeUpdate() > 0) {
				// Dispose
				if (programme.programmeType.equalsIgnoreCase("D") || programme.programmeType.equalsIgnoreCase("CT")){
					if (programme.programmeId == 0) {
						rs = preparedStatement.getGeneratedKeys();
						rs.next();
						programmeId = rs.getInt(1);
					}
					DBOperations.DisposeSql(preparedStatement, rs);
					preparedStatement = connection
							.prepareStatement(Queryconstants.deleteProgrammeVouchers);
					preparedStatement.setInt(1, programmeId);
					preparedStatement.executeUpdate();

					DBOperations.DisposeSql(preparedStatement);
					// insert branches
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertProgrammeVouchers);
					for (int i = 0; i < programme.vouchers.size(); i++) {
						preparedStatement.setInt(1, programmeId);
						preparedStatement.setInt(2,
								programme.vouchers.get(i).voucherId);
						preparedStatement.setString(3,
								programme.vouchers.get(i).frqSelect);
						preparedStatement.setBoolean(4,
								programme.vouchers.get(i).isActive);
						preparedStatement.setInt(5,
								programme.vouchers.get(i).createdBy);
						preparedStatement.setTimestamp(
								6,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement.setString(7,
								programme.vouchers.get(i).modeSelect);
						if (preparedStatement.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Voucher Id "
									+ programme.vouchers.get(i).voucherId);
						}
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

	private String getItms(List<ItmModes> itmModes) {
		String result = "";

		for (int i = 0; i < itmModes.size(); i++) {
			if (i != itmModes.size() - 1)
				result += itmModes.get(i).itmValue + ",";

			if (i == itmModes.size() - 1)
				result += itmModes.get(itmModes.size() - 1).itmValue;

		}

		return result;

	}

	private String getChtms(List<ChtmModes> chtmModes) {
		String result = "";

		for (int i = 0; i < chtmModes.size(); i++) {
			if (i != chtmModes.size() - 1)
				result += chtmModes.get(i).chtmValue + ",";

			if (i == chtmModes.size() - 1)
				result += chtmModes.get(chtmModes.size() - 1).chtmValue;
		}

		return result;

	}

	private String getIntms(List<IntmModes> intmModes) {
		String result = "";

		for (int i = 0; i < intmModes.size(); i++) {
			if (i != intmModes.size() - 1)
				result += intmModes.get(i).intmValue + ",";
			if (i == intmModes.size() - 1)
				result += intmModes.get(intmModes.size() - 1).intmValue;
		}

		return result;

	}

	/**
	 * 
	 */
	public List<Programme> GetAllProgrammes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammes);

			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.productName = resultSet.getString("product_name");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startDate = sdf.format(sdf.parse(resultSet
						.getString("start_date")));
				String endDate = sdf.format(sdf.parse(resultSet
						.getString("end_date")));
				programme.startDate = startDate;
				programme.endDate = endDate;
				programme.active = resultSet.getBoolean("active");
				programme.createdBy = resultSet.getInt("CreatedBy");
				programme.itmModes = resultSet.getString("itm_modes");
				programme.chtmModes = resultSet.getString("chtm_modes");
				programme.intmModes = resultSet.getString("intm_modes");
				programme.programmeType = resultSet.getString("prg_type");
				programme.count = count;
				if(programme.programmeType.equalsIgnoreCase("CT")){
					preparedStatement = connection
							.prepareStatement(Queryconstants.getCashVouchersByProgrammeId);
				
				}else{
				preparedStatement = connection
						.prepareStatement(Queryconstants.getVouchersByProgrammeId);
				
				}
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
									.getString("frq_type")));
				}
				programme.vouchers = services;
				objProgrammes.add(programme);
				count++;
			}
			return objProgrammes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Programme GetProgrammeById(int programmeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammeById);
			preparedStatement.setInt(1, programmeId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new Programme(resultSet.getInt("ProgrammeID"),
						resultSet.getString("ProgrammeCode"),
						resultSet.getString("ProgrammeName"),
						resultSet.getBoolean("active"),
						resultSet.getInt("CreatedBy"), 200);
			} else {
				return new Programme(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckProgrammeCode(String programmeCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammeByCode);
			preparedStatement.setString(1, programmeCode);

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

	public boolean CheckProgrammeNameByCode(String programmeName,
			String programmeCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProgrammeNameByCode);
			preparedStatement.setString(1, programmeName);
			preparedStatement.setString(2, programmeCode);
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
