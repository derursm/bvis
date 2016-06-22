package org.camunda.bpm.bvis.EJB;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import java.util.Calendar;
//import java.util.Random;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.camunda.bpm.bvis.Entities.Car;
import org.camunda.bpm.bvis.Entities.CarType;
import org.camunda.bpm.bvis.Entities.PickUpLocation;

@Startup
@Singleton
public class ApplicationInitialiser {

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
		
		Car aud = createCar("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123456", CarType.TYPE1, true);
		carService.create(aud);
		Car toy = createCar("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123456", CarType.TYPE1, true);
		carService.create(toy);

		// DeliveryOrder order = createOrderFor(johnDoe);
		// em.persist(order);
		// addDishesToOrder(new Dish[] { hawaii, salami }, order);
		//
		// order = createOrderFor(poeDameron);
		// em.persist(order);
		// addDishesToOrder(new Dish[] { salami, funghi }, order);
		//
		// order = createOrderFor(bratPitt);
		// em.persist(order);
		// addDishesToOrder(new Dish[] { bratwurst, currywurst }, order);
	}

	// /**
	// * Takes a list from object Dish adds each Dish to an Orderline, sets a
	// random quantity,
	// * persists the Orderline and then adds it to the Deliveryorder.
	// *
	// * @param dishes a list from object Dish which should be added
	// * @param order the DeliveryOrder the dishes get added to via an Orderline
	// */
	// private void addDishesToOrder(Dish[] dishes, DeliveryOrder order) {
	// for (Dish dish : dishes) {
	// OrderLine o = new OrderLine();
	// o.setQuantity(new Random().nextInt(3) + 1);
	// o.setDish(dish);
	// o.setDeliveryOrder(order);
	// em.persist(o);
	// order.getOrderLines().add(o);
	// }
	// }
	//
	// /**
	// * Creates and returns a Dish with the given parameters
	// *
	// * @param name a String representing the name for the new dish
	// * @param description a String representing the description of the new
	// dish
	// * @param price a int representing the price of the new dish
	// * @return the Dish with parameters of above set
	// */
	// private Dish createDish(String name, String description, int price) {
	// Dish dish = new Dish();
	// dish.setName(name);
	// dish.setDescription(description);
	// dish.setPrice(price);
	// return dish;
	// }
	//
	// /**
	// * Creates and returns a Customer with the given parameters
	// *
	// * @param name a String representing the name for the new customer
	// * @param phoneNumber a String representing a sequence of numbers (no
	// check)
	// * @param address a String representing the address for the new customer
	// * @return the Customer with parameters of above set
	// */
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

	// /**
	// * Creates an empty order for the given customer with the current date and
	// time.
	// * DeliveryOrder and Customer get a reference to each other.
	// *
	// * @param customer Customer object for referencing
	// * @return DeliveryOrder object with a reference to the given customer
	// */
	// private DeliveryOrder createOrderFor(Customer customer) {
	// DeliveryOrder order = new DeliveryOrder();
	// order.setReceived(Calendar.getInstance().getTime());
	// order.setCustomer(customer);
	// customer.getDeliveryOrders().add(order);
	// return order;
	// }

}
