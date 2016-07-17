package org.camunda.bpm.bvis.entities;

import java.util.HashMap;

public final class CarPriceMap {
    static HashMap<String, Double> map = new HashMap<String, Double>();
    
    static {
    	// car types and their prices per day
        map.put("mini_car", 30.0);
        map.put("small_car", 40.0);  
        map.put("car", 60.0);
        map.put("kombi", 80.0);
        map.put("pickup", 120.0);
        map.put("van", 100.0);
        map.put("limousine", 160.0);
        map.put("truck", 250.0);
    }
    
    public static double getPrice(CarType carType) {
    	String carTypeString = carType.toString();
        return map.get(carTypeString);
    }
}