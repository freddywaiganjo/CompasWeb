/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SafComTxns;
import com.compulynx.compas.models.Transaction;

/**
 * @author anita
 *
 */
public interface TransactionDal {
	CompasResponse UpdateTransaction(List<Transaction> txn);
	CompasResponse UpdateSafComTxn(SafComTxns txn);
	List<SafComTxns> GetAllTxns(String cardnumber);
	List<Device> GetDeviceList();
	List<Agent> GetAgentList();
}
