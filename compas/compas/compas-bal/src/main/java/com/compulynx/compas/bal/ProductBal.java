/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.AgeGroups;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Product;

/**
 * @author Anita
 *
 */
public interface ProductBal {
	CompasResponse UpdateProduct(Product product);

	List<Product> GetProducts();

	List<BeneficiaryGroup> GetBnfGroups();

	CompasResponse UpdateBnfGroup(BeneficiaryGroup bnfGrp);
	List<AgeGroups> GetAgeGroups();
	 List<Location> GetAgeGroupsByBnfGrp(int bnfgrpId);
	 CompasResponse UpdateLocAgeGrp(BeneficiaryGroup bnfgrp);
}
