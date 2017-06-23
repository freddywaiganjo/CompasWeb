package com.compulynx.compas.bal.impl;

import com.compulynx.compas.bal.CustomerBal;
import com.compulynx.compas.dal.impl.CustomerDalImpl;
import com.compulynx.compas.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CustomerBalImpl implements CustomerBal{

	private static final Logger logger = Logger.getLogger(CustomerBalImpl.class.getName());

	@Autowired
	CustomerDalImpl memberDal;
	public List<Customer> GetMembers(int locationId) {
		return memberDal.GetMembers(locationId);
	}

	public CompasResponse UpdateMember(Customer member) {		
		return memberDal.UpdateMember(member);
	}
	
	public List<Customer> GetCardIssuanceList() {
		return memberDal.GetCardIssuanceList();
	}

	public CompasResponse UpdateCardStatus(Customer member) {
		return memberDal.UpdateCardStatus(member);
	}

	public List<Customer> GetCardActivationList() {
		return memberDal.GetCardActivationList();
	}
	
	public List<Customer> GetCardBlockList() {
		return memberDal.GetCardBlockList();
	}


	public Personalize updateMemberCardLinkId(MemberCard memCard) {
		// TODO Auto-generated method stub
		return memberDal.updateMemberCardLinkId(memCard);
	}


	public List<MemberCard> getListOfCards() {
		// TODO Auto-generated method stub
		return memberDal.getListOfCards();
	}


	public List<Customer> getListOfCardsToPrint() {
		// TODO Auto-generated method stub
		return memberDal.getListOfCardsToPrint();
	}

	@Override
	public List<Wallet> GetWallets() {
		// TODO Auto-generated method stub
		return memberDal.GetWallets();
	}

	@Override
	public CompasResponse UpdateWallet(Organization org) {
		// TODO Auto-generated method stub
		return memberDal.UpdateWallet(org);
	}

	@Override
	public CompasResponse UpdateCardIssuance(MemberCard card) {
		// TODO Auto-generated method stub
		return memberDal.UpdateCardIssuance(card);
	}

	@Override
	public MemberCard GetLoadWalletCardDetails(String cardNumber) {
		// TODO Auto-generated method stub
		return memberDal.GetLoadWalletCardDetails(cardNumber);
	}

	@Override
	public CompasResponse UpdateCardLoad(MemberCard card) {
		// TODO Auto-generated method stub
		return memberDal.UpdateCardLoad(card);
	}

	@Override
	public CompasResponse UpdateCustomerEnr(Customer customer) {
		// TODO Auto-generated method stub
		return memberDal.UpdateCustomerEnr(customer);
	}

	@Override
	public CompasResponse VerifyCustomerEnr(String bioImage) {
		// TODO Auto-generated method stub
		return memberDal.VerifyCustomerEnr(bioImage);
	}

	@Override
	public CompasResponse isImageDBDistinct(byte[] capturedImage, String capturePosition, String afisIp, String afisPort) {
		// TODO Auto-generated method stub
		return memberDal.isImageDBDistinct(capturedImage, capturePosition, afisIp, afisPort);
	}

	@Override
	public List<ProgrammeValue> GetCardBalance(String cardNumber) {
		// TODO Auto-generated method stub
		return memberDal.GetCardBalance(cardNumber);
	}
	@Override
	public Customer GetBioMembers(int bioId) {
		// TODO Auto-generated method stub
		return memberDal.GetBioMembers(bioId);
	}

	@Override
	public List<Customer> GetBlockedCards() {
		// TODO Auto-generated method stub
		return memberDal.GetBlockedCards();
	}

	@Override
	public CompasResponse updateCardReissue(MemberCard memCard) {
		// TODO Auto-generated method stub
		return memberDal.updateCardReissue(memCard);
	}

	@Override
	public List<Programme> GetProgrammesByBnfId(int bnfId) {
		// TODO Auto-generated method stub
		return memberDal.GetProgrammesByBnfId(bnfId);
	}

	@Override
	public List<Relations> GetRelations() {
		// TODO Auto-generated method stub
		return memberDal.GetRelations();
	}

	@Override
	public CompasResponse UpdateBnfStatus(Customer member) {
		// TODO Auto-generated method stub
		return memberDal.UpdateBnfStatus(member);
	}

	@Override
	public List<Customer> GetVerifiedMembers() {
		// TODO Auto-generated method stub
		return memberDal.GetVerifiedMembers();
	}


	@Override
	public CompasResponse getTopupVouchers(Topup topup) {
		List<Topup> topups = new ArrayList<Topup>();
		if(topup.requestType.trim().equalsIgnoreCase("PROG")){
			logger.info("## Get Vouchers by PROG");
			String programmeId = "";
			programmeId = memberDal.concatenateListwithComma(topup.programmes);
			topups = memberDal.getTopupVouchersByProgram(programmeId);
			if(topups.size() > 0)
				return memberDal.insertTopupVoucher(topups);
		}

		if(topup.requestType.trim().equalsIgnoreCase("BEN")){
			logger.info("## Get Vouchers by BEN"+topup.card_number);
			topups = memberDal.getTopupVouchersByCardNumber(topup.card_number);
			if(topups.size() > 0)
				return memberDal.insertTopupVoucher(topups);
		}

		if(topup.requestType.trim().equalsIgnoreCase("BG")){
			logger.info("## Get Vouchers by BG");
//			String bgId = "";
//			bgId = memberDal.concatenateListwithComma(topup.beneficiary_groups);
//			topups = memberDal.getTopupVouchersByBeneficiaryGroups(bgId);
//			if(topups.size() > 0)
//				return memberDal.insertTopupVoucher(topups);
		}

		return new CompasResponse(201,"No Vouchers to be topped up!");
	}

	@Override
	public String downloadVouchers(String posId) {
		// TODO Auto-generated method stub
		return memberDal.buildDownloadVouchers(posId);
	}

	@Override
	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		return memberDal.updateVoucherDownloadStatus(deviceId);
	}

	@Override
	public List<Customer> GetRegMembers() {
		// TODO Auto-generated method stub
		return memberDal.GetRegMembers();
	}

	@Override
	public List<Customer> GetCustomers() {
		// TODO Auto-generated method stub
		return memberDal.GetCustomers();
	}

	@Override
	public List<Customer> GetCustCardIssuanceList() {
		// TODO Auto-generated method stub
		return memberDal.GetCustCardIssuanceList();
	}

	@Override
	public CompasResponse UpdatePosCustomer(Customer member) {
		// TODO Auto-generated method stub
		return memberDal.UpdatePosCustomer(member);
	}

	@Override
	public CompasResponse GetCustId(Customer customer) {
		// TODO Auto-generated method stub
		return memberDal.GetCustId(customer);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.CustomerBal#UpdateAndMember(java.util.List)
	 */
	@Override
	public CompasResponse UpdateAndMember(List<Customer> member) {
		// TODO Auto-generated method stub
		return memberDal.UpdateAndMember(member);
	}


}
