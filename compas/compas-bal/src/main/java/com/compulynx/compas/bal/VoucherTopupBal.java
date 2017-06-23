/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TopupLimit;
import com.compulynx.compas.models.User;

/**
 * @author Anita
 * Aug 15, 2016
 */
public interface VoucherTopupBal {


	 CompasResponse getTopupVouchers(Topup topup);
	String buildDownloadVouchers(String posId);
	CompasResponse updateVoucherDownloadStatus(String deviceId);
	List<BeneficiaryGroup> GetBnfGrpsForTopup();
	//double CalculateTopupValue(int bnfGrpId, double houseHoldValue);
	 List<BeneficiaryGroup> GetBnfGrpsByPrgId(int programmeId);
	 List<BeneficiaryGroup> GetTopupDetailsToApprove(BeneficiaryGroup bnfGrp);
	 CompasResponse UpdateTopupStatus(Topup topup);
	 List<BeneficiaryGroup> GetTopupedBnfGrps();
	 boolean CheckTopupCycle(int agentId,int bnfGrpId,int locationId);
	 List<BeneficiaryGroup> GetBnfGrpsByLocationId(int locationId);
	 List<Agent> GetAllAgentsByLoc(int locationId);
	 CompasResponse UpdateLimit(TopupLimit limit) ;
	 List<TopupLimit> GetTopupLimits();
	 List<User> GetWebUsers() ;
}
