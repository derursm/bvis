package org.camunda.bpm.bvis.EJB;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.camunda.bpm.bvis.Entites.Car;
import org.camunda.bpm.bvis.Entites.PickUpLocation;

@Stateless
public class PickUpLocationServiceBean {
	@PersistenceContext
	protected EntityManager em;
	
	public PickUpLocation create(PickUpLocation location) {
		em.persist(location);
		em.flush();
		return location;
	}
	
	public Collection<PickUpLocation> getAllLocations() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PickUpLocation> cq = cb.createQuery(PickUpLocation.class);
		Root<PickUpLocation> rootEntry = cq.from(PickUpLocation.class);
		return em.createQuery(cq.select(rootEntry)).getResultList();
	}
	
	public PickUpLocation getPickUpLocation(long id) {
		return em.find(PickUpLocation.class, id);
	}
}
