package org.camunda.bpm.bvis.ejb.beans;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.RentalOrder;

@Stateless
public class OrderServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public RentalOrder create(RentalOrder rentalOrder) {
		em.persist(rentalOrder);
		em.flush();
		return rentalOrder;
	}
	
	public RentalOrder getOrder(long id) {
		return em.find(RentalOrder.class, id);
	}
	
	public void updateOrder(RentalOrder rentalOrder) {
		em.merge(rentalOrder);
		em.flush();
	}
	 
	public boolean orderExists(long id) {
		try {
			RentalOrder order = em.find(RentalOrder.class, id);
			if (order != null) return true;
			else return false;
		}
		catch (Exception e) {
			return false;
		}	
	}
	
	public boolean vehicleExistsForOrder(long orderID, String vehicleID) {
		try {
			RentalOrder order = em.find(RentalOrder.class, orderID);
			Collection<Car> cars = order.getCars();
			for (Car car : cars) {
				if (car.getVehicleIdentificationNumber().equals(vehicleID)) return true;
			}
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}
}
