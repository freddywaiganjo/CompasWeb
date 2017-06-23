package com.compulynx.compas.dal;

import java.util.List;












import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.MemberCard;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.Personalize;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.ProgrammeValue;
import com.compulynx.compas.models.Relations;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.Wallet;

public interface CustomerDal {
	List<Customer> GetMembers(int locationId);
	
	CompasResponse UpdateMember(Customer member);
	
	List<Customer> GetCardIssuanceList();
	
	CompasResponse UpdateCardStatus(Customer member);
	
	List<Customer> GetCardBlockList();
	
	List<Customer> GetCardActivationList();
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
	 List<Relations> GetRelations();
	 CompasResponse UpdateBnfStatus(Customer member);
	 List<Customer> GetVerifiedMembers();
	 List<Customer> GetRegMembers();
	 List<Customer> GetCustomers();
	 List<Customer> GetCustCardIssuanceList();
	 CompasResponse UpdatePosCustomer(Customer member);
	 CompasResponse GetCustId(Customer customer);
	 CompasResponse UpdateAndMember(List<Customer> member);
}
