/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.ServiceDtlVO;
import com.compulynx.compas.models.Subservice;
import com.compulynx.compas.models.Uom;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public interface ServiceBal {
	CompasResponse UpdateService(Service service);

	List<Service> GetAllServices();

	List<Params> GetAllParams();

	//List<Service> GetSubServiceById(int serviceId);

	CompasResponse UpdateParam(Params param);
	
	List<Subservice> getAllSubservices(int page) ;
	List<ServiceDtlVO> getAllServiceDtls(int page);
	List<Params> getParams(int market, int page);
	List<Currency> GetCurrencies();
	 List<Uom> GetUoms();
	 List<Voucher> GetVoucherServices(int programmeId,int locationId);
	 CompasResponse UpdatePriceConfig(Programme prg);
}
