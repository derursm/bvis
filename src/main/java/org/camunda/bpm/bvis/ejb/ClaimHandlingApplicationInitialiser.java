package org.camunda.bpm.bvis.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.entities.ClaimInsurance;

@Startup
@Singleton
public class ClaimHandlingApplicationInitialiser {

	@PersistenceContext
	protected EntityManager em;
	
	@EJB
	protected ClaimServiceBean claimService;
	

	/**
	 * creates insurances, which can be mapped to the user input in for the involved parties
	 */
	@PostConstruct
	public void initialise() {
		ClaimInsurance ins = new ClaimInsurance();
		ins.setCity("Munich");
		ins.setCompany("Allianz");
		ins.setCountry("Germany");
		ins.setHouse_number("10");
		ins.setPostcode("40699");
		ins.setStreet("Marienplatz");
		claimService.createClaimInsurance(ins);
		ClaimInsurance ins2 = new ClaimInsurance();
		ins2.setCity("Cologne");
		ins2.setCompany("Capitol");
		ins2.setCountry("Germany");
		ins2.setHouse_number("20");
		ins2.setPostcode("50678");
		ins2.setStreet("Koelner Strasse");
		claimService.createClaimInsurance(ins2);
	}
}
