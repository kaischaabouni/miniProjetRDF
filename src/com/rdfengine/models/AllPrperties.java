package com.rdfengine.models;

import java.util.HashMap;

public abstract class AllPrperties {
	private static HashMap<Integer, PropertyTable> allProperties= new HashMap<Integer, PropertyTable>();
	
	public static void addPropertyTable(int propertyID, PropertyTable propertyTable) {
		allProperties.put(propertyID, propertyTable);
	}

	public static HashMap<Integer, PropertyTable> getTriplets() {
		return allProperties;
	}
	
}
