package org.camunda.bpm.bvis.EJB;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.camunda.bpm.bvis.Entities.Car;

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
	
	public Car getCar(long id) {
		return em.find(Car.class, id);
	}
}
