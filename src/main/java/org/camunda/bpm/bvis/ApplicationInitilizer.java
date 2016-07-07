package org.camunda.bpm.bvis;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.ejb.CarServiceBean;
import org.camunda.bpm.bvis.ejb.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarType;
import org.camunda.bpm.bvis.entities.PickUpLocation;



@Startup
@Singleton
public class ApplicationInitilizer {
	
	@PersistenceContext
	protected EntityManager em;
	
	@EJB
	private PickUpLocationServiceBean pickUpLocationService;

	@EJB
	private CarServiceBean carService;
	
	// /**
	// * Creates some example data for testing purpose.
	// *
	// * It creates multiple objects of type PickUpLocation and Car.
	// */
	@PostConstruct
	public void initialise() {
		PickUpLocation bcn = createPickUpLocation("Barcelona Airport", "+34 902 40 47 04", "El Prat de Llobregat", "",
				"08820", "Barcelona", "Spain");
		pickUpLocationService.create(bcn);
		PickUpLocation md = createPickUpLocation("Madrid Airport", "+34 913 21 10 00", "Avenida de la Hispanidad, s/n", "",
				"28042", "Madrid", "Spain");
		pickUpLocationService.create(md);
		PickUpLocation val = createPickUpLocation("Valencia Airport", "+34 902 40 47 04",
				"Carretera del Aeropuerto, s/n", "", "46940", "Valencia", "Spain");
		pickUpLocationService.create(val);
		
		Car aud = createCar("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123456", CarType.car, true);
		carService.create(aud);
		Car toy = createCar("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123456", CarType.car, true);
		carService.create(toy);
		
	}

	private PickUpLocation createPickUpLocation(String storeName, String phoneNumber, String street, String houseNumber, String postcode, String city, String country) {
		PickUpLocation location = new PickUpLocation();
		location.setStoreName(storeName);
		location.setPhoneNumber(phoneNumber);
		location.setStreet(street);
		location.setHouseNumber(houseNumber);
		location.setPostcode(postcode);
		location.setCity(city);
		location.setCountry(country);
		return location;
	}
	
	private Car createCar(String brand, int constructionYear, String fuelType, String model, int ps, String registrationNumber, String vehicle_identification_number, CarType type, boolean returned) {
		Car car = new Car();
		car.setBrand(brand);
		car.setConstructionYear(constructionYear);
		car.setFuelType(fuelType);
		car.setModel(model);
		car.setPs(ps);
		car.setRegistrationNumber(registrationNumber);
		car.setType(type);
		car.setRented(returned);
		car.setVehicleIdentificationNumber(vehicle_identification_number);
		return car;
	}
}
