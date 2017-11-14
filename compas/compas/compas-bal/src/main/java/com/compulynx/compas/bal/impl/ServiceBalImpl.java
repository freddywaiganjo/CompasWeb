package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.models.Params;
import com.compulynx.compas.bal.ServiceBal;
import com.compulynx.compas.dal.impl.ServiceDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.ServiceDtlVO;
import com.compulynx.compas.models.Subservice;
import com.compulynx.compas.models.Uom;
import com.compulynx.compas.models.Voucher;

@Component
public class ServiceBalImpl implements ServiceBal {

	@Autowired
	ServiceDalImpl serviceDal;

	public CompasResponse UpdateService(Service service) {

		return serviceDal.UpdateService(service);
	}

	public List<Service> GetAllServices() {

		return serviceDal.GetAllServices();
	}

	public List<Params> GetAllParams() {
		// TODO Auto-generated method stub
		return serviceDal.GetAllParams();
	}

	
	

	@Override
	public CompasResponse UpdateParam(Params param) {
		// TODO Auto-generated method stub
		return serviceDal.UpdateParam(param);
	}

	@Override
	public List<Subservice> getAllSubservices(int page) {
		// TODO Auto-generated method stub
		return serviceDal.getAllSubservices(page);
	}

	@Override
	public List<ServiceDtlVO> getAllServiceDtls(int page) {
		// TODO Auto-generated method stub
		return serviceDal.getAllServiceDtls(page);
	}

	@Override
	public List<Params> getParams(int market, int page) {
		// TODO Auto-generated method stub
		return serviceDal.getParams(market, page);
	}

	@Override
	public List<Currency> GetCurrencies() {
		// TODO Auto-generated method stub
		return serviceDal.GetCurrencies();
	}

	@Override
	public List<Uom> GetUoms() {
		// TODO Auto-generated method stub
		return serviceDal.GetUoms();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ServiceBal#GetVoucherServices(int)
	 */
	@Override
	public List<Voucher> GetVoucherServices(int programmeId,int locationId) {
		// TODO Auto-generated method stub
		return serviceDal.GetVoucherServices(programmeId,locationId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ServiceBal#UpdatePriceConfig(com.compulynx.compas.models.Programme)
	 */
	@Override
	public CompasResponse UpdatePriceConfig(Programme prg) {
		// TODO Auto-generated method stub
		return serviceDal.UpdatePriceConfig(prg);
	}



}
