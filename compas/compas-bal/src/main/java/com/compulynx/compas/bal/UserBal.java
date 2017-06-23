package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.TmsRetailersDownload;
import com.compulynx.compas.models.TmsTransactionMst;
import com.compulynx.compas.models.TmsUserDownload;
import com.compulynx.compas.models.TransactionVo;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidTopup;

/**
 * @author anita
 *
 */
public interface UserBal {
	CompasResponse UpdateUser(User user);

	// List<User> GetAllUsers();
	User GetUserById(int userId);

	List<User> GetQuestions();

	List<User> GetAllUsers();
	List<User> GetClasses();
	List<Agent> GetBranchAgents(int branchId);
	List<TmsUserDownload> GetTmsUserDownload(String macAddress);
	TmsRetailersDownload GetTmsRetailerDownload(String macAddress);
	String UplodTmsTrans(List<TmsTransactionMst> trans);
	List<UserGroup> GetGroupsByUserType(int userTypeId);
	AndroidDownloadVo GetAndroidDownloadData(String macAddress);
	AndroidDownloadVo GetAndroidBeneficiary(String macAddress);
	AndroidDownloadVo GetAndroidTopupDetails(String serialNo);
	CompasResponse updateVoucherDownloadStatus(String deviceId);
	List<Agent> GetLocationAgents(int locationId);
}
