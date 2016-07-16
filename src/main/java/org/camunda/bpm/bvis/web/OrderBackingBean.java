package org.camunda.bpm.bvis.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.ContractHandler;
import org.camunda.bpm.bvis.ejb.beans.CarServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.ejb.beans.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.InsuranceType;
import org.camunda.bpm.bvis.entities.PickUpLocation;
import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.web.util.WebUrls;
import org.camunda.bpm.engine.cdi.BusinessProcess;

@Named
@RequestScoped
@ManagedBean(name = "order")
public class OrderBackingBean {

	// Inject the BusinessProcess to access the process variables
	@Inject
	private BusinessProcess businessProcess;

	@Inject
	private ContractHandler contractHandler;

	@EJB
	private CarServiceBean carService;

	@EJB
	private PickUpLocationServiceBean locationService;
	
	@EJB
	private OrderServiceBean orderService;

	// Caches the RentalOrder during the conversation
	private RentalOrder rentalOrder;
	private int numberOfCars;
	
	public RentalOrder getRentalOrder() {
		if (rentalOrder == null) {
			// Load the order entity from the database if not already cached
			rentalOrder = contractHandler.getOrder((Long) businessProcess.getVariable("orderId"));
		}
		return rentalOrder;
	}
	
	public int getNumberOfCars() {
		if (rentalOrder == null) {
			rentalOrder = contractHandler.getOrder((Long) businessProcess.getVariable("orderId"));
		}
		return rentalOrder.getCars().size();
	}
	
	public void setNumberOfCars(int num) {
		numberOfCars = num;
	}

	public Long getCarID() {
		if (rentalOrder.getCars().isEmpty()) {
			return null;
		} else {
			return rentalOrder.getCars().iterator().next().getId();
		}
	}

	public List<SelectItem> getValidAmounts() {
		List<SelectItem> validAmounts = new ArrayList<SelectItem>();
		for (int i = 1; i < 21; i++) {
			validAmounts.add(new SelectItem(i));
		}
		return validAmounts;
	}

	public Collection<Car> getAllCars() {
		return carService.getAllCars();
	}

	public Collection<String> getAllCarNames() {
		return carService.getAllCarNames();
	}

	public Collection<PickUpLocation> getAllPickUpLocations() {
		return locationService.getAllLocations();
	}

	public PickUpLocation getPickUpLocation() {
		return locationService.getPickUpLocation(new Long(2));
	}

	public Collection<String> getAllPickUpLocationNames() {
		return locationService.getAllLocationNames();
	}

	public InsuranceType[] getInsuranceTypes() {
		return InsuranceType.values();
	}

	public void submitForm() throws IOException {
		// Persist updated order entity and complete task form
		contractHandler.updateOrder(rentalOrder, true);
	}

	public void setFleetSize() throws IOException {
		Car car = getAllCars().iterator().next();
		Collection<Car> cars = new ArrayList<Car>();

		for (int i = 0; i < numberOfCars; i++) {
			cars.add(car);
		}
		rentalOrder.setCars(cars);
		contractHandler.updateOrder(rentalOrder,false);
		
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(WebUrls.MODIFY_FLEET_CARS + "?taskId=" + businessProcess.getTaskId()
						+ "&callbackUrl=" + WebUrls.DEFAULT_TASKLIST);

	}

	public void updateFleetOrder() {
		Long pickUpLocationId = (Long.parseLong((String) businessProcess.getVariable("pickUpLoc")));
		Long returnStoreId = (Long.parseLong((String) businessProcess.getVariable("returnStore")));

		rentalOrder.setPickUpStore((PickUpLocation) locationService.getPickUpLocation(pickUpLocationId));
		rentalOrder.setReturnStore((PickUpLocation) locationService.getPickUpLocation(returnStoreId));

		contractHandler.updateOrder(rentalOrder, true);
	}
	
	public void backToModifyFleetOrder() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(WebUrls.MODIFY_FLEET_ORDER + "?taskId=" + businessProcess.getTaskId()
						+ "&callbackUrl=/#/");

		
	}
	
}
