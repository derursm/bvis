package org.camunda.bpm.bvis.entities;

import java.util.HashMap;

public final class CarPriceMap {
    static HashMap<String, Double> map = new HashMap<String, Double>();
    
    static {
    	// car types and their prices per day
        map.put("mini_car", 30.0);
        map.put("small_car", 40.0);  
        map.put("car", 50.0);
        map.put("kombi", 60.0);
        map.put("pickup", 70.0);
        map.put("van", 80.0);
        map.put("limousine", 90.0);
        map.put("truck", 100.0);
    }
    
    public static double getPrice(CarType carType) {
    	String carTypeString = carType.toString();
        return map.get(carTypeString);
    }
}