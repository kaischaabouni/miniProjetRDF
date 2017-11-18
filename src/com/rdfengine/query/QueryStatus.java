package com.rdfengine.query;

import java.util.ArrayList;

import com.rdfengine.models.TriplePatternOfStarQuery;

public class QueryStatus {
	private boolean queryExecutionCompleted;
	private String queriesSubjectVariableName;
	private ArrayList<TriplePatternOfStarQuery> queriesTriplePatternsList;
	private ArrayList<Integer> queryResult;
	
	public QueryStatus(boolean queryExecutionCompleted, String queriesSubjectVariableName,
			ArrayList<TriplePatternOfStarQuery> queriesTriplePatternsList) {
		super();
		this.queryExecutionCompleted = queryExecutionCompleted;
		this.queriesSubjectVariableName = queriesSubjectVariableName;
		this.queriesTriplePatternsList = queriesTriplePatternsList;
	}

	public QueryStatus() {
		super();
		queryExecutionCompleted = false;
		queriesSubjectVariableName = "V0";
	}

	public boolean isQueryExecutionCompleted() {
		return queryExecutionCompleted;
	}

	public void setQueryExecutionCompleted(boolean queryExecutionCompleted) {
		this.queryExecutionCompleted = queryExecutionCompleted;
	}

	public String getQueriesSubjectVariableName() {
		return queriesSubjectVariableName;
	}

	public void setQueriesSubjectVariableName(String queriesSubjectVariableName) {
		this.queriesSubjectVariableName = queriesSubjectVariableName;
	}

	public ArrayList<TriplePatternOfStarQuery> getQueriesTriplePatternsList() {
		return queriesTriplePatternsList;
	}

	public void setQueriesTriplePatternsList(ArrayList<TriplePatternOfStarQuery> queriesTriplePatternsList) {
		this.queriesTriplePatternsList = queriesTriplePatternsList;
	}

	public ArrayList<Integer> getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(ArrayList<Integer> queryResult) {
		this.queryResult = queryResult;
	}
		

}
