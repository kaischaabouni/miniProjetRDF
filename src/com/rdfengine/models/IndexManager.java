package com.rdfengine.models;

import java.util.HashMap;

public class IndexManager {
	private static volatile IndexManager instance = null;
	
	// Index
	private HashMap<Integer, PropertyTable> index = new HashMap<Integer, PropertyTable> ();
	
	// Private constructor so that it cannot be instantiated outside
	private IndexManager() {
		super();
	}

	/**
	 * @return Return the instance of the singleton Dictionary.
	 */
	public final static IndexManager getInstance() {

		// Instantiate if not yet instantiated
		if (IndexManager.instance == null) {
			// Prevent multiple instantiation
			synchronized(IndexManager.class) {
				if (IndexManager.instance == null) {
					IndexManager.instance = new IndexManager();
				}
			}
		}
		return IndexManager.instance;
	}

	/*
	 * Add triple to Index
	 */
	public void addTripleToIndex(int subject, int property, int object){
		PropertyTable propertyTable;
		if(index.containsKey(property)){
			propertyTable = index.get(property);
		} else {
			propertyTable = new PropertyTable();
			index.put(property, propertyTable);
		}
		propertyTable.addSubjectObject(subject, object);
	}
	
}
