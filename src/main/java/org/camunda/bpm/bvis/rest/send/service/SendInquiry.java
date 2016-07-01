package org.camunda.bpm.bvis.rest.send.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.EJB.OrderServiceBean;
import org.camunda.bpm.bvis.Entities.Customer;
import org.camunda.bpm.bvis.Entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.dto.ContractDetailsDTO;
import org.camunda.bpm.bvis.rest.send.dto.Order;
import org.camunda.bpm.bvis.rest.send.dto.Insurance;
import org.camunda.bpm.bvis.rest.send.dto.User;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ManagedBean
@ConversationScoped
@Named
public class SendInquiry implements Serializable{

	private static final long serialVersionUID = 1L;
	//private static final String BASE_URI = "http://camunda-capitol.uni-muenster.de/partner-interface/";
	private static final String BASE_URI = "localhost:8080/bvis/api/";
	
	
	@EJB
	private OrderServiceBean orderService;
	
	@Inject
	private BusinessProcess businessProcess;

	
	public void sendInquiry(RentalOrder entityOrder, String processInstanceID) {
		// parse RentalOrder object to Order object (which represents the agreed upon JSON format)
		System.out.println("PARSING SENDING OBJECT");
		User user = parseUser(entityOrder);
		Collection<org.camunda.bpm.bvis.rest.send.dto.Car> cars = parseCars(entityOrder);
		org.camunda.bpm.bvis.rest.send.dto.Insurance insurance = parseInsurance(entityOrder);
		Order order = parseOrder(cars, insurance, user, entityOrder);
		ContractDetailsDTO contractDetails = new ContractDetailsDTO();
		contractDetails.setOrder(order);
		contractDetails.setProcessinstance_id_bvis(processInstanceID);
		System.out.println("SENDING OBJECT SUCCESSFULLY PARSED");

		System.out.println("SENDING PARSED OBJECT");
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
		ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI);
		SendInquiryClient senderClient = target.proxy(SendInquiryClient.class);
		senderClient.sendInquiry(contractDetails);
	}
	
	/**
	 * Helper method that parses a User object out of the RentalOrder
	 * @param order
	 * @return
	 */
	private User parseUser(RentalOrder order) {
		Customer customer = order.getCustomer();
		User user = new User();
		user.setCity(customer.getCity());
		user.setCompany(customer.getCompany());
		user.setCompany_name(customer.getCompanyName());
		user.setCountry(customer.getCountry());
		user.setDate_of_birth(customer.getDateOfBirth().toString());
		user.setEmail(customer.getEmail());
		user.setFirstname(customer.getFirstname());
		user.setHouse_number(customer.getHouseNumber());
		user.setPhone_number(customer.getPhoneNumber());
		user.setPostcode(customer.getPostcode());
		user.setStreet(customer.getStreet());
		user.setSurname(customer.getSurname());
		return user;
	}
	
	private Collection<org.camunda.bpm.bvis.rest.send.dto.Car> parseCars(RentalOrder order) {
		org.camunda.bpm.bvis.rest.send.dto.Car car = new org.camunda.bpm.bvis.rest.send.dto.Car();
		Collection<org.camunda.bpm.bvis.Entities.Car> entityCars = order.getCars();
		ArrayList<org.camunda.bpm.bvis.rest.send.dto.Car> cars = new ArrayList<org.camunda.bpm.bvis.rest.send.dto.Car>();
		for (org.camunda.bpm.bvis.Entities.Car entityCar : entityCars) {
			car.setBrand(entityCar.getBrand());
			car.setConstruction_year(entityCar.getConstructionYear());
			car.setFuel_type(entityCar.getFuelType());
			car.setModel(entityCar.getModel());
			car.setPs(entityCar.getPs());
			car.setRegistration_number(entityCar.getRegistrationNumber());
			car.setType(entityCar.getType().toString());
			car.setVehicle_identification_number(entityCar.getVehicleIdentificationNumber());
			cars.add(car);
		}
		return cars;
	}
	
	private org.camunda.bpm.bvis.rest.send.dto.Insurance parseInsurance(RentalOrder rentalOrder) {
		org.camunda.bpm.bvis.rest.send.dto.Insurance insurance = new org.camunda.bpm.bvis.rest.send.dto.Insurance();
		org.camunda.bpm.bvis.Entities.Insurance entityInsurance = rentalOrder.getInsurance();
		
		// set alternative for insurance == null for testing purposes
		if (entityInsurance == null) {
			insurance.setDeductible(1337);
			insurance.setInsurance_id(1337);
			insurance.setPick_up_date("PickupDate");
			insurance.setReturn_date("ReturnDate");
			insurance.setType("TestType");
		}
		else {
			insurance.setDeductible(Double.parseDouble(entityInsurance.getDeductible().toString()));
			insurance.setInsurance_id(Integer.parseInt(entityInsurance.getInsuranceID()+""));
			insurance.setPick_up_date(entityInsurance.getPickUpDate().toString());
			insurance.setReturn_date(entityInsurance.getReturnDate().toString());
			insurance.setType(entityInsurance.getType().toString());	
		}
		return insurance;
	}
	
	private Order parseOrder(Collection<org.camunda.bpm.bvis.rest.send.dto.Car> cars, 
			org.camunda.bpm.bvis.rest.send.dto.Insurance insurance, User user, RentalOrder rentalOrder) {
		Order order = new Order();
		order.setCar(cars);
		order.setUser(user);
		order.setFleet_rental((cars.size() > 1) ? true : false);
		order.setInquiry_text(rentalOrder.getInquiryText());
		order.setInsurance(insurance);
		order.setOrder_id(Integer.parseInt(rentalOrder.getOrderID()+""));
		order.setRequest_date(rentalOrder.getRequestDate().toString());
		return order;
	}
}