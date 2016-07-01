package org.camunda.bpm.bvis.Web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.EJB.CarServiceBean;
import org.camunda.bpm.bvis.EJB.ContractHandler;
import org.camunda.bpm.bvis.EJB.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.Entities.Car;
import org.camunda.bpm.bvis.Entities.InsuranceType;
import org.camunda.bpm.bvis.Entities.PickUpLocation;
import org.camunda.bpm.bvis.Entities.RentalOrder;
import org.camunda.bpm.engine.cdi.BusinessProcess;

@Named
@ConversationScoped
@ManagedBean(name = "order")
public class OrderBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// Inject the BusinessProcess to access the process variables
	@Inject
	private BusinessProcess businessProcess;

	@Inject
	private ContractHandler contractHandler;

	@EJB
	private CarServiceBean carService;
	
	@EJB
	private PickUpLocationServiceBean locationService;
	
	// Caches the RentalOrder during the conversation
	private RentalOrder rentalOrder;

	public RentalOrder getRentalOrder() {
		if (rentalOrder == null) {
			// Load the order entity from the database if not already cached
			rentalOrder = contractHandler.getOrder((Long) businessProcess.getVariable("orderId"));
		}
		return rentalOrder;
	}
	
	public Long getCarID() {
		if(rentalOrder.getCars().isEmpty()) {
			return null;
		}
		else {
			return rentalOrder.getCars().iterator().next().getId();
		}
	}
	
	public List<SelectItem> getValidAmounts() {
		List<SelectItem> validAmounts = new ArrayList<SelectItem>();
		for (int i=1; i<21; i++) {
			validAmounts.add(new SelectItem(i));
		}
		return validAmounts;
	}
	
	public Collection<Car> getAllCars() {
		return carService.getAllCars();
	}
	
	public Collection<PickUpLocation> getAllPickUpLocations() {
		return locationService.getAllLocations();
	}
	
	public PickUpLocation getPickUpLocation() {
		return locationService.getPickUpLocation(new Long(2));
	}
	
	public InsuranceType[] getInsuranceTypes() {
		return InsuranceType.values();
	}

	public void submitForm() throws IOException {
		// Persist updated order entity and complete task form
		contractHandler.updateOrder(rentalOrder);
	}
	
	public void updateFleetOrder() throws IOException {
		String numberOfCars = businessProcess.getVariable("numberOfCars");
        Long carId = (Long.parseLong((String)businessProcess.getVariable("car")));
		
        Car car = carService.getCar(carId);
        Collection<Car> cars = rentalOrder.getCars();
        
		for (int i=0; i< Integer.parseInt(numberOfCars); i++) {
	        cars.add(car);
		}
    	Long pickUpLocationId = (Long.parseLong((String)businessProcess.getVariable("pickUpLoc")));
    	Long returnStoreId = (Long.parseLong((String)businessProcess.getVariable("returnStore")));
    	
    	rentalOrder.setPickUpStore((PickUpLocation) locationService.getPickUpLocation(pickUpLocationId));
    	rentalOrder.setReturnStore((PickUpLocation) locationService.getPickUpLocation(returnStoreId));
    
		rentalOrder.setCars(cars);
		contractHandler.updateOrder(rentalOrder);
	}
}
