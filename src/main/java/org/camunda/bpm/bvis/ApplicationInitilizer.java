package org.camunda.bpm.bvis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.bvis.ejb.beans.*;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarStatus;
import org.camunda.bpm.bvis.entities.CarType;
import org.camunda.bpm.bvis.entities.ClaimInsurance;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.Insurance;
import org.camunda.bpm.bvis.entities.InsuranceAnswer;
import org.camunda.bpm.bvis.entities.InsuranceType;
import org.camunda.bpm.bvis.entities.PickUpLocation;
import org.camunda.bpm.bvis.entities.RentalOrder;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.task.TaskQuery;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ACCESS;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;
import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Resources.APPLICATION;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;

@Startup
@Singleton
public class ApplicationInitilizer {
	
	@PersistenceContext
	protected EntityManager em;
	
	@Inject
	protected ProcessEngine engine;

	protected IdentityService identityService;
	protected AuthorizationService authorizationService;
	protected TaskService taskService;
	protected FilterService filterService;
	
	@EJB
	protected InsuranceServiceBean insuranceService;
	@EJB
	protected ClaimServiceBean claimService;
	@EJB
	protected CarServiceBean carService;
	@EJB
	protected CustomerServiceBean customerService;
	@EJB
	protected PickUpLocationServiceBean pickupLocationService;
	@EJB
	protected OrderServiceBean orderService;
	
	// Creates some example data for testing purpose
	@PostConstruct
	public void initialise() {

		// delete history
		CamundaCleaner cleaner = new CamundaCleaner();
		cleaner.clean(engine);
		
		identityService = engine.getIdentityService();
		authorizationService = engine.getAuthorizationService();
		taskService = engine.getTaskService();
		filterService = engine.getFilterService();
		
		// PickUpLocations
		pickupLocationService.create(new PickUpLocation("Barcelona Airport", "+34 902 40 47 04", "El Prat de Llobregat", "",
				"08820", "Barcelona", "Spain"));
		pickupLocationService.create(new PickUpLocation("Madrid Airport", "+34 913 21 10 00", "Avenida de la Hispanidad, s/n", "",
				"28042", "Madrid", "Spain"));
		PickUpLocation loc = new PickUpLocation("Valencia Airport", "+34 902 40 47 04",
				"Carretera del Aeropuerto, s/n", "", "46940", "Valencia", "Spain");
		pickupLocationService.create(loc);
		
		// Cars
		carService.create(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123456", CarType.car, CarStatus.inRepair));
		carService.create(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123457", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123458", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123459", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi A3", 1, "BC00BC", "W0L000051T2123460", CarType.car, CarStatus.available));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123461", CarType.car, CarStatus.inRepair));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123462", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123463", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123464", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123465", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123466", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123467", CarType.car, CarStatus.rented));
		carService.create(new Car("Toyota", 2014, "petrol", "Toyota Corolla", 1, "AB00AB", "W0L000051T2123468", CarType.car, CarStatus.rented));
		Car car = new Car("BMW", 2000, "Diesel", "5er BMW", 210, "abc", "MS1", CarType.kombi, CarStatus.available);
		carService.create(car);
		
		// Dummy insurance policy
		Insurance insurance = new Insurance();
		insurance.setActualCosts(new BigDecimal(1000));
		insurance.setDeductible(new BigDecimal(1000));
		insurance.setInquiryText("I need an insurance");
		insurance.setInsuranceAnswer(InsuranceAnswer.ACCEPTED);
		insurance.setPickUpDate(new Date());
		insurance.setReturnDate(new Date());
		insurance.setType(InsuranceType.partial);
		insurance.setEstimatedCosts(new BigDecimal(999));
		
		// ClaimInsurances
		insuranceService.createClaimInsurance(new ClaimInsurance("Allianz", "Marienplatz", "10", "40699", "Munich", "Germany")); 
		insuranceService.createClaimInsurance(new ClaimInsurance("Capitol", "Koelner Strasse", "20", "50678", "Cologne", "Germany")); 
		
		//Customer
		Customer cust = new Customer("Becker", "0123 456789", "Leonardo Campus", "3", "48159", "Muenster", "Germany");
		cust.setCompanyName("ERCIS");
		cust.setCompany(true);
		cust.setUsername("test");
		cust.setPassword("test");
		cust.setEmail("oli90@web.de");
		cust.setDateOfBirth(new Date());
		customerService.create(cust); // used in RentalOrder
		
		// RentalOrder
		RentalOrder order = new RentalOrder(cust, false);
		order.setInsurance(insurance);
		
		Collection<Car> cars = new ArrayList<Car>();
		cars.add(car);
		order.setCars(cars);
		orderService.create(order);
		System.out.println("DUMMY ORDER ID: " + order.getId());
		
		createCamundaUsers();
		createCamundaGroups();
		addUsersToGroups();
		adjustAuthorizations();
		createFilters();
	}
	
	private void createCamundaUsers(){
		identityService.saveUser(createUser("admin","Admin","Admin","admin"));
		identityService.saveUser(createUser("urs","Urs","the Urs","urs"));
		identityService.saveUser(createUser("marcus","Marcus","the Marcus","marcus"));
		identityService.saveUser(createUser("alan","Alan","the Alan","alan"));
		identityService.saveUser(createUser("oliver","Oliver","the Oliver","oliver"));
		identityService.saveUser(createUser("vit","Vit","the Vit","vit"));
		identityService.saveUser(createUser("lena","Lena","the Lena","lena"));
		identityService.saveUser(createUser("alex","Alex","the Alex","alex"));
	}
	
	private User createUser(String login, String fname, String lname, String pwd) {
		User user = identityService.newUser(login);
		user.setFirstName(fname);
		user.setLastName(lname);
		user.setPassword(pwd);
		user.setEmail(login + "@bvis.org");
		return user;
	}
	
	private void createCamundaGroups(){
		identityService.saveGroup(CreateGroup("bvis", "BVIS", "WORKFLOW"));
		identityService.saveGroup(CreateGroup("clerks-contract", "Clerks for Contracting", "WORKFLOW"));
		identityService.saveGroup(CreateGroup("management-contract", "Management for Contracting", "WORKFLOW"));
		identityService.saveGroup(CreateGroup("clerks-claims", "Clerks for Claims", "WORKFLOW"));
		identityService.saveGroup(CreateGroup("management-claims", "Management for Claims", "WORKFLOW"));
	}

	private Group CreateGroup(String id, String name, String type) {
		Group newGroup = identityService.newGroup(id);
		newGroup.setName(name);
		newGroup.setType(type);
		return newGroup;
	}
	
	private void addUsersToGroups(){
		identityService.createMembership("admin", Groups.CAMUNDA_ADMIN);
		
		identityService.createMembership("urs", "bvis");
		identityService.createMembership("marcus", "bvis");
		identityService.createMembership("alan", "bvis");
		identityService.createMembership("oliver", "bvis");
		identityService.createMembership("vit", "bvis");
		identityService.createMembership("lena", "bvis");
		identityService.createMembership("alex", "bvis");
		
		identityService.createMembership("urs", "clerks-contract");
		identityService.createMembership("marcus", "clerks-contract");
		identityService.createMembership("alan", "clerks-contract");
		
		identityService.createMembership("oliver", "clerks-claims");
		identityService.createMembership("vit", "clerks-claims");

		identityService.createMembership("lena", "management-contract");

		identityService.createMembership("alex", "management-claims");
	}
	
	private void adjustAuthorizations(){
		// create admin group if necessary
		if (identityService.createGroupQuery().groupId(Groups.CAMUNDA_ADMIN).count() == 0) {
			Group camundaAdminGroup = identityService.newGroup(Groups.CAMUNDA_ADMIN);
			camundaAdminGroup.setName("camunda BPM Administrators");
			camundaAdminGroup.setType(Groups.GROUP_TYPE_SYSTEM);
			identityService.saveGroup(camundaAdminGroup);
		}

		// create ADMIN authorizations on all built-in resources
		for (Resource resource : Resources.values()) {
			if (authorizationService.createAuthorizationQuery()
					.groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource)
					.resourceId(ANY).count() == 0) {
				AuthorizationEntity userAdminAuth = new AuthorizationEntity(
						AUTH_TYPE_GRANT);
				userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN);
				userAdminAuth.setResource(resource);
				userAdminAuth.setResourceId(ANY);
				userAdminAuth.addPermission(ALL);
				authorizationService.saveAuthorization(userAdminAuth);
			}
		}

		// Task-List
		Authorization bvisTasklistAuth = authorizationService
				.createNewAuthorization(AUTH_TYPE_GRANT);
		bvisTasklistAuth.setGroupId("bvis");
		bvisTasklistAuth.addPermission(ACCESS);
		bvisTasklistAuth.setResourceId("tasklist");
		bvisTasklistAuth.setResource(APPLICATION);
		authorizationService.saveAuthorization(bvisTasklistAuth);
	}
	
	private void createFilters(){

		// Personal Tasks

		Map<String, Object> filterProperties = new HashMap<String, Object>();
		filterProperties.put("description", "Tasks assigned to me");
		filterProperties.put("priority", -10);
		TaskService taskService = engine.getTaskService();
		TaskQuery query = taskService.createTaskQuery().taskAssigneeExpression("${currentUser()}");
		Filter myTasksFilter = filterService.newTaskFilter().setName("My Tasks")
				.setProperties(filterProperties).setOwner("admin").setQuery(query);
		filterService.saveFilter(myTasksFilter);
	
		Authorization globalMyTaskFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
		globalMyTaskFilterRead.setResource(FILTER);
		globalMyTaskFilterRead.setResourceId(myTasksFilter.getId());
		globalMyTaskFilterRead.addPermission(READ);
		authorizationService.saveAuthorization(globalMyTaskFilterRead);
		
		// Group Filter
		
		filterProperties.clear();
		filterProperties.put("description", "Unassigned / Open tasks");
		filterProperties.put("priority", -5);
		query = taskService.createTaskQuery()
				.taskCandidateGroupInExpression("${currentUserGroups()}").taskUnassigned();
		Filter groupTasksFilter = filterService.newTaskFilter().setName("Unassigned Group Tasks")
				.setProperties(filterProperties).setOwner("admin").setQuery(query);
		filterService.saveFilter(groupTasksFilter);
	
		Authorization globalGroupFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
		globalGroupFilterRead.setResource(FILTER);
		globalGroupFilterRead.setResourceId(groupTasksFilter.getId());
		globalGroupFilterRead.addPermission(READ);
		authorizationService.saveAuthorization(globalGroupFilterRead);
		
		// Contracting Clerk Tasks
		
		filterProperties.clear();
		filterProperties.put("description",	"New Claims");
		filterProperties.put("priority", 0);
		filterProperties.put("refresh", true);
		query = taskService.createTaskQuery().taskName("check the claim's eligibility");
		Filter newLiabilityCasesFilter = filterService.newTaskFilter()
				.setName("New Insurance Claims").setProperties(filterProperties)
				.setOwner("admin").setQuery(query);
		filterService.saveFilter(newLiabilityCasesFilter);

		Authorization newLiabilityCaseGroup1FilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		newLiabilityCaseGroup1FilterRead.setResource(FILTER);
		newLiabilityCaseGroup1FilterRead.setResourceId(newLiabilityCasesFilter
				.getId());
		newLiabilityCaseGroup1FilterRead.addPermission(READ);
		newLiabilityCaseGroup1FilterRead.setGroupId("ClaimHandler");
		authorizationService
				.saveAuthorization(newLiabilityCaseGroup1FilterRead);


		/* New Insurance Contract */
		filterProperties.clear();
		filterProperties.put("description", "All Rental Request");
		filterProperties.put("priority", 0);
		filterProperties.put("refresh", true);
		//filterProperties.put("color", "#8ad69a");
		query = taskService.createTaskQuery()
				.taskName("negotiate agreement conditions with customer (via telephone or face2face)");
		Filter openIncuranceContractsFilter = filterService.newTaskFilter()
				.setName("New Cases")
				.setProperties(filterProperties).setOwner("bvis")
				.setQuery(query);
		filterService.saveFilter(openIncuranceContractsFilter);

		Authorization newRentalAgreementGroupFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		newRentalAgreementGroupFilterRead.setResource(FILTER);
		newRentalAgreementGroupFilterRead
				.setResourceId(openIncuranceContractsFilter.getId());
		newRentalAgreementGroupFilterRead.addPermission(READ);
		newRentalAgreementGroupFilterRead.setGroupId("Contracting");
		authorizationService
				.saveAuthorization(newRentalAgreementGroupFilterRead);
		
		Authorization newRentalAgreementGroup1FilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		newRentalAgreementGroup1FilterRead.setResource(FILTER);
		newRentalAgreementGroup1FilterRead
				.setResourceId(openIncuranceContractsFilter.getId());
		newRentalAgreementGroup1FilterRead.addPermission(READ);
		newRentalAgreementGroup1FilterRead.setGroupId("Negotiations");
		authorizationService
				.saveAuthorization(newRentalAgreementGroup1FilterRead);

		// All tasks
		
		filterProperties.clear();
		filterProperties.put("description", "All Tasks");
		filterProperties.put("priority", 10);
		query = taskService.createTaskQuery();
		Filter allTasksFilter = filterService.newTaskFilter()
				.setName("All Tasks").setProperties(filterProperties)
				.setOwner("admin").setQuery(query);
		filterService.saveFilter(allTasksFilter);
	}
}
