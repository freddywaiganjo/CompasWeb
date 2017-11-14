/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.SafComLogin;
import com.compulynx.compas.models.SafComResponse;
import com.compulynx.compas.models.TmsUserDownload;

/**
 * @author Anita
 *
 */
public interface LoginDal {

	LoginUser GetUserIdManual(String userName, String password);

	LoginUser GetUserIdBio(int bioId);

	LoginSession GetUserAssgnRightsList(int userId);

	LoginUser GetUserIdBio(String userName);

	LoginUser GetUserIdDevice(String userName, String password, String deviceId);
	
	SafComResponse SafComLogin(String userName, String password,
			String deviceId,String userType,String licenseNumber);
	SafComResponse ChangePin(SafComLogin login);
	SafComResponse UnblockCard(SafComLogin login);
	SafComResponse BlockCard(SafComLogin login);
	

	
}
