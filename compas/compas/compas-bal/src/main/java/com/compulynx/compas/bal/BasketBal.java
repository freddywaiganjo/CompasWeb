/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Basket;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public interface BasketBal {
	boolean checkBasketByName(String basketName);

	CompasResponse UpdateBasket(Basket basket);

	List<Basket> GetAllBaskets();

	Basket GetBasketById(int basketId);
}
