/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TopupLimit;
import com.compulynx.compas.models.User;

/**
 * @author shree
 *Aug 15, 2016
 */
public interface VoucherTopupDal {

	 String buildDownloadVouchers(String posId);
	 CompasResponse updateVoucherDownloadStatus(String deviceId);
	 List<BeneficiaryGroup> GetBnfGrpsForTopup();
	 //double CalculateTopupValue(int bnfGrpId, double houseHoldValue);
	 List<BeneficiaryGroup> GetBnfGrpsByPrgId(int programmeId);
	 List<BeneficiaryGroup> GetTopupDetailsToApprove(BeneficiaryGroup bnfGrp);
	 CompasResponse UpdateTopupStatus(Topup topup,List<Agent> agentCycle);
	 List<BeneficiaryGroup> GetTopupedBnfGrps();
	 boolean CheckTopupCycle(int agentId,int bnfGrpId,int locationId);
	 List<BeneficiaryGroup> GetBnfGrpsByLocationId(int locationId);
	 List<Agent> GetAllAgentsByLoc(int locationId);
	 CompasResponse UpdateLimit(TopupLimit limit) ;
	 List<TopupLimit> GetTopupLimits();
	 List<User> GetWebUsers() ;
	 
}
