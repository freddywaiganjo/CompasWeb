/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.BasketBal;
import com.compulynx.compas.dal.impl.BasketDalImpl;
import com.compulynx.compas.models.Basket;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
@Component
public class BasketBalImpl implements BasketBal {

	@Autowired
	BasketDalImpl basketDal;

	public boolean checkBasketByName(String basketName) {
		// TODO Auto-generated method stub
		return basketDal.checkBasketByName(basketName);
	}


	public CompasResponse UpdateBasket(Basket basket) {
		// TODO Auto-generated method stub
		return basketDal.UpdateBasket(basket);
	}


	public List<Basket> GetAllBaskets() {
		// TODO Auto-generated method stub
		return basketDal.GetAllBaskets();
	}


	public Basket GetBasketById(int basketId) {
		// TODO Auto-generated method stub
		return basketDal.GetBasketById(basketId);
	}

}
