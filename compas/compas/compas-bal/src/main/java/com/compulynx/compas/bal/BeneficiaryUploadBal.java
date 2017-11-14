/**
 * 
 */
package com.compulynx.compas.bal;

import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 * Aug 11, 2016
 */
public interface BeneficiaryUploadBal {
	CompasResponse UploadAccount(String filePath, String uploadedBy);
	CompasResponse UploadHHDetails(String filePath, String uploadedBy,int locationId);
}
