package com.rdfengine.models;

import java.util.HashMap;

public final class RDFDictionary{

	private static volatile RDFDictionary instance = null;

	// Dictionary
	private HashMap<String, Integer> resourceToId = new HashMap<String, Integer> ();
	private HashMap<Integer, String> idToResource = new HashMap<Integer, String> ();

	// Last value (increment each insert)
	private int lastId = 0;

	// Private constructor so that it cannot be instantiated outside
	private RDFDictionary() {
		super();
	}


	/**
	 * @return Return the instance of the singleton Dictionary.
	 */
	public final static RDFDictionary getInstance() {

		// Instantiate if not yet instantiated
		if (RDFDictionary.instance == null) {
			// Prevent multiple instantiation
			synchronized(RDFDictionary.class) {
				if (RDFDictionary.instance == null) {
					RDFDictionary.instance = new RDFDictionary();
				}
			}
		}
		return RDFDictionary.instance;
	}

	// Add new Resource
	public void addResource(String resource) {	
		resourceToId.put(resource, lastId);
		idToResource.put(lastId, resource);
		lastId++;
	}

	// get Resource by ID
	public String getResource(int id) {
		return idToResource.get(id);
	}

	// get ID by Resource
	public int getId(String resource) {
		return resourceToId.get(resource);
	}

	// check if dictionary contains Resource
	public boolean containsResource(String resource) {
		return resourceToId.containsKey(resource);
	}

	// check if dictionary contains ID
	public boolean containsId(int id) {
		return idToResource.containsKey(id);
	}

	// Remove Resource
	public void remove(String resource) {
		idToResource.remove(resourceToId.get(resource));
		resourceToId.remove(resource);
	}

	// Remove Resource by id
	public void remove(int id) {
		resourceToId.remove(idToResource.get(id));
		idToResource.remove(id);
	}

	// Print All Resources of the dictionary
	public void showAll() {
		for(String resource: resourceToId.keySet())	{
			System.out.println("Ressource: " + resource + "	----->" + getId(resource));
		}
	}

	// Last ID
	public int getLastId(){
		return lastId;
	}
}
