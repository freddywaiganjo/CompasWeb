/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.dal.impl.UserDalImpl;
import com.compulynx.compas.bal.UserBal;
import com.compulynx.compas.bal.UserGroupBal;
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
 * @author Anita
 *
 */
@Component
public class UserBalImpl implements UserBal{
	@Autowired
	UserDalImpl userDal;

	public CompasResponse UpdateUser(User user) {
		
		return userDal.UpdateUser(user);
	}
	
//	public List<User> GetAllUsers() {
//		return userDal.GetAllUsers();
//	}
	
	public User GetUserById(int userId) {
		return userDal.GetUserById(userId);
	}


	public List<User> GetQuestions() {
		
		return userDal.GetQuestions();
	}
	public List<User> GetAllUsers() {
		return userDal.GetAllUsers();
	}

	
	public List<User> GetClasses() {
		// TODO Auto-generated method stub
		return userDal.GetClasses();
	}

	public List<Agent> GetBranchAgents(int branchId) {
		// TODO Auto-generated method stub
		return userDal.GetBranchAgents(branchId);
	}

	@Override
	public List<TmsUserDownload> GetTmsUserDownload(String macAddress) {
		// TODO Auto-generated method stub
		return userDal.GetTmsUserDownload(macAddress);
	}

	@Override
	public TmsRetailersDownload GetTmsRetailerDownload(String macAddress) {
		// TODO Auto-generated method stub
		return userDal.GetTmsRetailerDownload(macAddress);
	}

	@Override
	public String UplodTmsTrans(List<TmsTransactionMst> trans) {
		// TODO Auto-generated method stub
		return userDal.UplodTmsTrans(trans);
	}

	@Override
	public List<UserGroup> GetGroupsByUserType(int userTypeId) {
		// TODO Auto-generated method stub
		return userDal.GetGroupsByUserType(userTypeId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.UserBal#GetAndroidDownloadData(java.lang.String)
	 */
	@Override
	public AndroidDownloadVo GetAndroidDownloadData(String macAddress) {
		// TODO Auto-generated method stub
		return userDal.GetAndroidDownloadData(macAddress);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.UserBal#GetAndroidBeneficiary()
	 */
	@Override
	public AndroidDownloadVo GetAndroidBeneficiary(String macAddress) {
		// TODO Auto-generated method stub
		return userDal.GetAndroidBeneficiary(macAddress);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.UserBal#GetAndroidTopupDetails(java.lang.String)
	 */
	@Override
	public AndroidDownloadVo GetAndroidTopupDetails(String serialNo) {
		// TODO Auto-generated method stub
		return userDal.GetAndroidTopupDetails(serialNo);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.UserBal#updateVoucherDownloadStatus(java.lang.String)
	 */
	@Override
	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		// TODO Auto-generated method stub
		return userDal.updateVoucherDownloadStatus(deviceId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.UserBal#GetLocationAgents(int)
	 */
	@Override
	public List<Agent> GetLocationAgents(int locationId) {
		// TODO Auto-generated method stub
		return userDal.GetLocationAgents(locationId);
	}
}
