/**
 * 
 */
package com.compulynx.compas.bal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.BeneficiaryUploadBal;
import com.compulynx.compas.dal.impl.BeneficiaryUploadDalImpl;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 * Aug 11, 2016
 */
@Component
public class BeneficiaryUploadBalImpl implements BeneficiaryUploadBal{
	@Autowired
	BeneficiaryUploadDalImpl bnfUploadDal;
	  @Override
	    public CompasResponse UploadAccount(String filePath, String uploadedBy) {
	        return bnfUploadDal.UploadAccount(filePath,uploadedBy);
	    }
	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.BeneficiaryUploadBal#UploadHHDetails(java.lang.String, java.lang.String)
	 */
	@Override
	public CompasResponse UploadHHDetails(String filePath, String uploadedBy,int locationId) {
		// TODO Auto-generated method stub
		return bnfUploadDal.UploadHHDetails(filePath, uploadedBy,locationId);
	}

}
