package org.camunda.bpm.bvis.rest.send.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import org.camunda.bpm.bvis.entities.Claim;
import org.camunda.bpm.bvis.entities.InvolvedParty;
import org.camunda.bpm.bvis.rest.send.dto.ClaimDetailsDTO;
import org.camunda.bpm.bvis.rest.send.dto.ClaimInsurance;
import org.camunda.bpm.bvis.rest.send.dto.Involved_party;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ManagedBean
//@ConversationScoped
@Named
public class SendClaim {

	private static final String BASE_URI = "http://camunda-capitol.uni-muenster.de/partner-interface/";
	
	public String sendClaim(org.camunda.bpm.bvis.entities.Claim claim, String processInstanceID) {
		ClaimDetailsDTO claimDetails = new ClaimDetailsDTO();
		claimDetails.setProcessinstance_id_bvis(processInstanceID);
		claimDetails.setRequest_date(Calendar.getInstance());
		Collection<Involved_party> involvedParties = this.parseInvolvedParties(claim);
		claimDetails.setClaim(this.parseClaim(claim, involvedParties));
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
		ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI);
		SendClaimClient senderClient = target.proxy(SendClaimClient.class);
		String result = senderClient.sendClaim(claimDetails);
		return result;
	}
	
	private org.camunda.bpm.bvis.rest.send.dto.Claim parseClaim(
			org.camunda.bpm.bvis.entities.Claim entityClaim,
			Collection<Involved_party> involvedParties) {
		org.camunda.bpm.bvis.rest.send.dto.Claim claim = new org.camunda.bpm.bvis.rest.send.dto.Claim();
		claim.setClaim_description(entityClaim.getClaimDescription());
		claim.setClaim_id(Integer.parseInt(entityClaim.getClaimID().toString()));
		claim.setDamage_address(entityClaim.getDamageAddress());
		Calendar cal = Calendar.getInstance();
		cal.setTime(entityClaim.getDamageDate());
		claim.setDamage_date(cal);
		claim.setInsurance_id(Integer.parseInt(entityClaim.getInsurance().getInsuranceID() + ""));
		claim.setInvolvedParties(involvedParties);
		claim.setOrder_id(Integer.parseInt(entityClaim.getRentalOrder().getOrderID() + ""));
		claim.setParties_involved(involvedParties.size() > 0 ? true : false);
		claim.setVehicle_identification_number(entityClaim.getVehicleIdentificationNumber());
		claim.setWorkshop_price(entityClaim.getWorkshopPrice().intValue());
		return claim;
	}
	
	private Collection<Involved_party> parseInvolvedParties(Claim claim) {
		Collection<InvolvedParty> involvedPartiesEntity = claim.getInvolvedParties();
		ArrayList<Involved_party> involvedParties = new ArrayList<Involved_party>();
		for (InvolvedParty entityParty : involvedPartiesEntity) {
			Involved_party party = new Involved_party();
			party.setCity(entityParty.getCity());
			party.setCompany(entityParty.getCompany());
			party.setCountry(entityParty.getCountry());
			Calendar cal = Calendar.getInstance();
			cal.setTime(entityParty.getDate_of_birth());
			party.setDate_of_birth(cal);
			party.setEmail(entityParty.getEmail());
			party.setFirstname(entityParty.getFirstname());
			party.setHas_insurance(true);
			party.setHouse_number(entityParty.getHouse_number());
			party.setInsurance(this.parseClaimInsuranceForInvolvedParty(entityParty));
			party.setPhone_number(entityParty.getPhone_number());
			party.setPostcode(entityParty.getPostcode());
			party.setStreet(entityParty.getStreet());
			party.setSurname(entityParty.getSurname());
			involvedParties.add(party);
		}
		return involvedParties;
	}
	
	private ClaimInsurance parseClaimInsuranceForInvolvedParty(InvolvedParty party) {
		ClaimInsurance insurance = new ClaimInsurance();
		insurance.setCity(party.getInsurance_company_city());
		insurance.setCompany(party.getInsurance_company_name());
		insurance.setCountry(party.getInsurance_company_country());
		insurance.setHouse_number(party.getInsurance_company_house_number());
		insurance.setPostcode(party.getInsurance_company_postcode());
		insurance.setStreet(party.getInsurance_company_street());
		return insurance;
	}
}
