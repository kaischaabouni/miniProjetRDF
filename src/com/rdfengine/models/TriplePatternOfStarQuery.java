package com.rdfengine.models;

public class TriplePatternOfStarQuery {
	
	private Integer predicateID;
	private Integer objectID;

	public TriplePatternOfStarQuery() {
		super();
	}
	
	public TriplePatternOfStarQuery(Integer predicateID, Integer objectID) {
		super();
		this.predicateID = predicateID;
		this.objectID = objectID;
	}
	
	public Integer getPredicateID() {
		return predicateID;
	}
	
	public void setPredicateID(Integer predicateID) {
		this.predicateID = predicateID;
	}
	
	public Integer getObjectID() {
		return objectID;
	}
	
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
}
