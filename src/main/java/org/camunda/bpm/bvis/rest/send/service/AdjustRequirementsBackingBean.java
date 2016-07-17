package org.camunda.bpm.bvis.rest.send.service;

import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.camunda.bpm.bvis.ejb.beans.CarServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.RentalOrder;

@ManagedBean(name="adjustRequirements")
@RequestScoped
public class AdjustRequirementsBackingBean {
	
	@EJB
	protected CarServiceBean carService;
	@EJB
	protected OrderServiceBean orderService;
	
	private long carID;

	public CarServiceBean getCarService() {
		return carService;
	}

	public void setCarService(CarServiceBean carService) {
		this.carService = carService;
	}
	
	public Collection<Car> getAvailableCars(long orderId) {
		RentalOrder order = orderService.getOrder(orderId);
		Date begin = order.getPick_up_date();
		Date end = order.getReturn_date();
		return carService.getAvailableCarsForPeriod(begin, end);
	}
	
}
