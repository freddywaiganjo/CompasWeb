/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.MerchantBal;
import com.compulynx.compas.dal.impl.MerchantDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;

/**
 * @author Anita
 *
 */
@Component
public class MerchantBalImpl implements MerchantBal{

	@Autowired
	MerchantDalImpl merchantDal;
	
	public CompasResponse UpdateMerchant(Merchant merchant) {
		// TODO Auto-generated method stub
		return merchantDal.UpdateMerchant(merchant);
	}

	public List<Merchant> GetMerchants() {
		// TODO Auto-generated method stub
		return merchantDal.GetMerchants();
	}

}
