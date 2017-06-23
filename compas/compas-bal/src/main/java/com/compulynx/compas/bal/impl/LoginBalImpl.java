/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.LoginBal;
import com.compulynx.compas.dal.impl.LoginDalImpl;
import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.SafComResponse;
import com.compulynx.compas.models.TmsUserDownload;

/**
 * @author Anita
 *
 */
@Component
public class LoginBalImpl implements LoginBal{
	@Autowired
	LoginDalImpl loginDal;
	
	public LoginUser ValidateManualLogin(String userName, String password) {
		return loginDal.GetUserIdManual(userName, password);
	}

	public LoginUser ValidateBioLogin(int bioId) {
		return loginDal.GetUserIdBio(bioId);
	}

	public LoginSession GetLoginSession(int userId) {
		return loginDal.GetUserAssgnRightsList(userId);
	}


	public LoginUser ValidateBioLogin(String userName) {
		return loginDal.GetUserIdBio(userName);
	}


	public LoginUser GetUserIdDevice(String userName, String password,
			String deviceId) {
		// TODO Auto-generated method stub
		return loginDal.GetUserIdDevice(userName, password, deviceId);
	}

	@Override
	public SafComResponse SafComLogin(String userName, String password, String deviceId, String userType,String licenseNumber) {
		// TODO Auto-generated method stub
		return loginDal.SafComLogin(userName, password, deviceId, userType,licenseNumber);
	}

	@Override
	public SafComResponse ChangePin(com.compulynx.compas.models.SafComLogin login) {
		// TODO Auto-generated method stub
		return loginDal.ChangePin(login);
	}

	@Override
	public SafComResponse UnblockCard(com.compulynx.compas.models.SafComLogin login) {
		// TODO Auto-generated method stub
		return loginDal.UnblockCard(login);
	}

	@Override
	public SafComResponse BlockCard(com.compulynx.compas.models.SafComLogin login) {
		// TODO Auto-generated method stub
		return loginDal.BlockCard(login);
	}

	

	
}
