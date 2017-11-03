package com.rdfengine.loading;

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
				
				// Stock Next triplet in tripletString
				tripletString += line;
				if(!line.endsWith(".")){
					continue;
				}

				// Removing trailing
				while(!tripletString.endsWith("\"") && !tripletString.endsWith(">")){
					tripletString = tripletString.substring(0, tripletString.length()-1);
				}
				
				// Split tripletString to Subject, Predicate and Object
				splittedTrippleString = tripletString.split("\t");
				tripletString = "";
				
				/*
				 *  Add Subject, Predicate and Object to Dictionary, AllProperties and AllSubjects
				 */
				
				// Subject
				int subjectID;
				if(!dictionary.containsResource(splittedTrippleString[0])) {
					dictionary.addResource(splittedTrippleString[0]);
					subjectID = dictionary.getId(splittedTrippleString[0]);
					AllSubjects.addSubject(subjectID);
				} else {
					subjectID = dictionary.getId(splittedTrippleString[0]);
				}
				
				// Property
				int predicateID;
				if(!dictionary.containsResource(splittedTrippleString[1])) {
					dictionary.addResource(splittedTrippleString[1]);
					predicateID = dictionary.getId(splittedTrippleString[1]);
					AllProperties.addProperty(predicateID);
				} else {
					predicateID = dictionary.getId(splittedTrippleString[1]);
				}
				
				// Object
				int objectID;
				if(!dictionary.containsResource(splittedTrippleString[2])) {
					dictionary.addResource(splittedTrippleString[2]);
				}	
				objectID = dictionary.getId(splittedTrippleString[2]);
				
				// Add Subject And Object To Existing Property
				// The Property Table corresponding to predicateID is already created above
				AllProperties.addSubjectAndObjectToExistingProperty(subjectID, predicateID, objectID);
			}
			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
