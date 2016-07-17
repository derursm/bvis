package org.camunda.bpm.bvis.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.entities.RentalOrder;

@Named
@RequestScoped
@ManagedBean(name = "listOrders")
public class ListOrdersBackingBean {

	@EJB
	CustomerServiceBean customerService;
	
	@ManagedProperty(value="#{webSession}")
	private WebSession sessionBean;
	
	private Collection<RentalOrder> orders;
	
	public void setSessionBean(WebSession sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public Collection<RentalOrder> getOrders() {
		if (orders == null && sessionBean.isLoggedIn()) {
			orders = customerService.getCustomer(sessionBean.getUid()).getOrders();
			System.out.println(orders.size() + " ORDERS FOUND");
			System.out.println("print orders element " + orders.iterator().next().getOrderID());
			if (orders == null) return new ArrayList<RentalOrder>();
			else {
				return orders;
			}
		}
		return orders;
	}
	
}
