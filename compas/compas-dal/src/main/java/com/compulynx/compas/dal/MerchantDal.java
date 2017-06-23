/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;

/**
 * @author Anita
 *
 */
public interface MerchantDal {
	CompasResponse UpdateMerchant(Merchant merchant);

	List<Merchant> GetMerchants();

}
