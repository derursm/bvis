package org.camunda.bpm.bvis.web.util;

import javax.faces.context.FacesContext;


/**
 * Helper class that stores all URLs for parts of the website
 *
 */
public class WebUrls {

	public static final String INDEX = "/bvis/index.xhtml";
	public static final String DAMAGE_REPORT = "/bvis/damageReport.xhtml";
	public static final String ORDER_SUBMITTED = "/bvis/rentalOrderSubmitted.xhtml";
	public static final String REGISTRATION_SUCCESSFUL = "/bvis/registrationSuccessful.xhtml";
	public static final String DAMAGE_REPORT_SUBMITTED = "/bvis/damageReportSubmitted.xhtml";
	public static final String MODIFY_FLEET_ORDER = "/bvis/modifyFleetOrder.jsf";
	public static final String MODIFY_FLEET_CARS = "/bvis/modifyFleetCars.jsf";
	public static final String DEFAULT_TASKLIST = "/camunda/app/tasklist/default/#/";

	public static String getUrl(String page, Boolean completeUrl, Boolean redirect){
		StringBuilder sb = new StringBuilder();
		
		if(completeUrl){			
			sb.append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
		}
		
		sb.append(page);
		
		if(completeUrl){			
			sb.append(".xhtml");
		}
		
		if(redirect){
			sb.append("?faces-redirect=true");
		}
		
		return sb.toString();
	}
}
