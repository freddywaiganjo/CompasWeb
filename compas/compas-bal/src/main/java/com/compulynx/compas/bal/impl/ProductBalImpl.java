package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.ProductBal;
import com.compulynx.compas.dal.impl.AgentDalImpl;
import com.compulynx.compas.dal.impl.ProductDalImpl;
import com.compulynx.compas.models.AgeGroups;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Product;

@Component
public class ProductBalImpl implements ProductBal {
	@Autowired
	ProductDalImpl productDal;

	@Override
	public CompasResponse UpdateProduct(Product product) {
		// TODO Auto-generated method stub
		return productDal.UpdateProduct(product);
	}

	@Override
	public List<Product> GetProducts() {
		// TODO Auto-generated method stub
		return productDal.GetProducts();
	}

	@Override
	public List<BeneficiaryGroup> GetBnfGroups() {
		// TODO Auto-generated method stub
		return productDal.GetBnfGroups();
	}

	@Override
	public CompasResponse UpdateBnfGroup(BeneficiaryGroup bnfGrp) {
		// TODO Auto-generated method stub
		return productDal.UpdateBnfGroup(bnfGrp);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ProductBal#GetAgeGroups()
	 */
	@Override
	public List<AgeGroups> GetAgeGroups() {
		// TODO Auto-generated method stub
		return productDal.GetAgeGroups();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ProductBal#GetAgeGroupsByBnfGrp(int)
	 */
	@Override
	public List<Location> GetAgeGroupsByBnfGrp(int bnfgrpId) {
		// TODO Auto-generated method stub
		return productDal.GetAgeGroupsByBnfGrp(bnfgrpId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ProductBal#UpdateLocAgeGrp(com.compulynx.compas.models.BeneficiaryGroup)
	 */
	@Override
	public CompasResponse UpdateLocAgeGrp(BeneficiaryGroup bnfgrp) {
		// TODO Auto-generated method stub
		return productDal.UpdateLocAgeGrp(bnfgrp);
	}

}
