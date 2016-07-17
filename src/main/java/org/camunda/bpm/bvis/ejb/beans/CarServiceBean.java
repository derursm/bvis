package org.camunda.bpm.bvis.ejb.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarStatus;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.RentalOrder;

import com.itextpdf.text.List;

@Stateless
public class CarServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public Car create(Car car) {
		em.persist(car);
		return car;
	}
	
	public Collection<Car> getAllCars() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Car> cq = cb.createQuery(Car.class);
		Root<Car> rootEntry = cq.from(Car.class);
		return em.createQuery(cq.select(rootEntry)).getResultList();
	}
	
	public Collection<String> getAllCarNames() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Car> cq = cb.createQuery(Car.class);
		Root<Car> rootEntry = cq.from(Car.class);
		Collection<String> nameList = new ArrayList<String>();
		for (Car carItem : em.createQuery(cq.select(rootEntry)).getResultList()){
			nameList.add(carItem.getModel());
		}
		return nameList;
	}
	
	public Car getCar(long id) {
		return em.find(Car.class, id);
	}
	
	public Car getCarByVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		final String querystring = "SELECT c FROM Car c WHERE c.vehicleIdentificationNumber = :vehicleIdentificationNumber";
		TypedQuery<Car> query = em.createQuery(querystring, Car.class);
		query.setParameter("vehicleIdentificationNumber", vehicleIdentificationNumber);
		return query.getResultList().get(0);
	}
	
	@Deprecated
	public Car getAvailableCarByModel(String model) {
		final String querystring = "SELECT c FROM Car c WHERE c.model = :model AND c.carStatus = :carStatus";
		TypedQuery<Car> query = em.createQuery(querystring, Car.class);
		query.setParameter("model", model);
		query.setParameter("carStatus", CarStatus.available);
		
		Collection <Car> res = query.getResultList();
		if (res.toArray().length == 0) {
			return null;
		}
		else { 
			return query.getResultList().get(0);
		}
	}
	
	public Collection<Car> getAllCarsByModel(String model) {
		final String querystring = "SELECT c FROM Car c WHERE c.model = :model";
		TypedQuery<Car> query = em.createQuery(querystring, Car.class);
		query.setParameter("model", model);
		
		Collection <Car> res = query.getResultList();
		if (res.toArray().length == 0) {
			return null;
		}
		else { 
			return query.getResultList();
		}
	}
	
	public Collection<Car> getAvailableCarsForPeriod(Date begin, Date end) {
		final String querystring = "SELECT c FROM Car c WHERE c.carStatus = :carStatus";
		TypedQuery<Car> query = em.createQuery(querystring, Car.class);
		query.setParameter("carStatus", CarStatus.available);
		
		Collection<Car> allCars = query.getResultList();
		ArrayList<Car> availableCars = new ArrayList<Car>();
		for (Car car : allCars) {
			for (RentalOrder order : car.getRentalOrders()) {
				if ((order.getPick_up_date().before(begin) || order.getPick_up_date().equals(begin)) 
						&& (order.getReturn_date().after(begin) || order.getReturn_date().equals(begin))) break;
				else if ((order.getPick_up_date().after(begin) || order.getPick_up_date().equals(begin))
						&& (order.getPick_up_date().before(end) || order.getPick_up_date().equals(end))) break;
				else availableCars.add(car);
			}
			if (car.getRentalOrders().size() == 0) availableCars.add(car);
		}
		return(availableCars);
	}
	
	public void updateCar(Car car) {
		em.merge(car);
		em.flush();
	}
}
