package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SafComTxns;
import com.compulynx.compas.models.Transaction;

public interface TransactionBal {
	CompasResponse UpdateTransaction(List<Transaction> txn);
	CompasResponse UpdateSafComTxn(SafComTxns txn);
	List<SafComTxns> GetAllTxns(String cardnumber);
	List<Device> GetDeviceList();
	List<Agent> GetAgentList();
}
