package com.rdfengine.models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public final class RDFDictionary{

	private static volatile RDFDictionary instance = null;

	// Dictionary
	private HashMap<Integer, String> dictionary;
	
	// Private constructor
	private RDFDictionary() {
		super();
		dictionary = new HashMap<Integer, String> ();
	}

	
	/**
	 * @return Return the instance of the singleton Dictionary.
	 */
	public final static RDFDictionary getInstance() {

		// Instantiate if not yet instanciated
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

	// Add new value
	public void put(Integer key, String value) {
		this.dictionary.put(key, value);
	}

	// Get value
	public String get(Integer key){
		return this.dictionary.get(key);
	}

	// Remove value
	public void remove(Integer key) {
		this.dictionary.remove(key);
	}
	
	// Add RDF Triplets Values From File
	public void addRDFValuesFromFile(String filePath){
		try {
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			Integer i = 0;
			RDFDictionary dictionary = getInstance();
			while ((strLine = br.readLine()) != null)   {
				String[] values = strLine.split(" ");
				System.out.println(values[0]);
				dictionary.put(i, values[0]);
				System.out.println(values[1]);
				dictionary.put(i, values[1]);
				System.out.println(values[2]);
				dictionary.put(i, values[2]);
				i++;
			}
			in.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}	
}
