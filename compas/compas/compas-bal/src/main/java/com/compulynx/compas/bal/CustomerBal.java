package com.compulynx.compas.bal;

import com.compulynx.compas.models.*;

import java.util.List;

public interface CustomerBal {
	List<Customer> GetMembers(int locationId);

	

	CompasResponse UpdateMember(Customer member);

	List<Customer> GetCardIssuanceList();

	CompasResponse UpdateCardStatus(Customer member);

	List<Customer> GetCardActivationList();
	
	List<Customer> GetCardBlockList();
	Personalize updateMemberCardLinkId(MemberCard memCard);
	List<MemberCard> getListOfCards();
	List<Customer> getListOfCardsToPrint();
	List<Wallet> GetWallets();
	 CompasResponse UpdateWallet(Organization org);
	 CompasResponse UpdateCardIssuance(MemberCard card);
	MemberCard GetLoadWalletCardDetails(String cardNumber);
	 CompasResponse UpdateCardLoad(MemberCard card);
	 CompasResponse UpdateCustomerEnr(Customer customer);
	 CompasResponse VerifyCustomerEnr(String bioImage);
	 CompasResponse isImageDBDistinct(byte[] capturedImage, String capturePosition, String afisIp, String afisPort) ;
	 List<ProgrammeValue> GetCardBalance(String cardNumber);
	 Customer GetBioMembers(int bioId) ;
	 List<Customer> GetBlockedCards();
	 CompasResponse updateCardReissue(MemberCard memCard);
	 List<Programme> GetProgrammesByBnfId(int bnfId);
	 CompasResponse UpdatePosCustomer(Customer member);
	 List<Relations> GetRelations();

	 CompasResponse UpdateBnfStatus(Customer member);
	 List<Customer> GetVerifiedMembers();


	 CompasResponse getTopupVouchers(Topup topup);
	 String downloadVouchers(String posId);

	public CompasResponse updateVoucherDownloadStatus(String deviceId);
	List<Customer> GetRegMembers();
	 List<Customer> GetCustomers();
	 List<Customer> GetCustCardIssuanceList();
	 CompasResponse GetCustId(Customer customer);
	 CompasResponse UpdateAndMember(List<Customer> member);

}
