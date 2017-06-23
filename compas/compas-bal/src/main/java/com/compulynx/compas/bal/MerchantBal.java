/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;

/**
 * @author Anita
 *
 */
public interface MerchantBal {
	CompasResponse UpdateMerchant(Merchant merchant);

	List<Merchant> GetMerchants();
}
