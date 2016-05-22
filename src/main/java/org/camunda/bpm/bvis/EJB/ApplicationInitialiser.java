package org.camunda.bpm.bvis.EJB;

//import java.util.Calendar;
//import java.util.Random;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import de.wwu.pi.acse.Pizza08.entity.Customer;
//import de.wwu.pi.acse.Pizza08.entity.DeliveryOrder;
//import de.wwu.pi.acse.Pizza08.entity.Dish;
//import de.wwu.pi.acse.Pizza08.entity.OrderLine;

@Startup @Singleton
public class ApplicationInitialiser {

	@PersistenceContext
	protected EntityManager em;
  
//  @EJB
//  private DishServiceBean dishService;
//  
//  @EJB
//  private CustomerServiceBean customerService;
//  
//  /**
//   * Creates some example data for testing purpose.
//   * 
//   * It creates multiple objects of type Customer, Dish and DeliveryOrder.
//   */
//  @PostConstruct
  public void initialise() {
//    Customer johnDoe = createCustomer("John Doe", "+49 251 833800", "Leonardo-Campus 18");
//    customerService.create(johnDoe);
//    Customer poeDameron = createCustomer("Poe Dameron", "+49 251 547578", "Peter-Wust-Straﬂe 13");
//    customerService.create(poeDameron);
//	  Customer bratPitt = createCustomer("Brat Wurst Pitt", "+49 251 8445500", "Hollywurst 90");
//	  customerService.create(bratPitt);
//
//    Dish magharita = createDish("Pizza Magharita", "Only dough, tomato sauce and cheese", 3);
//    dishService.create(magharita);
//    Dish hawaii = createDish("Pizza Hawaii", "Ham and pineapple", 6);
//    dishService.create(hawaii);
//    Dish salami = createDish("Pizza Salami", "Salami", 5);
//    dishService.create(salami);
//    Dish funghi = createDish("Pizza Funghi", "Funghi", 5);
//    dishService.create(funghi);
//    Dish donerKebabPizza = createDish(DeliveryOrderServiceBean.DONER_KEABAB_PIZZA_DISH_NAME, "Pizza with doner kebab meat.", 7);
//    dishService.create(donerKebabPizza);
//    Dish pizza08Special = createDish("Pizza Pizza08 Special", "Be surprised by the amazing toppings!", 8);
//    dishService.create(pizza08Special);
//	  Dish bratwurst = createDish("Bratwurst", "It's a Bratwurst. Nothing to describe", 2);
//	  dishService.create(bratwurst);
//	  Dish currywurst = createDish("Currywurst", "Bratwurst with curry ketchup", 3);
//	  dishService.create(currywurst);
//    
//    DeliveryOrder order = createOrderFor(johnDoe);
//    em.persist(order); 
//    addDishesToOrder(new Dish[] { hawaii, salami }, order);
//    
//    order = createOrderFor(poeDameron);
//    em.persist(order); 
//    addDishesToOrder(new Dish[] { salami, funghi }, order);
//    
//    order = createOrderFor(bratPitt);
//	  em.persist(order);
//	  addDishesToOrder(new Dish[] { bratwurst, currywurst }, order);
  }
  
//  /**
//   * Takes a list from object Dish adds each Dish to an Orderline, sets a random quantity,
//   * persists the Orderline and then adds it to the Deliveryorder.
//   * 
//   * @param dishes	a list from object Dish which should be added
//   * @param order	the DeliveryOrder the dishes get added to via an Orderline
//   */
//  private void addDishesToOrder(Dish[] dishes, DeliveryOrder order) {
//    for (Dish dish : dishes) {
//      OrderLine o = new OrderLine();
//      o.setQuantity(new Random().nextInt(3) + 1);  
//      o.setDish(dish);
//      o.setDeliveryOrder(order);
//      em.persist(o);
//      order.getOrderLines().add(o);
//    }
//  }
//
//  /**
//   * Creates and returns a Dish with the given parameters
//   * 
//   * @param name		a String representing the name for the new dish
//   * @param description	a String representing the description of the new dish
//   * @param price		a int representing the price of the new dish
//   * @return			the Dish with parameters of above set
//   */
//  private Dish createDish(String name, String description, int price) {
//    Dish dish = new Dish();
//    dish.setName(name);
//    dish.setDescription(description);
//    dish.setPrice(price);
//    return dish;
//  }
//
//  /**
//   * Creates and returns a Customer with the given parameters
//   * 
//   * @param name		a String representing the name for the new customer
//   * @param phoneNumber	a String representing a sequence of numbers (no check)
//   * @param address		a String representing the address for the new customer 
//   * @return			the Customer with parameters of above set
//   */
//  private Customer createCustomer(String name, String phoneNumber,
//      String address) {
//    Customer customer = new Customer();
//    customer.setName(name);
//    customer.setPhoneNumber(phoneNumber);
//    customer.setAddress(address);
//    return customer;
//  }
//  
//  /**
//   * Creates an empty order for the given customer with the current date and time.
//   * DeliveryOrder and Customer get a reference to each other.
//   * 
//   * @param customer	Customer object for referencing
//   * @return			DeliveryOrder object with a reference to the given customer
//   */
//  private DeliveryOrder createOrderFor(Customer customer) {
//    DeliveryOrder order = new DeliveryOrder();
//    order.setReceived(Calendar.getInstance().getTime());
//    order.setCustomer(customer);
//    customer.getDeliveryOrders().add(order);
//    return order;
//  }
  
  
}
