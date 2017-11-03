package com.rdfengine.datasetconstruction;

import java.io.BufferedReader;
import java.io.FileReader;

public abstract class Loader {

	public static void loadData(String filePath) {
		Dictionary dictionary = Dictionary.getInstance();
		String line = null;
		String tripletString = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			String[] splittedTrippleString = new String[3];

			while ((line = bufferedReader.readLine()) != null) {
				
				// Stock Next triple in tripletString
				tripletString += line;
				if(!line.endsWith(".")){
					continue;
				}

				// Rremoving trailing
				while(!tripletString.endsWith("\"") && !tripletString.endsWith(">")){
					tripletString = tripletString.substring(0, tripletString.length()-1);
				}
				
				// Split tripletString to Subject, Predicate and Object
				splittedTrippleString = tripletString.split("\t");
				
				// Add Subject, Predicate and Object to Dictionary, AllProperties and AllSubjects
				for(int i=0; i < splittedTrippleString.length; i++) {
					if(!dictionary.containsResource(splittedTrippleString[i])) {
						dictionary.addResource(splittedTrippleString[i]);
					}					
				}
				
				//######## add in if ou after for
				int subjectID = dictionary.getId(splittedTrippleString[0]);
				int predicateID = dictionary.getId(splittedTrippleString[1]);
				int objectID = dictionary.getId(splittedTrippleString[2]);
				AllProperties.addTriple(subjectID, predicateID, objectID);
			}
			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
