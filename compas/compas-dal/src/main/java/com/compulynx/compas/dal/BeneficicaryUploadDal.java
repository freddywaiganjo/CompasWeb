/**
 * 
 */
package com.compulynx.compas.dal;

import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *Aug 11, 2016
 */
public interface BeneficicaryUploadDal {
	CompasResponse UploadAccount(String filePath, String uploadedBy);
	CompasResponse UploadHHDetails(String filePath, String uploadedBy,int locationId);
}
