package com.compulynx.compas.bal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.TransactionBal;
import com.compulynx.compas.dal.impl.TransactionDalImpl;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SafComTxns;
import com.compulynx.compas.models.Transaction;

@Component
public class TransactionBalImpl implements TransactionBal{

	@Autowired
	TransactionDalImpl transactionDal;
	public CompasResponse UpdateTransaction(List<Transaction> txn) {
		// TODO Auto-generated method stub
		return transactionDal.UpdateTransaction(txn);
	}
	@Override
	public CompasResponse UpdateSafComTxn(SafComTxns txn) {
		// TODO Auto-generated method stub
		return transactionDal.UpdateSafComTxn(txn);
	}
	@Override
	public List<SafComTxns> GetAllTxns(String cardnumber) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllTxns(cardnumber);
	}
	@Override
	public List<Device> GetDeviceList() {
		// TODO Auto-generated method stub
		return transactionDal.GetDeviceList();
	}
	@Override
	public List<Agent> GetAgentList() {
		// TODO Auto-generated method stub
		return transactionDal.GetAgentList();
	}
	

	


}
