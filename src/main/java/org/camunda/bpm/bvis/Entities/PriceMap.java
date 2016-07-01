package org.camunda.bpm.bvis.Entities;

import java.util.HashMap;

public final class PriceMap {
    static HashMap<String, Double> map = new HashMap<String, Double>();
    
    static {
        map.put("TYPE1", 50.0);
        map.put("TYPE2", 75.0);  
        map.put("TYPE3", 100.0);
    }
    
    public static double getPrice(CarType carType) {
    	String carTypeString = carType.toString();
        return map.get(carTypeString);
    }
}