package org.camunda.bpm.bvis.Web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.camunda.bpm.bvis.EJB.CarServiceBean;
import org.camunda.bpm.bvis.EJB.OrderServiceBean;
import org.camunda.bpm.bvis.EJB.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.Entites.Car;
import org.camunda.bpm.bvis.Entites.Order;
import org.camunda.bpm.bvis.Entites.PickUpLocation;
 
@ManagedBean(name="order")
@SessionScoped
public class OrderCreateBean implements Serializable{

	@EJB
	private OrderServiceBean orderService;
	private Order order = new Order();

	//private String errorMessage;
	
	@EJB 
	private PickUpLocationServiceBean locationService;
	
	@EJB
	private CarServiceBean carService;
	
	public Order getOrder() {
		return order;
	}
	
	public Collection<Car> getAllCars() {
		return carService.getAllCars();
	}
	
	public Collection<PickUpLocation> getAllPickUpLocations() {
		return locationService.getAllLocations();
	}
}