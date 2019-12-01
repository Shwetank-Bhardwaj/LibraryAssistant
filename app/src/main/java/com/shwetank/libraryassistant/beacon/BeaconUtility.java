package com.shwetank.libraryassistant.beacon;

import java.util.HashMap;
import java.util.Map;

public class BeaconUtility {

    private static Map<String, String> mapping = new HashMap<>();

    static {
        mapping.put("1","109242");
        mapping.put("2","56427");
        mapping.put("3","109123");
        mapping.put("4","19338");
        mapping.put("5","34435");
        mapping.put("6","50272");
        mapping.put("7","109244");
        mapping.put("8","42675");
        mapping.put("9","54465");
        mapping.put("10","36298");
        mapping.put("11","15127");
    }
    public static String getBeaconId(String num) {
        return mapping.get(num);
    }

}
