/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Basket;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public interface BasketDal {
	boolean checkBasketByName(String basketName);

	CompasResponse UpdateBasket(Basket basket);

	List<Basket> GetAllBaskets();

	Basket GetBasketById(int basketId);
}
