/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.VoucherTopupBal;
import com.compulynx.compas.dal.impl.VoucherTopupDalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TopupLimit;
import com.compulynx.compas.models.User;

/**
 * @author Anita Aug 15, 2016
 */
@Component
public class VoucherTopupBalImpl implements VoucherTopupBal {
	private static final Logger logger = Logger
			.getLogger(VoucherTopupBalImpl.class.getName());
	@Autowired
	VoucherTopupDalImpl topupDal;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.bal.VoucherTopupBal#getTopupVouchers(com.compulynx
	 * .compas.models.Topup)
	 */
	@Override
	public CompasResponse getTopupVouchers(Topup topup) {
		// TODO Auto-generated method stub
		// Check if topup is done for same cycle and not approved
		for (int i = 0; i < topup.retailerSelected.size(); i++) {
			if (topup.beneficiary_groups != null) {
				if (topup.beneficiary_groups.size() > 0) {
					for (int j = 0; j < topup.beneficiary_groups.size(); j++) {
						if (topupDal.CheckTopupCycle(
								(int) topup.retailerSelected.get(i),
								(int) topup.beneficiary_groups.get(j),topup.location_id)) {
							return new CompasResponse(
									201,
									"Topup is not approved for the retailer/retailers ,Kindly approve and then topup");
						}else 	if (topupDal.CheckTopupCycleDownloaded(
								(int) topup.retailerSelected.get(i),
								(int) topup.beneficiary_groups.get(j),topup.location_id)) {
							return new CompasResponse(
									201,
									"Topup is approved but not downloaded for the retailer/retailers ,Kindly download and then topup");
						}
					}
				}
			}
		}
		List<Topup> topups = new ArrayList<Topup>();
		if (topup.requestType.trim().equalsIgnoreCase("PROG")) {
			logger.info("## Get Vouchers by PROG");

			String bgId = "";
			// bgId =
			// topupDal.concatenateListwithComma(topup.beneficiary_groups);
			topups = topupDal.getTopupVouchersByBnfGrp(topup);
			if (topups.size() > 0) {

			}
			return topupDal.insertTopupVoucher(topups, topup.retailerSelected,
					topup);
		}
		if (topup.requestType.trim().equalsIgnoreCase("LOC")) {
			logger.info("## Get Vouchers by Camp");

			String bgId = "";
			// bgId =
			// topupDal.concatenateListwithComma(topup.beneficiary_groups);
			topups = topupDal.getTopupVouchersByBnfGrp(topup);
			if (topups.size() > 0) {

			}
			return topupDal.insertTopupVoucher(topups, topup.retailerSelected,
					topup);
		}
		if (topup.requestType.trim().equalsIgnoreCase("BEN")) {
			logger.info("## Get Vouchers by BEN" + topup.card_number);
			topups = topupDal.getTopupVouchersByCardNumber(topup);
			if (topups.size() > 0)
				return topupDal.insertTopupVoucher(topups,
						topup.retailerSelected, topup);
		}

		if (topup.requestType.trim().equalsIgnoreCase("BG")) {
			logger.info("## Get Vouchers by BG");
			String bgId = "";
			// bgId =
			// topupDal.concatenateListwithComma(topup.beneficiary_groups);
			topups = topupDal.getTopupVouchersByBnfGrp(topup);
			if (topups.size() > 0)
				return topupDal.insertTopupVoucher(topups,
						topup.retailerSelected, topup);
		}

		return new CompasResponse(201, "No Vouchers to be topped up!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.bal.VoucherTopupBal#downloadVouchers(java.lang.String
	 * )
	 */
	@Override
	public String buildDownloadVouchers(String posId) {
		// TODO Auto-generated method stub
		return topupDal.buildDownloadVouchers(posId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.bal.VoucherTopupBal#updateVoucherDownloadStatus(
	 * java.lang.String)
	 */
	@Override
	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		// TODO Auto-generated method stub
		return topupDal.updateVoucherDownloadStatus(deviceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetBnfGrpsByPrg()
	 */
	@Override
	public List<BeneficiaryGroup> GetBnfGrpsForTopup() {
		// TODO Auto-generated method stub
		return topupDal.GetBnfGrpsForTopup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.compulynx.compas.bal.VoucherTopupBal#CalculateTopupValue(int,
	 * double)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetBnfGrpsByPrgId(int)
	 */
	@Override
	public List<BeneficiaryGroup> GetBnfGrpsByPrgId(int programmeId) {
		// TODO Auto-generated method stub
		return topupDal.GetBnfGrpsByPrgId(programmeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.bal.VoucherTopupBal#GetTopupDetailsToApprove(com
	 * .compulynx.compas.models.BeneficiaryGroup)
	 */
	@Override
	public List<BeneficiaryGroup> GetTopupDetailsToApprove(BeneficiaryGroup bnfGrp) {
		// TODO Auto-generated method stub
		return topupDal.GetTopupDetailsToApprove(bnfGrp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.bal.VoucherTopupBal#UpdateTopupStatus(com.compulynx
	 * .compas.models.Topup)
	 */

	public CompasResponse UpdateTopupStatus(Topup topup) {
		// TODO Auto-generated method stub
		// int cycle=0;
		List<Agent> agentCycle =null;// GetTopupDetailsToApprove(topup);

		//if (agentCycle.size()>0) {
			//return topupDal.UpdateTopupStatus(topup, agentCycle);
		//} else {
		//	return new CompasResponse(201, "No Vouchers to be approved!");
		//}

		 return topupDal.UpdateTopupStatus(topup, agentCycle);
			//} else {);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetTopupedBnfGrps()
	 */
	@Override
	public List<BeneficiaryGroup> GetTopupedBnfGrps() {
		// TODO Auto-generated method stub
		return topupDal.GetTopupedBnfGrps();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.compulynx.compas.bal.VoucherTopupBal#CheckTopupCycle(int, int,
	 * int)
	 */
	@Override
	public boolean CheckTopupCycle(int agentId, int bnfGrpId,int locationId) {
		// TODO Auto-generated method stub
		return topupDal.CheckTopupCycle(agentId, bnfGrpId,locationId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetBnfGrpsByLocationId(int)
	 */
	@Override
	public List<BeneficiaryGroup> GetBnfGrpsByLocationId(int locationId) {
		// TODO Auto-generated method stub
		return topupDal.GetBnfGrpsByLocationId(locationId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetAllAgentsByLoc(int)
	 */
	@Override
	public List<Agent> GetAllAgentsByLoc(int locationId) {
		// TODO Auto-generated method stub
		return topupDal.GetAllAgentsByLoc(locationId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherTopupBal#UpdateLimit(com.compulynx.compas.models.TopupLimit)
	 */
	@Override
	public CompasResponse UpdateLimit(TopupLimit limit) {
		// TODO Auto-generated method stub
		return topupDal.UpdateLimit(limit);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetTopupLimits()
	 */
	@Override
	public List<TopupLimit> GetTopupLimits() {
		// TODO Auto-generated method stub
		return topupDal.GetTopupLimits();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherTopupBal#GetWebUsers()
	 */
	@Override
	public List<User> GetWebUsers() {
		// TODO Auto-generated method stub
		return topupDal.GetWebUsers();
	}

}
