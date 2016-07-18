package org.camunda.bpm.bvis.ejb;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.bvis.ejb.beans.CarServiceBean;
import org.camunda.bpm.bvis.ejb.beans.CustomerServiceBean;
import org.camunda.bpm.bvis.ejb.beans.OrderServiceBean;
import org.camunda.bpm.bvis.ejb.beans.PickUpLocationServiceBean;
import org.camunda.bpm.bvis.entities.Car;
import org.camunda.bpm.bvis.entities.CarPriceMap;
import org.camunda.bpm.bvis.entities.Customer;
import org.camunda.bpm.bvis.entities.Insurance;
import org.camunda.bpm.bvis.entities.InsuranceAnswer;
import org.camunda.bpm.bvis.entities.InsurancePriceMap;
import org.camunda.bpm.bvis.entities.InsuranceType;
import org.camunda.bpm.bvis.entities.OrderStatus;
import org.camunda.bpm.bvis.entities.PickUpLocation;
import org.camunda.bpm.bvis.entities.RentalOrder;
import org.camunda.bpm.bvis.rest.send.service.SendContractConfirmation;
import org.camunda.bpm.bvis.rest.send.service.SendContractConfirmationClient;
import org.camunda.bpm.bvis.rest.send.service.SendInquiry;
import org.camunda.bpm.bvis.util.SendHTMLEmail;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

//pdf
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Stateless
@Named
public class ContractHandler {

	@EJB
	private OrderServiceBean orderService;

	@EJB
	private CustomerServiceBean customerService;

	@EJB
	private PickUpLocationServiceBean locationService;

	@EJB
	private CarServiceBean carService;

	// Inject task form available through the Camunda cdi artifact
	@Inject
	private TaskForm taskForm;

	@Inject
	private BusinessProcess businessProcess;

	// private static SendHTMLEmail sendMail;

	public void persistOrder(DelegateExecution delegateExecution) throws ParseException {
		double estimatedInsurancPrice = 0;
		
		// Create new order instance
		System.out.println("Process instance ID " + businessProcess.getProcessInstanceId());
		System.out.println("Execution ID: " + businessProcess.getExecutionId());
		RentalOrder rentalOrder = new RentalOrder();

		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
		Customer customer = new Customer();
		
		if (variables.get("customerID") != null) {
			customer = customerService.getCustomer(Long.parseLong(variables.get("customerID")+""));
		}
		else {
			customer.setFirstname((String) variables.get("customerFirstname"));
			customer.setSurname((String) variables.get("customerSurname"));
			String customerCompanyName = (String) variables.get("customerCompanyName");

			boolean isCompany = true;
			if (customerCompanyName.isEmpty()) {
				isCompany = false;
			}

			customer.setCompanyName(customerCompanyName);
			customer.setCompany(isCompany);

			customer.setEmail((String) variables.get("customerEmail"));
			customer.setPhoneNumber((String) variables.get("customerPhoneNumber"));
			System.out.println(variables.get("customerDateOfBirth"));
			customer.setDateOfBirth((Date) variables.get("customerDateOfBirth"));
			customer.setStreet((String) variables.get("customerStreet"));
			customer.setHouseNumber((String) variables.get("customerHouseNumber"));
			customer.setPostcode((String) variables.get("customerPostcode"));
			customer.setCity((String) variables.get("customerCity"));
			customer.setCountry((String) variables.get("customerCountry"));
			customer.setEligibility(false);

			customerService.create(customer);
		}

		// Set order attributes
		rentalOrder.setCustomer(customer);
		rentalOrder.setRequestDate(new Date());

		boolean isFleet = (Boolean) variables.get("fleet");
		if (!isFleet) {
			Date pickUpDate = (Date) variables.get("pickUpDate");
			rentalOrder.setPick_up_date(pickUpDate);
			Date returnDate = (Date) variables.get("returnDate");
			rentalOrder.setReturn_date(returnDate);

			Long pickUpLocationId = (Long) variables.get("pickUpLoc");
			Long returnStoreId = (Long) variables.get("returnStore");

			rentalOrder.setPickUpStore((PickUpLocation) locationService.getPickUpLocation(pickUpLocationId));
			rentalOrder.setReturnStore((PickUpLocation) locationService.getPickUpLocation(returnStoreId));

			InsuranceType insuranceType = (InsuranceType) variables.get("insuranceType");

			Long carId = (Long) variables.get("car");

			Car car = carService.getCar(carId);
			Collection<Car> cars = new ArrayList<Car>();
			cars.add(car);

			rentalOrder.setCars((Collection<Car>) cars);

			// calculate price for cars
			double priceCars = calcCarPrice(cars, returnDate, pickUpDate);
			rentalOrder.setPriceCars(priceCars);

			// calculate price for insurance
			estimatedInsurancPrice = calcInsurancePrice(cars, insuranceType, returnDate, pickUpDate);
			if (estimatedInsurancPrice < 0) estimatedInsurancPrice = 0;
		}
		rentalOrder.setFleetRental(isFleet);
		rentalOrder.setInquiryText((String) variables.get("inquiryText"));

		rentalOrder.setClerkComments("");
		rentalOrder.setApproveStatus(false);

		// TODO GENERATE A PROPER INSURANCE. THIS IS JUST A DUMMY FOR TESTING
		// PURPOSES
		Insurance insurance = new Insurance();		
		insurance.setPickUpDate((Date) variables.get("pickUpDate"));
		insurance.setReturnDate((Date) variables.get("returnDate"));
		insurance.setOrder(rentalOrder);
		System.out.println(variables.get("insuranceType"));
		if(Objects.equals(((InsuranceType) variables.get("insuranceType")).toString(),"total")){
			insurance.setDeductible(new BigDecimal(0));
			insurance.setType(InsuranceType.total);
			System.out.println("Set total insurance");
		} 
		if(Objects.equals(((InsuranceType) variables.get("insuranceType")).toString(),"partial")){
			insurance.setDeductible(new BigDecimal(1000));
			insurance.setType(InsuranceType.partial);
			System.out.println("Set partial insurance");
		} 
		if(Objects.equals(((InsuranceType) variables.get("insuranceType")).toString(),"liability")){
			insurance.setDeductible(new BigDecimal(2000));
			insurance.setType(InsuranceType.liability);
			System.out.println("Set liablilty insurance");
		} 
	
		insurance.setEstimatedCosts(new BigDecimal(estimatedInsurancPrice));
		
		rentalOrder.setInsurance(insurance);
		rentalOrder.setOrderStatus(OrderStatus.PENDING);
		orderService.create(rentalOrder);
		System.out.println("Cars: " + rentalOrder.getCars());

		// Remove no longer needed process variables
		delegateExecution.removeVariables(variables.keySet());

		// Add newly created order id as process variable
		delegateExecution.setVariable("orderId", rentalOrder.getId());
		delegateExecution.setVariable("fleet", rentalOrder.isFleetRental());
		delegateExecution.setVariable("processId", delegateExecution.getProcessInstanceId());
		System.out.println("CREATED ORDER WITH ORDER ID: " + rentalOrder.getId());
	}

	// Create contract and send to user's email
	public void sendContract(DelegateExecution delegateExecution) {
		System.out.println("Start creating contract");

		RentalOrder order;
		Customer customer;
		Collection<Car> cars;
		String surname, email, pickupLocation, returnLocation, insurancePac, carModel, rentalEnd, rentalStart, date,
				type, street, city, orderId_str, vehicleIdent;
		double totalPrice, insurancePrice, rentalPrice;
		Long orderId;

		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
		orderId = (long) variables.get("orderId");
		order = orderService.getOrder(orderId);
		customer = order.getCustomer();
		cars = order.getCars();
		order.setOrderStatus(OrderStatus.ACCEPTED);

		surname = "surname";
		email = "email";
		pickupLocation = "pickupLocation";
		returnLocation = "returnLocation";
		carModel = "carmodel";
		rentalStart = "rentalStart";
		rentalEnd = "rentalEnd";
		orderId_str = "orderId";
		insurancePac = "insurancePac";
		rentalPrice = 0;
		insurancePrice = 0;
		totalPrice = 0;
		vehicleIdent = order.getCars().iterator().next().getVehicleIdentificationNumber();

		// Get rental information
		if (order.isFleetRental()) {
			type = "Fleet";
		} else {
			type = "Private Single Rental";
		}
		orderId_str = orderId.toString();
		surname = customer.getSurname();
		street = customer.getStreet() + " " + customer.getHouseNumber();
		city = customer.getPostcode() + " " + customer.getCity();
		email = customer.getEmail();
		rentalStart = order.getPick_up_date().toString();
		rentalEnd = order.getReturn_date().toString();
		pickupLocation = order.getPickUpStore().getContactDetails();
		returnLocation = order.getReturnStore().getContactDetails();
		// insurancePac = order.getInsurance().getType();
		rentalPrice = order.getPriceCars();
		insurancePrice = order.getInsurance().getActualCosts().doubleValue();
		totalPrice = order.getPrice();
		insurancePac = order.getInsurance().getType().toString();

		carModel = "";
		int i = 1;
		for (Car loop_car : cars) {
			carModel += String.valueOf(i) + ": " + loop_car.getHTMLCarDetails();
		}

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		date = formatter.format(today);

		// surname, email, pickupLocation, returnLocation,
		// insurancePac, carModel, rentalEnd, rentalStart, orderId_str, date,
		// type, street, city;
		// double totalPrice, insurancePrice, rentalPrice;
		// Long orderId;

		final String[][] rentalData = { { "Type:", type }, { "Pick-up date:", rentalStart },
				{ "Return date:", rentalEnd }, { "Pick-up location:", pickupLocation },
				{ "Return location:", returnLocation }, { "Car:", carModel }, { "Insurance type:", insurancePac },
				{ "Price insurance:", String.valueOf(insurancePrice) + " EUR" },
				{ "Price car:", String.valueOf(rentalPrice) + " EUR" },
				{ "Vehicle identification number:", String.valueOf(vehicleIdent) },
				{ "Total:", String.valueOf(totalPrice) + " EUR" }, };

		try {
			Document document = new Document();
			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
			PdfWriter pdf = null;
			pdf = PdfWriter.getInstance(document, baosPDF);
			document.open();
			Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
			Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
			Paragraph pa0 = new Paragraph("Order ID: " + orderId_str, paragraphFont);
			pa0.setAlignment(Element.ALIGN_RIGHT);
			pa0.setFont(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
			document.add(pa0);

			Chunk chunk = new Chunk("BVIS Rental Contract", chapterFont);
			document.add(chunk);
			Paragraph pa1 = new Paragraph("The best choice", paragraphFont);
			pa1.setSpacingAfter(15);
			document.add(pa1);

			List list = new List();
			list.setListSymbol("");
			list.add(new ListItem("Mr/Mrs " + surname));
			list.add(new ListItem(street));
			list.add(new ListItem(city));
			Paragraph pa2 = new Paragraph();
			pa2.add(list);
			pa2.setSpacingAfter(20);
			document.add(pa2);

			Paragraph datePar = new Paragraph(date, paragraphFont);
			datePar.setAlignment(Element.ALIGN_RIGHT);
			document.add(datePar);

			document.add(new Paragraph("Dear Mr/Mrs " + surname + ",", paragraphFont));
			Paragraph pa3 = new Paragraph(
					"thank you for choosing BVIS as your transportation service provider. Please find your rental details below:",
					paragraphFont);
			pa3.setSpacingAfter(10);
			document.add(pa3);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(80);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.setWidths(new int[] { 5, 10 });
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.addCell(rentalData[0][0]);
			table.addCell(rentalData[0][1]);
			table.addCell(rentalData[1][0]);
			table.addCell(rentalData[1][1]);
			table.addCell(rentalData[2][0]);
			table.addCell(rentalData[2][1]);
			table.addCell(rentalData[3][0]);
			table.addCell(rentalData[3][1]);
			table.addCell(rentalData[4][0]);
			table.addCell(rentalData[4][1]);
			table.addCell(rentalData[5][0]);
			table.addCell(rentalData[5][1]);
			table.addCell(rentalData[6][0]);
			table.addCell(rentalData[6][1]);
			table.addCell(rentalData[7][0]);
			table.addCell(rentalData[7][1]);
			table.addCell(rentalData[8][0]);
			table.addCell(rentalData[8][1]);
			table.addCell(rentalData[9][0]);
			table.addCell(rentalData[9][1]);
			table.addCell(rentalData[10][0]);
			table.addCell(rentalData[10][1]);

			Paragraph pa4 = new Paragraph();
			pa4.add(table);
			pa4.setSpacingAfter(15);
			document.add(pa4);

			document.add(new Paragraph("Thank you and have a nice ride", paragraphFont));

			document.close();
			pdf.close();

			SendHTMLEmail.mainAtt("Final contract",
					"Dear Mr/Mrs " + surname + ". <br> Please find attached your contract.", "bvis@test.de", email,
					baosPDF, "bvis_contract_" + surname);
		} catch (DocumentException e) {
			System.out.println("Contracting DocumentException");
		}
	}

	public RentalOrder getOrder(Long orderId) {
		// Load order entity from database
		return orderService.getOrder(orderId);
	}

	public boolean isFleetOrder(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		boolean isFleetOrder = (Boolean) variables.get("fleet");
		return isFleetOrder;
	}

	public void updateOrder(RentalOrder rentalOrder, boolean completeTask) {
		// Merge detached order entity with current persisted state
		orderService.updateOrder(rentalOrder);

		if (completeTask) {
			try {
				// Complete user task from
				taskForm.completeTask();
			} catch (IOException e) {
				// Rollback both transactions on error
				throw new RuntimeException("Cannot complete task", e);
			}
		}
	}

	// General send email method. State states the content of the email. Email
	// information is caught from
	// process variables
	public void sendOrderStateNotification(DelegateExecution delegateExecution) throws ParseException {

		RentalOrder order;
		Customer customer;
		Car car;
		Collection<Car> cars;
		String surname, subject, text, from, email, state, path, pickupLocation, returnLocation, insurancePac, carModel,
				rentalEnd, rentalStart, orderId_str, clerkComment, towingAddress, pathCss, textCss, vehicleIdent;
		Long orderId;
		boolean isFleetRental;
		// tbc..

		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
		orderId = (long) variables.get("orderId");
		state = variables.get("state").toString();
		order = orderService.getOrder(orderId);
		customer = order.getCustomer();
		cars = order.getCars();

		surname = "surname";
		email = "email";
		from = "from";
		pickupLocation = "pickupLocation"; // order.getPick_up_store().getStoreName()
											// +
											// order.getPick_up_store().getCity();
		returnLocation = "returnLocation"; // order.getReturn_store().getStoreName()
											// +
											// order.getReturn_store().getCity();
		carModel = "carmodel"; // order.getCars();
		rentalStart = "rentalStart";
		rentalEnd = "rentalEnd";
		orderId_str = "orderId";
		towingAddress = "towingAddress";
		insurancePac = "insurancePac";
		vehicleIdent = " ";

		// Get rental information
		isFleetRental = order.isFleetRental();
		orderId_str = orderId.toString();
		surname = customer.getSurname();
		email = customer.getEmail();

		System.out.println("FLEET");
		System.out.println(isFleetRental);

		from = "bvis@bvis.com";
		if (!isFleetRental) {
			rentalStart = order.getPick_up_date().toString();
			rentalEnd = order.getReturn_date().toString();
			pickupLocation = order.getPickUpStore().getHTMLContactDetails();
			returnLocation = order.getReturnStore().getHTMLContactDetails();
			clerkComment = order.getClerkComments();
			insurancePac = order.getInsurance().getType().toString();
			for (Car loop_car : cars) {
				carModel += loop_car.getHTMLCarDetails() + "<br>";
				vehicleIdent += loop_car.getVehicleIdentificationNumber() + " ";
			}
		}

		subject = "";

		path = "/templates/";
		pathCss = "/templates/css.txt";

		// built email template path by state
		switch (state) {
		case "canc_fleet":
			path += "canc_fleet.txt";
			subject = "We are sorry... (No. " + orderId_str + ")";
			break;
		case "canc_single":
			path += "canc_single.txt";
			subject = "We are sorry... (No. " + orderId_str + ")";
			break;
		case "conf_req":
			if (isFleetRental) {
				path += "conf_req_fleet.txt";
				subject = "Booking reservation (No. " + orderId_str + ")";
				break;
			} else if (!isFleetRental) {
				path += "conf_req_single.txt";
				subject = "Booking reservation (No. " + orderId_str + ")";
				break;
			}
			break;
		case "rej_el":
			path += "rej_el.txt";
			subject = "We are sorry... (No. " + orderId_str + ")";
			break;
		case "rej_ins":
			path += "rej_ins.txt";
			subject = "We are sorry... (No. " + orderId_str + ")";
			break;
		case "send_cont":
			path += "send_cont.txt";
			subject = "Congratulation! (No. " + orderId_str + ")";
			break;
		case "send_sorrow":
			path += "send_sorrow.txt";
			subject = "Good bye (No. " + orderId_str + ")";
			order.setOrderStatus(OrderStatus.REJECTED);
			break;
		}

		InputStream in = this.getClass().getResourceAsStream(path);
		InputStream inCss = this.getClass().getResourceAsStream(pathCss);

		try {
			text = IOUtils.toString(in, "UTF-8");
			textCss = IOUtils.toString(inCss, "UTF-8");
		} catch (IOException e) {
			text = "error in file reading. path: " + path;
			textCss = "";
			email = "bvis@bvis.com";
		} catch (NullPointerException e) {
			text = "null pointer file reading. path: " + path;
			textCss = "";
			email = "bvis@bvis.com";
		}

		try {
			text = String.format(text, surname, carModel, pickupLocation, rentalStart, returnLocation, rentalEnd,
					orderId_str, towingAddress, insurancePac, vehicleIdent);
		} catch (IllegalFormatException e) {
			subject = "illegal conversion ";
			email = "bvis@bvis.com";
		}
		SendHTMLEmail.main(subject, textCss + text, from, email);

	}

	public void sendInquiryToCapitol(DelegateExecution delegateExecution) {
		System.out.println("SENDING TO CAPITOL INITIALIZED");
		SendInquiry sender = new SendInquiry();
		RentalOrder entityOrder = orderService.getOrder((Long) businessProcess.getVariable("orderId"));
		System.out.println("Sending inquiry with vehicle identification number: " + entityOrder.getCars().
				iterator().next().getVehicleIdentificationNumber());
		String result = sender.sendInquiry(entityOrder, delegateExecution.getProcessInstanceId());
		System.out.println("SENDING DONE. INSURANCE API RESPONSE: " + result);
		// TODO handle failures
	}

	public boolean handleInsuranceResponse(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		int orderID = Integer.parseInt(variables.get("orderID").toString());
		RentalOrder order = orderService.getOrder(orderID);
		Insurance insurance = order.getInsurance();
		insurance.setActualCosts(new BigDecimal(Double.parseDouble(variables.get("finalPrice").toString())));
		insurance.setInquiryText(variables.get("inquiryText").toString());
		order.setPrice(insurance.getActualCosts().doubleValue() + order.getPriceCars());
		int insuranceAnswer = Integer.parseInt(variables.get("insuranceResult").toString());
		if (insuranceAnswer == 0) {
			insurance.setInsuranceAnswer(InsuranceAnswer.REJECTED);
			System.out.println("INSURANCE REJECTED");
			order.setOrderStatus(OrderStatus.REJECTED);
		} else if (insuranceAnswer == 1) {
			insurance.setInsuranceAnswer(InsuranceAnswer.ACCEPTED);
			System.out.println("INSURANCE ACCEPTED");
			order.setOrderStatus(OrderStatus.ACCEPTED);
		} else {
			insurance.setInsuranceAnswer(InsuranceAnswer.ADJUSTED);
			System.out.println("INSURANCE ADJUSTED");
		}
		order.setInsurance(insurance);
		delegateExecution.removeVariables();
		orderService.updateOrder(order);
		// return true to trigger continuation
		return true;
	}

	public void sendContractConfirmation(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		int orderID = Integer.parseInt(variables.get("orderID").toString());
		RentalOrder order = orderService.getOrder(orderID);
		SendContractConfirmation client = new SendContractConfirmation();
		System.out.println("SENDING CONTRACT CONFIRMATION");
		String processInstanceIDBVIS = (String) variables.get("processId");
		String processInstanceIDCapitol = (String) variables.get("processIdCapitol");
		client.sendContractConfirmation(order, processInstanceIDBVIS, processInstanceIDCapitol, 1);
		System.out.println("CONTRACT CONFIRMATION SUCCESSFULLY SENT");										 // TODO
																									// insert
																									// proper
																									// contract
																									// status

	}

	public void sendReminder() {
		// TODO insert business logic
		// TODO create form
	}

	public void handleRequirements(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		
		// only gets called for single car rentals
		RentalOrder rentalOrder = getOrder((Long) variables.get("orderId"));
		Car car = rentalOrder.getCars().iterator().next();
		
		// get all cars that have the same model
		Collection<Car> sameCars = carService.getAllCarsByModel(car.getModel());
		
		// find a car that is available in the requested period for the current rental order
		Date begin = rentalOrder.getPick_up_date();
		Date end = rentalOrder.getReturn_date();
		boolean foundOne = false;
		for (Car c : sameCars) {
			boolean available = true;
			for (RentalOrder o : c.getRentalOrders()) {
				// if it is the same rental order as the current one continue
				if (o.equals(rentalOrder)) continue;
				// not available when
				// case 1: pickup before pickup and return after pickup
				// case 2: pickup after pickup and before return
				if ((o.getPick_up_date().before(begin) || o.getPick_up_date().equals(begin))
						&& (o.getReturn_date().after(begin) || o.getReturn_date().equals(begin))) {
					available = false;
					break;
				}
				else if ((o.getPick_up_date().after(begin) || o.getPick_up_date().equals(begin))
						&& (o.getPick_up_date().before(end) || o.getPick_up_date().equals(end))) {
					available = false;
					break;
				}
			}
			// if current car is available
			if (available) {
				foundOne = true;
				// update newly assigned car
				Collection<RentalOrder> currentOrders = c.getRentalOrders();
				currentOrders.add(rentalOrder);
				c.setRentalOrders(currentOrders);
				carService.updateCar(c);
				
				// update original car
				currentOrders = car.getRentalOrders();
				currentOrders.remove(rentalOrder);
				carService.updateCar(car);
				
				// update rental order
				ArrayList<Car> newCarList = new ArrayList<Car>();
				newCarList.add(c);
				rentalOrder.setCars(newCarList);
				orderService.updateOrder(rentalOrder);
			}
		}
		
		if (foundOne) System.out.println("Car available");
		else System.out.println("No car available");
		delegateExecution.setVariable("fulfillable", foundOne);
		
	}
	
	public double calcInsurancePrice(Collection<Car> cars, InsuranceType bookingInsuranceType, Date returnDate, Date pickupDate) {
        double insuranceTypeFactor = InsurancePriceMap.getInsuranceFactor(bookingInsuranceType);
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        double priceInsurance_expected = 0;
        
		for(Car carItem : cars) {
	        double carTypeFactor = InsurancePriceMap.getInsuranceFactor(carItem.getType());
	        int ps = carItem.getPs();
	        int construction_year = carItem.getConstructionYear();
	        int year_diff = current_year - construction_year;
	        
	        priceInsurance_expected += ((insuranceTypeFactor * carTypeFactor) + (ps * 0.15) + (20
	                - Math.pow(1.2, year_diff) / 30.0 )) /100;
	        
	        System.out.println(priceInsurance_expected);
		}
		
        Long diff = returnDate.getTime() - pickupDate.getTime();
        long rentDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        rentDays ++;
                
        double priceInsurance = priceInsurance_expected * (double) rentDays;
        
        return ((double) Math.round(priceInsurance*100))/100;
    }
	
	public boolean updateContract(DelegateExecution delegateExecution) {
		Map<String, Object> variables = delegateExecution.getVariables();
		long newCarID = Long.parseLong(variables.get("newCarId")+"");
		Car newCar = carService.getCar(newCarID);
		RentalOrder order = orderService.getOrder(Long.parseLong(variables.get("orderId")+""));
		ArrayList<Car> cars = new ArrayList<Car>();
		cars.add(newCar);
		order.setCars(cars);
		orderService.updateOrder(order);
		System.out.println("NEW CAR: " + order.getCars().iterator().next().getModel());
		return true;
	}
	
	
	public double calcCarPrice(Collection<Car> cars, Date returnDate, Date pickupDate) {
		Long diff = returnDate.getTime() - pickupDate.getTime();
		long rentDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
		long price = 0;
		for(Car carItem : cars) {
			price += (long) CarPriceMap.getPrice(carItem.getType());
		}
		return rentDays * price;
	}
}
