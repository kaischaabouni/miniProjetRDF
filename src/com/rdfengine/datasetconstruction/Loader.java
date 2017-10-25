package com.rdfengine.datasetconstruction;

import java.io.BufferedReader;
import java.io.FileReader;

import com.rdfengine.models.RDFDictionary;
import com.rdfengine.models.Triplet;

public abstract class Loader {

	//public static String filePath = "assets/datasets/500K.rdf";

	public static void loadData(String filePath)
	{
		String line = null;
		String currentResource;
		Triplet currentTriplet;
		String tripletString = "";
		int currentResourceValue;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			String[] split = new String[3];

			while ((line = bufferedReader.readLine()) != null) {
				tripletString += line;
				if(!line.endsWith("."))
					continue;
				currentTriplet = new Triplet();

				//removing trailing
				while(!tripletString.endsWith("\"") && !tripletString.endsWith(">")){
					tripletString = tripletString.substring(0, tripletString.length()-1);
				}
				split = tripletString.split("\t");
				for(int i=0; i<3; i++) {
					currentResource = split[i];
					if(!RDFDictionary.getInstance().containsResource(currentResource)) {
						RDFDictionary.getInstance().addResource(currentResource);
						currentResourceValue = RDFDictionary.getInstance().getLastId()-1;
					} else {
						currentResourceValue = RDFDictionary.getInstance().getId(currentResource);
					}
					currentTriplet.setNext(currentResourceValue);
					tripletString = "";
				}
				Triplets.addTriplet(currentTriplet);
			}
			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
