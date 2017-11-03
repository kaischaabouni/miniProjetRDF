package com.rdfengine.triplets;

import java.util.ArrayList;

public abstract class Triplets {

	static ArrayList<Triplet> triplets = new ArrayList<Triplet>();

	public static void addTriplet(Triplet triplet) {
		triplets.add(triplet);
	}

	public static ArrayList<Triplet> getTriplets() {
		return triplets;
	}
}