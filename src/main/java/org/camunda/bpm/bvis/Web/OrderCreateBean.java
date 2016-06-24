package org.camunda.bpm.bvis.Web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.camunda.bpm.bvis.EJB.CarServiceBean;
import org.camunda.bpm.bvis.EJB.OrderServiceBean;
import org.camunda.bpm.bvis.EJB.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.Entities.Car;
import org.camunda.bpm.bvis.Entities.InsuranceType;
import org.camunda.bpm.bvis.Entities.PickUpLocation;
import org.camunda.bpm.bvis.Entities.RentalOrder;
 
@ManagedBean(name="order")
@SessionScoped
public class OrderCreateBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB
	private OrderServiceBean orderService;
	private RentalOrder rentalOrder = new RentalOrder();

	//private String errorMessage;
	
	@EJB 
	private PickUpLocationServiceBean locationService;
	
	@EJB
	private CarServiceBean carService;
	
	public RentalOrder getOrder() {
		return rentalOrder;
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
}