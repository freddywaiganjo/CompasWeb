/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.compulynx.compas.dal.VoucherDal;
import com.compulynx.compas.dal.VoucherTopupDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.dal.operations.Utility;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.Commodity;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TopupLimit;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.VoucherVO;
import com.google.gson.Gson;

/**
 * @author Anita Aug 15, 2016
 */
public class VoucherTopupDalImpl implements VoucherTopupDal {
	private static final Logger logger = Logger
			.getLogger(VoucherTopupDalImpl.class.getName());

	private DataSource dataSource;

	public VoucherTopupDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public long generateNumber() {
		return (long) (Math.random() * 100000 + 3333300000L);
	}

	/**
	 * @author kibet returns list of vouchers by programmeId
	 * @param programmeId
	 * @return list of vouchers
	 */
	public List<Topup> getTopupVouchersByProgram(Topup topup) {
		Connection connection = null;
		// PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			List<Topup> vouchers = new ArrayList<Topup>();
			for (int i = 0; i < topup.prgTopupDetails.size(); i++) {
				logger.info("ProgrammeId$$"
						+ topup.prgTopupDetails.get(i).programmeId);

				/**
				 * commented by Anita 16august2016
				 */
				// String sql =
				// "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,\n"
				// +
				// "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n"
				// +
				// "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type\n"
				// + "from BeneficiaryGroup b ,ProgrammeMaster p,\n"
				// + "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,\n"
				// +
				// "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n"
				// +
				// " where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 \n"
				// +
				// " and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId\n"
				// +
				// "   and vm.id = vd.voucher_id and vd.isActive=1 and p.Id in("
				// + programmeId + ")";
				/**
				 * modified by Anita 16august2016
				 */
				String sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc, "
						+ "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value, "
						+ "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
						+ "sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type  "
						+ "from BeneficiaryGroup b ,ProgrammeMaster p,  "
						+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
						+ "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm  "
						+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 "
						+ "and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId "
						+ "and vm.id = vd.voucher_id and vd.isActive=1 and p.Id="
						+ topup.prgTopupDetails.get(i).programmeId
						+ " "
						+ "and m.bnfGrpId=b.id and m.ID=ci.CustomerId and m.active=1 "
						+ "order by b.id";

				connection = dataSource.getConnection();
				statement = connection.createStatement();
				// preparedStatement =
				// connection.prepareStatement(Queryconstants.getVoucherDetailsByProgramme);
				// preparedStatement.setString(1,programmeId);
				resultSet = statement.executeQuery(sql);

				while (resultSet.next()) {
					Topup objtopup = new Topup();
					objtopup.programme_id = resultSet.getInt("programmeId");
					objtopup.programme_name = resultSet
							.getString("programmeDesc");
					objtopup.voucher_id = resultSet.getString("voucher_id");
					objtopup.service_id = resultSet.getInt("service_id");
					objtopup.service_quantity = resultSet.getDouble("quantity");
					objtopup.voucher_type = resultSet.getString("voucher_type");

					objtopup.beneficiary_group_id = resultSet.getInt("bgId");
					objtopup.beneficiary_id = resultSet.getInt("beneficiaryId");
					objtopup.uom = resultSet.getString("uom");
					objtopup.card_number = resultSet.getString("cardNumber");
					if (objtopup.voucher_type.equalsIgnoreCase("VA")) {
						objtopup.service_value = CalcTopupValueIndividual(
								objtopup.beneficiary_id,
								resultSet.getDouble("house_hold_value"),
								objtopup.voucher_type, topup.programme_id,
								topup.location_id);
					} else if (objtopup.voucher_type.equalsIgnoreCase("CA")) {
						objtopup.service_value = CalcTopupValueIndividual(
								objtopup.beneficiary_id,
								resultSet.getDouble("house_hold_value"),
								objtopup.voucher_type, topup.programme_id,
								topup.location_id);
					} else if (objtopup.voucher_type.equalsIgnoreCase("CM")) {
						objtopup.service_value = resultSet
								.getDouble("service_value");
					}

					vouchers.add(objtopup);
				}

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
	public List<Topup> getTopupVouchersByCardNumber(Topup topup) {
		logger.info("CardNumber" + topup.idPassportNo);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// commented by anita 22Aug2016
		// String sql =
		// "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc, "
		// + "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value, "
		// + "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
		// + "sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type  "
		// + "from BeneficiaryGroup b ,ProgrammeMaster p,  "
		// +
		// "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
		// + "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm  "
		// +
		// "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 "
		// +
		// "and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId "
		// + "and vm.id = vd.voucher_id and vd.isActive=1 and  ci.CardNo = ? "
		// + "and m.bnfGrpId=b.id and m.ID=ci.CustomerId "
		// + "order by b.id";
		String sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,  "
				+ "vm.id as voucher_id,voucher_value service_value, "
				+ "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
				+ "ci.CardNo as cardNumber,vm.voucher_type  "
				+ "from BeneficiaryGroup b ,ProgrammeMaster p, "
				+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
				+ "CardIssuance ci,Voucher_mst vm "
				+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 "
				+ "and pv.ProgrammeId=p.Id and vm.id=pv.voucherId and ci.CustomerId=c.CustomerId "
				+ " and m.bnfGrpId=b.id and m.ID=ci.CustomerId and   m.idppno=? and m.active=1 "
				+ " order by b.id ";

		// String sql =
		// "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n"
		// +
		// "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type from BeneficiaryGroup b ,\n"
		// +
		// "ProgrammeMaster p,CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n"
		// +
		// "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId\n"
		// +
		// " and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId and vm.id = vd.voucher_id and vd.isActive=1 \n"
		// + " and ci.CardNo = ?  ";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, topup.idPassportNo);
			resultSet = preparedStatement.executeQuery();
			List<Topup> vouchers = new ArrayList<Topup>();
			while (resultSet.next()) {
				Topup objtopup = new Topup();
				objtopup.programme_id = resultSet.getInt("programmeId");
				objtopup.programme_name = resultSet.getString("programmeDesc");
				objtopup.voucher_id = resultSet.getString("voucher_id");
				objtopup.service_id = 0;// resultSet.getInt("service_id");
				objtopup.service_quantity = 0;// resultSet.getDouble("quantity");
				objtopup.voucher_type = resultSet.getString("voucher_type");
				objtopup.beneficiary_group_id = resultSet.getInt("bgId");
				objtopup.beneficiary_id = resultSet.getInt("beneficiaryId");
				objtopup.uom = "N/A"; // resultSet.getString("uom");
				objtopup.card_number = resultSet.getString("cardNumber");
				// /topup.service_value = resultSet.getDouble("service_value");
				objtopup.service_value = topup.topupValue;
				objtopup.remarks = topup.remarks;
				objtopup.location_id = topup.location_id;
				vouchers.add(objtopup);
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
	@SuppressWarnings("resource")
	public CompasResponse insertTopupVoucher(List<Topup> topups, List retailer,
			Topup objtopup) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		int cycle = getCycleId();
		logger.info("Cycle##" + cycle);

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			logger.info("Selected Retailer size for topup##" + retailer.size());

			for (int i = 0; i < retailer.size(); i++) {
				if (objtopup.requestType.trim().equalsIgnoreCase("BEN")) {
					preparedStatement=connection.prepareStatement(Queryconstants.checkRationNoCamp);
					preparedStatement.setString(1, objtopup.idPassportNo);
					preparedStatement.setInt(2, objtopup.location_id);
					resultSet=preparedStatement.executeQuery();
					if(resultSet.next()){
						DBOperations.DisposeSql(preparedStatement);
						preparedStatement = connection
								.prepareStatement(Queryconstants.update_topup);
						preparedStatement.setDouble(1, objtopup.topupValue);
						preparedStatement.setString(2, objtopup.remarks);
						preparedStatement.setString(3, objtopup.idPassportNo);
						preparedStatement.setInt(4, (int) retailer.get(i));
						preparedStatement.setInt(5, cycle);
						if (preparedStatement.executeUpdate() <= 0) {
							connection.rollback();
							return new CompasResponse(201, "Nothing to update");
						}
					}else{
						return new CompasResponse(201, "Ration No does not belong to selected camp");
					}
					
				} else {

					// logger.info("Selected Retailer id for topup##" +
					// retailer.get(i));
//					/DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.insert_topup);
					for (Topup topup : topups) {
						// if (CheckTopupCycle((int) retailer.get(i),
						// topup.beneficiary_group_id, topup.programme_id)) {
						// return new CompasResponse(201,
						// "Topup is already exists and not approved ");
						// }

						String voucher_id_number = generateVoucherIdNumber(
								topup, cycle, (int) retailer.get(i));
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
						preparedStatement.setTimestamp(
								11,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						if (objtopup.requestType.trim().equalsIgnoreCase("BEN")) {
							preparedStatement.setInt(12, cycle);
						} else {
							preparedStatement.setInt(12, cycle + 1);
						}

						preparedStatement.setString(13, voucher_id_number);
						preparedStatement.setString(14, objtopup.topupStatus);
						preparedStatement.setString(15, topup.voucher_type);
						preparedStatement.setInt(16, (int) retailer.get(i));
						preparedStatement.setInt(17, topup.location_id);
						preparedStatement.setString(18, objtopup.remarks);
						// preparedStatement.addBatch();
						if (preparedStatement.executeUpdate() <= 0) {
							connection.rollback();
							throw new Exception("Nothing To Update");
						}
					}
				}

			}
			if (objtopup.beneficiary_groups != null) {
				if (objtopup.beneficiary_groups.size() > 0) {
					for (int i = 0; i < objtopup.beneficiary_groups.size(); i++) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.updBnfGrpTopupCount);
						preparedStatement1.setInt(1, 1);
						preparedStatement1.setInt(2,
								(int) objtopup.beneficiary_groups.get(i));
						preparedStatement1.executeUpdate();
					}
				}
			}

			connection.commit();
			return new CompasResponse(200, "Topup Done Successfully");
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
				+ "  and bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and am.Id = di.AgentId  and topup_status='A' \n"
				+ "   and di.DeviceRegId = dr.Id and sm.Id = mt.item_id and prm.Id = mt.programme_id and dr.SerialNo = ? and voucher_id_number=? and mt.downloaded = ? and  mt.agent_Id=di.AgentId";

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
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, serialNo);
					preparedStatement.setString(2, topup.voucher_id);
					preparedStatement.setBoolean(3, false);
					resultSet = preparedStatement.executeQuery();
					voucherVO.cycle = topup1.cycle;
					voucherVO.intervention_id = topup1.programme_id;
					voucherVO.intervention = topup1.voucherName;
					voucherVO.card_number = topup1.card_number;
					voucherVO.voucher_id_number = topup1.voucher_id;
					voucherVO.valid_from = topup1.valid_from;
					voucherVO.valid_until = topup1.valid_until;

					if (topup1.voucher_type.trim().equalsIgnoreCase("CM")) {
						type = "0";
					}

					if (topup1.voucher_type.trim().equalsIgnoreCase("VA")) {
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
				+ "    where  bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and di.DeviceRegId = dr.Id and dr.SerialNo = ? and mt.downloaded = ? and  mt.agent_Id=di.AgentId and topup_status='A'";
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
		String sql = "  select distinct mt.voucher_id_number as voucher_id_number,mt.beneficiary_id,mt.card_number,mt.item_id,mt.item_quantity,mt.item_value,mt.programme_id,voucher_name,\n"
				+ "  mt.cycle,mt.voucher_id,mt.uom,am.agent_code,bg.bnfgrp_name,dr.SerialNo,sm.ServiceCode,sm.ServiceName,prm.ProgrammeDesc,vm.voucher_type,vm.start_date,vm.end_date\n"
				+ "   from mst_topup mt,AgentMaster am,BeneficiaryGroup bg,CardIssuance ci,M_MemberMaster mm,\n"
				+ "  ProductMaster pm,DeviceIssueDetails di,DeviceRegDetails dr,ServiceMaster sm,ProgrammeMaster prm,Voucher_mst vm,Voucher_dtl vd where pm.merchant_id = am.merchantId \n"
				+ "  and bg.product_id = pm.id and bg.id = mt.beneficiary_group_id and am.Id = di.AgentId and vd.voucher_id=mt.voucher_id \n"
				+ "   and di.DeviceRegId = dr.Id and sm.Id = mt.item_id and prm.Id = mt.programme_id and  mt.agent_Id=di.AgentId and mm.active=1 "
				+ "and vm.id = vd.voucher_id  and mt.voucher_id = vm.id and ci.CardNo=mt.card_number and mt.beneficiary_id = mm.ID  and mt.voucher_id_number = ? and mt.topup_status='A' and downloaded=0 ";

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
				topup.voucherName = resultSet.getString("voucher_name");
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
		String sql = "update mst_topup set downloaded = ? where voucher_id_number=? and topup_status='A' ";
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
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		String sql2 = "select AgentId from DeviceIssueDetails di,DeviceRegDetails dr where dr.Id=di.DeviceRegId and dr.SerialNo=?";
		String sql = "select distinct t.voucher_id,t.voucher_id_number from BeneficiaryGroup b,ProductMaster p,\n"
				+ "  AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d,mst_topup t,Voucher_dtl vd\n"
				+ "   where p.id=b.product_id and a.merchantId=p.merchant_id and di.AgentId=a.Id \n"
				+ "   and d.Id=di.DeviceRegId and t.voucher_id = vd.voucher_id and d.SerialNo = ? and t.downloaded = ? "
				+ " and topup_status='A' and t.agent_id=di.AgentId and a.Id=t.agent_id and t.agent_id=?";

		try {
			connection = dataSource.getConnection();
			preparedStatement1 = connection.prepareStatement(sql2);
			preparedStatement1.setString(1, serialNo);
			resultSet1 = preparedStatement1.executeQuery();
			List<Topup> vouchers = new ArrayList<Topup>();
			if (resultSet1.next()) {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, serialNo);
				preparedStatement.setBoolean(2, false);
				preparedStatement.setInt(3, resultSet1.getInt("agentid"));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Topup topup = new Topup();
					topup.voucher_id = resultSet.getString("voucher_id_number");
					vouchers.add(topup);
				}
				logger.info("##Voucher Update Size$$" + vouchers.size());
			}

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
	private String generateVoucherIdNumber(Topup topup, int cycle, int retailer) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(topup.beneficiary_id)
				.append(topup.beneficiary_group_id).append(topup.programme_id)
				.append(topup.voucher_id).append(cycle + 1);
		// logger.info("##" + stringBuffer.toString());
		return stringBuffer.toString();
	}

	/**
	 *
	 * @return
	 */
	private int getCycleId() {
		// logger.info("Get Cycle Id$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "select max(cycle) as id from mst_topup";
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
				cardnumber = "123456" + generateNumber();
				cust_name = resultSet.getString("name");
				return new CompasResponse(200, "Records Updated", cardnumber,
						cust_name);
			} else {
				return new CompasResponse(201, "Nothing To Update", "", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(401, "Exception Occured", "", "");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.dal.VoucherTopupDal#downloadVouchers(java.lang.String
	 * )
	 */
	public List<BeneficiaryGroup> GetBnfGrpsForTopup() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpForTopup);
			resultSet = preparedStatement.executeQuery();
			List<BeneficiaryGroup> objBnfGrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				BeneficiaryGroup bnfGrp = new BeneficiaryGroup();
				bnfGrp.bnfGrpId = resultSet.getInt("ID");
				bnfGrp.bnfGrpCode = resultSet.getString("bnfgrp_Code");
				bnfGrp.bnfGrpName = resultSet.getString("bnfgrp_name");
				bnfGrp.houseHoldValue = resultSet.getDouble("house_hold_value");
				bnfGrp.noOfBnfs = resultSet.getInt("no_of_bnfs");
				bnfGrp.maxCap = resultSet.getInt("max_cap");
				bnfGrp.minCap = resultSet.getInt("min_cap");
				bnfGrp.locationId = resultSet.getInt("location_id");
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getLastTopupDate);
				preparedStatement1.setInt(1, bnfGrp.bnfGrpId);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					bnfGrp.lastTopup = resultSet1.getString("last_topup");

				}

				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryCount);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				preparedStatement2.setInt(2, bnfGrp.locationId);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					bnfGrp.noOfBnfs = resultSet2.getInt("bnfCount");
					bnfGrp.noOfCaseloads = bnfGrp.noOfBnfs;

				}
				DBOperations.DisposeSql(preparedStatement2, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryId);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				preparedStatement2.setInt(2, bnfGrp.locationId);
				resultSet2 = preparedStatement2.executeQuery();
				DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
				while (resultSet2.next()) {

					bnfGrp.topupValue += CalculateTopupValue(
							resultSet2.getInt("id"), bnfGrp.houseHoldValue,
							bnfGrp.maxCap, bnfGrp.minCap, bnfGrp.locationId);
					bnfGrp.topupValueDisplay = df.format(bnfGrp.topupValue);
				}

				bnfGrp.isActive = false;

				objBnfGrps.add(bnfGrp);
				count++;
			}
			return objBnfGrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public double CalculateTopupValue(int bnfGrpId, double houseHoldValue,
			int maxCap, int mincap, int locationId) {
		// logger.info("Calculate topup value$$");
		// logger.info("Beneficiary Group HH value$$" + houseHoldValue);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double topupValue = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpHHDetails);
			preparedStatement.setInt(1, bnfGrpId);
			preparedStatement.setInt(2, locationId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				// logger.info("Voucher Value$$"
				// + resultSet.getDouble("voucher_value"));
				// logger.info("Family Size$$" +
				// resultSet.getInt("familysize"));
				//
				// topupValue += resultSet.getDouble("voucher_value")
				// + ((resultSet.getInt("familysize") - 1) * houseHoldValue);
				//
				/*
				 * if ((resultSet.getInt("familysize") - 1) > maxCap) {
				 * topupValue += resultSet.getDouble("voucher_value") + (maxCap
				 * * houseHoldValue);
				 * 
				 * } else { topupValue += resultSet.getDouble("voucher_value") +
				 * ((resultSet.getInt("familysize") - 1) * houseHoldValue); }
				 */

				topupValue += (resultSet.getDouble("cash") * resultSet
						.getInt("household_no"));

			}
			// logger.info("Topup Value$$" + topupValue);
			return topupValue;
		} catch (Exception ex) {
			ex.printStackTrace();
			return topupValue;
		} finally {

			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * @author ANita returns list of vouchers by beneficiarygroupId
	 * @param programmeId
	 * @return list of vouchers
	 */
	public List<Topup> getTopupVouchersByBnfGrp(Topup topup) {
		Statement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			List<Topup> vouchers = new ArrayList<Topup>();
			for (int i = 0; i < topup.bgTopupDetails.size(); i++) {
				// logger.info("Update $$" +
				// topup.bgTopupDetails.get(i).bnfGrpId);
				UpdateHouseHoldValue(topup.bgTopupDetails.get(i).bnfGrpId,
						topup.bgTopupDetails.get(i).houseHoldValue);
				// logger.info("BeneficiaryGrpId$$"
				// + topup.bgTopupDetails.get(i).bnfGrpId);

				// PreparedStatement preparedStatement = null;

				// String sql =
				// " select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,"
				// +
				// " vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,"
				// +
				// "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value,"
				// +
				// "sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type "
				// + " from BeneficiaryGroup b ,ProgrammeMaster p, "
				// +
				// " CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
				// +
				// " Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm "
				// +
				// " where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 "
				// +
				// "and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId "
				// + " and vm.id = vd.voucher_id and vd.isActive=1 and b.Id="
				// + topup.bgTopupDetails.get(i).bnfGrpId
				// + " "
				// + " and m.bnfGrpId=b.id and m.ID=ci.CustomerId "
				// + " order by b.id";

				// Changed for value voucher topup
				String sql = "";
				if (topup.programme_id > 0) {
					sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,  "
							+ "vm.id as voucher_id,voucher_value service_value, "
							+ "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
							+ "ci.CardNo as cardNumber,vm.voucher_type  "
							+ "from BeneficiaryGroup b ,ProgrammeMaster p, "
							+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
							+ "CardIssuance ci,Voucher_mst vm "
							+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 and m.active=1  "
							+ "and pv.ProgrammeId=p.Id and vm.id=pv.voucherId and ci.CustomerId=c.CustomerId "
							+ "and b.Id="
							+ topup.bgTopupDetails.get(i).bnfGrpId
							+ " and p.id="
							+ topup.programme_id
							+ " "
							+ "and m.bnfGrpId=b.id and m.ID=ci.CustomerId "
							+ "order by b.id ";

				} else if (topup.location_id > 0) {
					sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,  "
							+ "vm.id as voucher_id,voucher_value service_value,m.location_id, "
							+ "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
							+ "ci.CardNo as cardNumber,vm.voucher_type  "
							+ "from BeneficiaryGroup b ,ProgrammeMaster p, "
							+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
							+ "CardIssuance ci,Voucher_mst vm "
							+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 and m.active=1  "
							+ "and pv.ProgrammeId=p.Id and vm.id=pv.voucherId and ci.CustomerId=c.CustomerId "
							+ "and b.Id="
							+ topup.bgTopupDetails.get(i).bnfGrpId
							+ " and m.location_id="
							+ topup.location_id
							+ " "
							+ "and m.bnfGrpId=b.id and m.ID=ci.CustomerId "
							+ "order by b.id ";

				} else {
					sql = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,  "
							+ "vm.id as voucher_id,voucher_value service_value, "
							+ "b.id as bgId,c.CustomerId as beneficiaryId,house_hold_value, "
							+ "ci.CardNo as cardNumber,vm.voucher_type  "
							+ "from BeneficiaryGroup b ,ProgrammeMaster p, "
							+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,M_MemberMaster m, "
							+ "CardIssuance ci,Voucher_mst vm "
							+ "where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 and pv.IsActive=1 and m.active=1 "
							+ "and pv.ProgrammeId=p.Id and vm.id=pv.voucherId and ci.CustomerId=c.CustomerId "
							+ "and b.Id="
							+ topup.bgTopupDetails.get(i).bnfGrpId

							+ " and m.bnfGrpId=b.id and m.ID=ci.CustomerId "
							+ " order by b.id ";
				}
				resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					Topup objtopup = new Topup();
					objtopup.programme_id = resultSet.getInt("programmeId");
					objtopup.programme_name = resultSet
							.getString("programmeDesc");
					objtopup.voucher_id = resultSet.getString("voucher_id");
					objtopup.service_id = 0;// resultSet.getInt("service_id");
					objtopup.service_quantity = 0;// resultSet.getDouble("quantity");
					objtopup.voucher_type = resultSet.getString("voucher_type");

					objtopup.beneficiary_group_id = resultSet.getInt("bgId");
					objtopup.beneficiary_id = resultSet.getInt("beneficiaryId");
					objtopup.uom = "N/A";
					objtopup.card_number = resultSet.getString("cardNumber");
					objtopup.location_id = resultSet.getInt("location_id");
					if (objtopup.voucher_type.equalsIgnoreCase("VA")) {
						objtopup.service_value = CalcTopupValueIndividual(
								objtopup.beneficiary_id,
								resultSet.getDouble("house_hold_value"),
								objtopup.voucher_type, objtopup.programme_id,
								objtopup.location_id);
					} else if (objtopup.voucher_type.equalsIgnoreCase("CA")) {
						objtopup.service_value = CalcTopupValueIndividual(
								objtopup.beneficiary_id,
								resultSet.getDouble("house_hold_value"),
								objtopup.voucher_type, objtopup.programme_id,
								objtopup.location_id);
					} else if (objtopup.voucher_type.equalsIgnoreCase("CM")) {
						objtopup.service_value = resultSet
								.getDouble("service_value");
					}

					vouchers.add(objtopup);
				}

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

	public double CalcTopupValueIndividual(int bnfId, double houseHoldValue,
			String voucherType, int programmeId, int locationId) {
		// logger.info("Calculate topup value$$");
		// logger.info("Beneficiary Group HH value$$" + houseHoldValue);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double topupValue = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfHHDetails);
			preparedStatement.setInt(1, bnfId);
			preparedStatement.setString(2, voucherType);
			preparedStatement.setInt(3, programmeId);
			preparedStatement.setInt(4, locationId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				// logger.info("Voucher Value$$"
				// + resultSet.getDouble("voucher_value"));
				// logger.info("Family Size$$" +
				// resultSet.getInt("familysize"));
				/*
				 * if ((resultSet.getInt("familysize") - 1) > resultSet
				 * .getInt("max_cap")) { topupValue +=
				 * resultSet.getDouble("voucher_value") +
				 * (resultSet.getInt("max_cap") * houseHoldValue);
				 * 
				 * } else { topupValue += resultSet.getDouble("voucher_value") +
				 * ((resultSet.getInt("familysize") - 1) * houseHoldValue); }
				 */
				topupValue += (resultSet.getDouble("voucher_value") * resultSet
						.getInt("familysize"));

			}
			// logger.info("Topup Value$$" + topupValue);
			return topupValue;
		} catch (Exception ex) {
			ex.printStackTrace();
			return topupValue;
		} finally {

			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateHouseHoldValue(int bnfGrpId,
			double houseHoldValue) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			logger.info("## Beneficiary verifications update");

			preparedStatement = connection
					.prepareStatement(Queryconstants.updateHouseHoldValue);
			preparedStatement.setDouble(1, houseHoldValue);
			preparedStatement.setInt(2, bnfGrpId);
			if (preparedStatement.executeUpdate() <= 0) {
				connection.rollback();
				// logger.info("Failed to Update beneficiary Id " + bnfGrpId);
				return new CompasResponse(202, "Nothing To Update");
			}

			connection.commit();
			return new CompasResponse(200, "Records Updated");
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

	// Commmnted by ANita 25 january 2017
	/**
	 * instead doing topup approval for each line item changed it to do by cycle
	 * id
	 */
	// public List<Topup> GetTopupDetailsToApprove(Topup bnfGrp) {
	// DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
	// Connection connection = null;
	// CallableStatement callableStatement = null;
	// ResultSet resultSet = null;
	//
	// int count = 1;
	// try {
	// connection = dataSource.getConnection();
	// List<Topup> topupList = new ArrayList<Topup>();
	// for(int i=0;i<bnfGrp.retailerSelected.size();i++){
	// callableStatement = connection
	// .prepareCall(Queryconstants.getTopupDetailsToApprove);
	// callableStatement.setInt(1, bnfGrp.bnfGrpId);
	// callableStatement.setString(2, bnfGrp.fromDate);
	// callableStatement.setString(3, bnfGrp.toDate);
	// callableStatement.setInt(4, (int) bnfGrp.retailerSelected.get(i));
	// resultSet = callableStatement.executeQuery();
	//
	// while (resultSet.next()) {
	// Topup topup = new Topup();
	// topup.id = resultSet.getInt("topupid");
	// topup.bnfGrpname = resultSet.getString("bnfgrp_name");
	// topup.programmeCode = resultSet.getString("ProgrammeCode");
	// topup.programme_name = resultSet.getString("ProgrammeDesc");
	// topup.voucherName = resultSet.getString("voucher_name");
	// topup.service_name = resultSet.getString("servicename");
	// topup.itemvalue = df.format(resultSet.getDouble("item_value"));
	// topup.card_number = resultSet.getString("cardNumber");
	// topup.uom = resultSet.getString("uom");
	// topup.count = count;
	// topup.isActive = false;
	// topupList.add(topup);
	// count++;
	// }
	// }
	// logger.info("Toup details size##" + topupList.size());
	// return topupList;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// } finally {
	//
	// DBOperations.DisposeSql(connection, callableStatement, resultSet);
	// }
	// }
	//
	public List<BeneficiaryGroup> GetTopupDetailsToApprove(
			BeneficiaryGroup bnfGrp) {
		// DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		CallableStatement callableStatement = null;
		PreparedStatement preparedstatement = null;
		PreparedStatement preparedstatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet1 = null;
		int cycle = 0;
		int count = 1;

		try {
			connection = dataSource.getConnection();
			List<BeneficiaryGroup> agentList = new ArrayList<BeneficiaryGroup>();

			callableStatement = connection
					.prepareCall(Queryconstants.getTopupDetailsToApprove);
			callableStatement.setInt(1, bnfGrp.locationId);
			callableStatement.setString(2, bnfGrp.fromDate);
			callableStatement.setString(3, bnfGrp.toDate);

			resultSet = callableStatement.executeQuery();

			while (resultSet.next()) {
				BeneficiaryGroup agent = new BeneficiaryGroup();
				agent.cycle = resultSet.getInt("cycle");
				agent.bnfGrpId = resultSet.getInt("beneficiary_group_id");
				preparedstatement = connection
						.prepareStatement(Queryconstants.getTopupApprovalDtl);
				preparedstatement.setInt(1, agent.bnfGrpId);
				preparedstatement.setInt(2, bnfGrp.locationId);
				resultSet2 = preparedstatement.executeQuery();
				if (resultSet2.next()) {
					agent.bnfGrpName = resultSet2.getString("bnfgrp_name");
					agent.noOfBnfs = resultSet2.getInt("bnfcount");
				}
				DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
				agent.topupValue = resultSet.getDouble("topup_value");
				agent.topupValueDisplay = df.format(agent.topupValue);

				preparedstatement1 = connection
						.prepareStatement(Queryconstants.getLastTopupDate);
				preparedstatement1.setInt(1, bnfGrp.locationId);
				resultSet1 = preparedstatement1.executeQuery();
				if (resultSet1.next()) {
					agent.lastTopup = resultSet1.getString("last_topup");
					agent.count = count;
				}
				count++;
				agentList.add(agent);

			}
			logger.info("Toup details size##" + agentList.size());
			return agentList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {

			DBOperations.DisposeSql(connection, callableStatement, resultSet);
		}
	}

	public CompasResponse UpdateTopupStatus(Topup topup, List<Agent> agentCycle) {
		logger.info("Update Approved Topups$$");
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		// String topupIds = "";
		// topupIds = concatenateListwithComma(topup.topupSelection);

		// logger.info("Topups$$" + topupIds);

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			// List retailer = topup.retailerSelected;
			// for (int j = 0; j < agentCycle.size(); j++) {
			for (int i = 0; i < topup.topupDetails.size(); i++) {
				String sql = "update mst_topup set topup_status='"
						+ topup.topupStatus
						+ "' ,approved_by="
						+ topup.createdBy
						+ ",approved_on='"
						+ new java.sql.Timestamp(new java.util.Date().getTime())
						+ "' where  downloaded=0 and topup_status<>'A' and beneficiary_group_id="
						+ topup.topupDetails.get(i).bnfGrpId + " and cycle="
						+ topup.topupDetails.get(i).cycle + " and location_id="
						+ topup.locationId + "";
				statement.addBatch(sql);
				// int res = statement.executeUpdate(sql);
				// if (res <= 0) {
				// throw new Exception(
				// "Fail to update the topup"
				// );
				// //if (Utility.Sendmail(topup.userName, "A", topup.userEmail))
				// {
				//
				// //} else {
				//
				// //return new CompasResponse(201, "Fail to update the topup");
				// //}
				// } //else {
				// return new CompasResponse(201, "Nothing To Update");

				// }
			}
			int res[] = statement.executeBatch();
			if (res.length <= 0) {
				return new CompasResponse(202, "No Topups Found");
				// if (Utility.Sendmail(topup.userName, "A", topup.userEmail)) {

				// } else {

				// return new CompasResponse(201, "Fail to update the topup");
				// }
			}
			connection.commit();
			return new CompasResponse(200, "Topup Approved Successfully");

		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.printStackTrace();

			return new CompasResponse(404, "Exception Occured");

		} finally {
			DBOperations.DisposeSql(connection, statement, resultSet);
		}
	}

	public List<BeneficiaryGroup> GetTopupedBnfGrps() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLocForTopup);
			resultSet = preparedStatement.executeQuery();
			List<BeneficiaryGroup> objBnfGrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				
				
				
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getLastTopupDate);
				preparedStatement1.setInt(1,resultSet.getInt("locid"));
				resultSet1 = preparedStatement1.executeQuery();
				while (resultSet1.next()) {
					BeneficiaryGroup bnfGrp = new BeneficiaryGroup();
					bnfGrp.noOfLoc = resultSet.getInt("locid");
					bnfGrp.lastTopup = resultSet1.getString("last_topup");
					bnfGrp.locationName = resultSet1.getString("location_name");
					bnfGrp.noOfBnfs = resultSet1.getInt("no_of_bnfs");
					bnfGrp.topupValue=resultSet1.getDouble("topup_value");
					bnfGrp.topupStatus=resultSet1.getString("topupstatus");
					bnfGrp.topupValueDisplay = df.format(bnfGrp.topupValue);
					objBnfGrps.add(bnfGrp);
				}

				/*preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryCount);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					

				}
				DBOperations.DisposeSql(preparedStatement2, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryId);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				resultSet2 = preparedStatement2.executeQuery();
				while (resultSet2.next()) {

					bnfGrp.topupValue += CalculateTopupedValue(
							resultSet2.getInt("id"), bnfGrp.houseHoldValue,
							bnfGrp.maxCap, bnfGrp.minCap);
				}
				bnfGrp.topupValueDisplay = df.format(bnfGrp.topupValue);
				bnfGrp.isActive = false;*/

				
				count++;
			}
			return objBnfGrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public double CalculateTopupedValue(int bnfGrpId, double houseHoldValue,
			int maxCap, int mincap) {
		// logger.info("Calculate topup value$$");
		// logger.info("Beneficiary Group HH value$$" + houseHoldValue);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double topupValue = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpHHDetailsTopuped);
			preparedStatement.setInt(1, bnfGrpId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				// logger.info("Voucher Value$$"
				// + resultSet.getDouble("voucher_value"));
				// logger.info("Family Size$$" +
				// resultSet.getInt("familysize"));
				//
				// topupValue += resultSet.getDouble("voucher_value")
				// + ((resultSet.getInt("familysize") - 1) * houseHoldValue);
				//
				if ((resultSet.getInt("familysize") - 1) > maxCap) {
					topupValue += resultSet.getDouble("voucher_value")
							+ (maxCap * houseHoldValue);

				} else {
					topupValue += resultSet.getDouble("voucher_value")
							+ ((resultSet.getInt("familysize") - 1) * houseHoldValue);
				}

			}
			// logger.info("Topup Value$$" + topupValue);
			return topupValue;
		} catch (Exception ex) {
			ex.printStackTrace();
			return topupValue;
		} finally {

			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckTopupCycle(int agentId, int bnfGrpId, int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkTopupCycle);
			preparedStatement.setInt(1, agentId);
			preparedStatement.setInt(2, bnfGrpId);
			preparedStatement.setInt(3, locationId);
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

	public boolean CheckTopupCycleDownloaded(int agentId, int bnfGrpId,
			int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkTopupCycleDownloaded);
			preparedStatement.setInt(1, agentId);
			preparedStatement.setInt(2, bnfGrpId);
			preparedStatement.setInt(3, locationId);
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

	public List<BeneficiaryGroup> GetBnfGrpsByPrgId(int programmeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByPrgId);
			preparedStatement.setInt(1, programmeId);
			resultSet = preparedStatement.executeQuery();
			List<BeneficiaryGroup> objBnfGrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				BeneficiaryGroup bnfGrp = new BeneficiaryGroup();
				bnfGrp.bnfGrpId = resultSet.getInt("ID");
				bnfGrp.bnfGrpCode = resultSet.getString("bnfgrp_Code");
				bnfGrp.bnfGrpName = resultSet.getString("bnfgrp_name");
				bnfGrp.houseHoldValue = resultSet.getDouble("house_hold_value");
				bnfGrp.noOfBnfs = resultSet.getInt("no_of_bnfs");
				bnfGrp.maxCap = resultSet.getInt("max_cap");
				bnfGrp.minCap = resultSet.getInt("min_cap");
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getLastTopupDate);
				preparedStatement1.setInt(1, bnfGrp.bnfGrpId);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					bnfGrp.lastTopup = resultSet1.getString("last_topup");
				}
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryCount);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					bnfGrp.noOfBnfs = resultSet2.getInt("bnfCount");
				}
				DBOperations.DisposeSql(preparedStatement2, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryId);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				resultSet2 = preparedStatement2.executeQuery();
				DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
				while (resultSet2.next()) {

					bnfGrp.topupValue += CalculateTopupValue(
							resultSet2.getInt("id"), bnfGrp.houseHoldValue,
							bnfGrp.maxCap, bnfGrp.minCap, bnfGrp.locationId);
				}
				bnfGrp.topupValueDisplay = df.format(bnfGrp.topupValue);
				bnfGrp.isActive = false;
				objBnfGrps.add(bnfGrp);
				count++;
			}
			return objBnfGrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<BeneficiaryGroup> GetBnfGrpsByLocationId(int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByLoc);
			preparedStatement.setInt(1, locationId);
			resultSet = preparedStatement.executeQuery();
			List<BeneficiaryGroup> objBnfGrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				BeneficiaryGroup bnfGrp = new BeneficiaryGroup();
				bnfGrp.bnfGrpId = resultSet.getInt("bnfgrpid");
				bnfGrp.bnfGrpCode = resultSet.getString("bnfgrp_Code");
				bnfGrp.bnfGrpName = resultSet.getString("bnfgrp_name");
				bnfGrp.houseHoldValue = resultSet.getDouble("house_hold_value");
				bnfGrp.noOfBnfs = resultSet.getInt("no_of_bnfs");
				bnfGrp.maxCap = resultSet.getInt("max_cap");
				bnfGrp.locationId = resultSet.getInt("location_id");
				bnfGrp.minCap = resultSet.getInt("min_cap");
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getLastTopupDate);
				preparedStatement1.setInt(1, bnfGrp.locationId);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					bnfGrp.lastTopup = resultSet1.getString("last_topup");
				}
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryCount);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				preparedStatement2.setInt(2, bnfGrp.locationId);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					bnfGrp.noOfBnfs = resultSet2.getInt("bnfCount");
				}
				DBOperations.DisposeSql(preparedStatement2, resultSet2);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getBeneficiaryId);
				preparedStatement2.setInt(1, bnfGrp.bnfGrpId);
				preparedStatement2.setInt(2, bnfGrp.locationId);
				resultSet2 = preparedStatement2.executeQuery();
				DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
				while (resultSet2.next()) {

					bnfGrp.topupValue += CalculateTopupValue(
							resultSet2.getInt("id"), bnfGrp.houseHoldValue,
							bnfGrp.maxCap, bnfGrp.minCap, bnfGrp.locationId);
				}
				bnfGrp.topupValueDisplay = df.format(bnfGrp.topupValue);
				bnfGrp.isActive = false;
				objBnfGrps.add(bnfGrp);
				count++;
			}
			return objBnfGrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetAllAgentsByLoc(int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentsByLoc);
			preparedStatement.setInt(1, locationId);

			resultSet = preparedStatement.executeQuery();
			List<Agent> objAgents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent agent = new Agent();
				agent.agentId = resultSet.getInt("ID");
				// /agent.agentCode = resultSet.getString("Agent_Code");
				agent.agentDesc = resultSet.getString("AgentDesc");

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

	public CompasResponse UpdateLimit(TopupLimit limit) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int agentId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			if (limit.limitId == 0) {
				if (CheckLimitExists(limit.userId)) {
					return new CompasResponse(201,
							"Limit is already set for specified user");
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertLimitDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, limit.userId);
				preparedStatement.setDouble(2, limit.limit);

				preparedStatement.setInt(3, limit.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));

			} else {

				preparedStatement = connection
						.prepareStatement(Queryconstants.updLimitDetails);
				// /preparedStatement.setInt(1, limit.userId);
				preparedStatement.setDouble(1, limit.limit);

				preparedStatement.setInt(2, limit.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				preparedStatement.setInt(4, limit.userId);

			}
			if (preparedStatement.executeUpdate() > 0) {
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
				return new CompasResponse(201, "Failed to Insert");
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

	public List<TopupLimit> GetTopupLimits() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTopupLimits);

			resultSet = preparedStatement.executeQuery();
			List<TopupLimit> objAgents = new ArrayList<TopupLimit>();
			while (resultSet.next()) {
				TopupLimit limit = new TopupLimit();
				limit.limitId = resultSet.getInt("ID");
				limit.userId = resultSet.getInt("user_id");
				limit.limit = resultSet.getDouble("topup_limit");
				limit.userFullName = resultSet.getString("userfullname");

				limit.count = count;

				objAgents.add(limit);
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

	public boolean CheckLimitExists(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkLimitExists);

			preparedStatement.setInt(1, userId);
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

	public List<User> GetWebUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWebUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> objAgents = new ArrayList<User>();
			while (resultSet.next()) {
				User limit = new User();
				limit.userId = resultSet.getInt("ID");
				limit.userFullName = resultSet.getString("userfullname");
				// limit.limit=resultSet.getDouble("limit");

				limit.count = count;

				objAgents.add(limit);
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
}
