package org.camunda.bpm.bvis;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarType;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.PickUpLocation;
import org.camunda.bpm.bvis.entities.RentalOrder;

@Startup
@Singleton
public class ApplicationInitilizer {
	
	@PersistenceContext
	protected EntityManager em;
	
	// Creates some example data for testing purpose
	@PostConstruct
	public void initialise() {
		// PickUpLocations
		em.persist(new PickUpLocation("Barcelona Airport", "+34 902 40 47 04", "El Prat de Llobregat", "",
				"08820", "Barcelona", "Spain"));
		em.persist(new PickUpLocation("Madrid Airport", "+34 913 21 10 00", "Avenida de la Hispanidad, s/n", "",
				"28042", "Madrid", "Spain"));
		em.persist(new PickUpLocation("Valencia Airport", "+34 902 40 47 04",
				"Carretera del Aeropuerto, s/n", "", "46940", "Valencia", "Spain"));
		
		// Cars
		em.persist(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123456", CarType.car, true));
		em.persist(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123456", CarType.car, true));
		Car car = new Car("BMW", 2000, "Diesel", "5er BMW", 210, "abc", "MS1", CarType.kombi, true);
		em.persist(car);
		
		
		// ClaimInsurances
		em.persist(new ClaimInsurance("Allianz", "Marienplatz", "10", "40699", "Munich", "Germany")); 
		em.persist(new ClaimInsurance("Capitol", "Koelner Strasse", "20", "50678", "Cologne", "Germany")); 
		
		//Customer
		Customer cust = new Customer("Becker", "0123 456789", "Leonardo Campus", "3", "48159", "Muenster", "Germany");
		cust.setCompanyName("ERCIS");
		cust.setCompany(true);
		em.persist(cust); // used in RentalOrder
		
		// RentalOrder
		RentalOrder order = new RentalOrder(cust, false);
		Collection<Car> cars = new ArrayList<Car>();
		cars.add(car);
		order.setCars(cars);
		em.persist(order);
		System.out.println("RENTAL ORDER ID: " + order.getId());
	}
}
