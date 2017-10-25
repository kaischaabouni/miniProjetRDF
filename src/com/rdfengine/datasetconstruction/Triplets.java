package com.rdfengine.datasetconstruction;

import java.util.ArrayList;

import com.rdfengine.models.Triplet;

public abstract class Triplets {

	static ArrayList<Triplet> triplets = new ArrayList<Triplet>();

	public static void addTriplet(Triplet triplet) {
		triplets.add(triplet);
	}

	public static ArrayList<Triplet> getTriplets() {
		return triplets;
	}
}