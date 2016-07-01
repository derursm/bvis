package org.camunda.bpm.bvis.Entities;

import java.util.HashMap;

public final class InsurancePriceMap {
    static HashMap<String, Double> map = new HashMap<String, Double>();
    
    static {
    	// car types and their insurance price factors
        map.put("mini_car", 0.85);
        map.put("small_car", 1.0);  
        map.put("car", 1.2);
        map.put("kombi", 1.4);
        map.put("pickup", 1.65);
        map.put("van", 1.8);
        map.put("limousine", 2.25);
        map.put("truck", 2.5);
        
        // insurance types and their insurance price factors
        map.put("total", 2.2);
        map.put("partial", 1.1);   
        map.put("liability", 0.7);
    }
    
    public static double getInsuranceFactor(CarType carType) {
    	String carTypeString = carType.toString();
        return map.get(carTypeString);
    }
   
    public static double getInsuranceFactor(InsuranceType insuranceType) {
    	String insuranceTypeString = insuranceType.toString();
        return map.get(insuranceTypeString);    	
    }
}