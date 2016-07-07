package org.camunda.bpm.bvis.ejb;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarType;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.RentalOrder;

@Startup
@Singleton
public class ClaimHandlingApplicationInitialiser {

	@PersistenceContext
	protected EntityManager em;
	
	@EJB
	protected ClaimServiceBean claimService;
	
	@EJB 
	protected OrderServiceBean orderService;
	
	@EJB 
	protected CarServiceBean carService;
	
	@EJB
	protected CustomerServiceBean customerService;
	

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
		
		// rental order with cars and insurance for testing purposes
		RentalOrder order = new RentalOrder();
		order.setFleetRental(false);
		Customer customer = new Customer();
		customer.setCity("Muenster");
		customer.setCompanyName("ERCIS");
		customer.setCountry("Germany");
		customer.setDateOfBirth(new Date());
		customer.setFirstname("Joerg");
		customer.setHouseNumber("3");
		customer.setPhoneNumber("0123 456789");
		customer.setPostcode("48159");
		customer.setStreet("Leonardo Campus");
		customer.setSurname("Becker");
		customerService.create(customer);
		ArrayList<Car> cars = new ArrayList<Car>();
		Car car = new Car();
		car.setVehicleIdentificationNumber("MS1");
		car.setRegistrationNumber("abc");
		car.setConstructionYear(2000);
		car.setModel("5er");
		car.setBrand("BMW");
		car.setFuelType("Diesel");
		car.setType(CarType.kombi);
		car.setPs(210);
		carService.create(car);
		cars.add(car);
		order.setCars(cars);
		order.setCustomer(customer);
		orderService.create(order);
		System.out.println("RENTAL ORDER ID: " + order.getId());
	}
}
