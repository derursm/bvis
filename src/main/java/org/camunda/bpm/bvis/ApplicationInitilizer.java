package org.camunda.bpm.bvis;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        pickupLocationService.create(new PickUpLocation("Muenster-Osnabrueck Airport", "+49 2571 94 3360", "Airportallee", "1",
                "48268", "Greven", "Germany"));
        pickupLocationService.create(new PickUpLocation("Hamburg Airport", "+49 40 507 50", "Flughafenstrasse", "1",
                "22335", "Hamburg", "Germany"));
        pickupLocationService.create(new PickUpLocation("New York Airport", "+1 718 244 44 44", "New York", "NY",
                "11430", "New York", "United States of America"));
		PickUpLocation loc = new PickUpLocation("Hong Kong Airport", "+852 2181 88 88", "Sky Plaza Road", "1",
                "1", "Hong Kong", "China - special treatment");
		pickupLocationService.create(loc);
		
		// Cars
		carService.create(new Car("Audi", 2015, "petrol", "Audi RS 3 sportback", 367, "BVISAUDI001", "B-VIS 201", CarType.car, CarStatus.inRepair));
		carService.create(new Car("Audi", 2015, "petrol", "Audi S8 plus", 605, "BVISAUDI002", "B-VIS 202", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi RS Q3", 340, "BVISAUDI003", "B-VIS 203", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi TT RS", 400, "BVISAUDI004", "B-VIS 204", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "petrol", "Audi R8 Coupe", 610, "BVISAUDI005", "B-VIS 205", CarType.car, CarStatus.available));
		carService.create(new Car("Audi", 2015, "electricity", "Audi A3 sportback e-tron", 204, "BVISAUDI006", "B-VIS 206", CarType.car, CarStatus.available));
		carService.create(new Car("BMW", 2016, "electricity", "BMW i3", 170, "BVISBMW007", "B-VIS 207", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "petrol and electricity", "BMW i8", 362, "BVISBMW008", "B-VIS 208", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "petrol", "BMW Z4", 340, "BVISBMW009", "B-VIS 209", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "electricity", "BMW M4 cabrio", 431, "BVISBMW010", "B-VIS 210", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "diesel", "BMW M6 gran coupe", 560, "BVISBMW011", "B-VIS 211", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "electricity", "BMW M6 cabrio", 560, "BVISBMW012", "B-VIS 212", CarType.car, CarStatus.inRepair));
		carService.create(new Car("BMW", 2016, "electricity", "BMW X6 m", 575, "BVISBMW013", "B-VIS 213", CarType.car, CarStatus.inRepair));
		carService.create(new Car("TESLA", 2016, "electricity", "TESLA X P90D", 773, "BVISBMW014", "B-VIS 214", CarType.car, CarStatus.inRepair));
		carService.create(new Car("TESLA", 2016, "electricity", "TESLA X P90D", 773, "BVISBMW015", "B-VIS 215", CarType.car, CarStatus.inRepair));
		carService.create(new Car("TESLA", 2016, "electricity", "TESLA S 75D", 332, "BVISBMW016", "B-VIS 216", CarType.car, CarStatus.inRepair));
		carService.create(new Car("TESLA", 2016, "electricity", "TESLA S 90D", 422, "BVISBMW017", "B-VIS 217", CarType.car, CarStatus.inRepair));
		carService.create(new Car("TESLA", 2016, "electricity", "TESLA S P90D", 1008, "BVISBMW018", "B-VIS 218", CarType.car, CarStatus.inRepair));

		Car car = new Car("Mercedes Benz", 1957, "petrol", "Mercedes Benz 300 SL Roadster", 215, "BVISBMW019", "B-VIS 219", CarType.kombi, CarStatus.available);
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date pickup = sdf.parse("17/07/2016");
			Date end = sdf.parse("24/07/2016");
			order.setPick_up_date(pickup);
			order.setReturn_date(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
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
